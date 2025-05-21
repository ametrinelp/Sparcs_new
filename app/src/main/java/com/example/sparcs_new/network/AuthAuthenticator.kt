package com.example.sparcs_new.network

import android.util.Log
import com.example.sparcs_new.DTO.RefreshTokenRequest
import com.example.sparcs_new.data.DataTokenStore
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route


class AuthAuthenticator(
    private val tokenStore: DataTokenStore,
    private val authApi: AuthApiService
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.request.header("Authorization") != null && responseCount(response) >= 2) {
            return null
        }

        val refreshToken = runBlocking { tokenStore.getRefreshToken() } ?: return null

        val tokenResponse = runBlocking {
            try {
                val result = authApi.refreshToken(RefreshTokenRequest(refreshToken).toString())
                if (result.isSuccessful) result.body()?.accessToken else null
            } catch (e: Exception) {
                null
            }
        } ?: return null

        tokenStore.saveAccessToken(tokenResponse)
        Log.d("Authenticator", "401 received, trying to refresh token...")

        return response.request.newBuilder()
            .header("Authorization", "Bearer $tokenResponse")
            .build()
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var priorResponse = response.priorResponse
        while (priorResponse != null) {
            count++
            priorResponse = priorResponse.priorResponse
        }
        return count
    }
}
