package com.example.sparcs_new.network

import android.util.Log
import com.example.sparcs_new.data.DataTokenStore
import com.example.sparcs_new.data.AuthManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response


class AuthInterceptor(
    private val tokenStore: DataTokenStore,
    private val authApi: AuthApiService
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (response.code == 401 || response.code == 403) {
            return runBlocking {
                val refreshToken = tokenStore.getRefreshToken()
                if (refreshToken == null) {
                    Log.e("AuthInterceptor", "No refresh token available. User is not authenticated.")
                    navigateToLoginScreen()
                    return@runBlocking response
                }

                val refreshResponse = try {
                    authApi.refreshToken(refreshToken)
                } catch (e: Exception) {
                    Log.e("AuthInterceptor", "Refresh token request failed: ${e.message}")
                    navigateToLoginScreen()
                    return@runBlocking response
                }

                if (!refreshResponse.isSuccessful || refreshResponse.body() == null) {
                    Log.e("AuthInterceptor", "Refresh failed: ${refreshResponse.code()} ${refreshResponse.errorBody()?.string()}")
                    tokenStore.clearTokens()
                    navigateToLoginScreen()
                    return@runBlocking response
                }

                val newAccessToken = refreshResponse.body()!!.accessToken
                tokenStore.saveAccessToken(newAccessToken)

                Log.d("AuthInterceptor", "Access token refreshed. Retrying original request.")

                val newRequest = request.newBuilder()
                    .removeHeader("Authorization")
                    .addHeader("Authorization", "Bearer $newAccessToken")
                    .build()

                chain.proceed(newRequest)
            }
        }

        return response
    }
    private fun navigateToLoginScreen() {
        AuthManager.setUnauthenticated()
    }
}

