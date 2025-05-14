// 파일: network/TokenAuthenticator.kt
package com.example.sparcs_new.network

import com.example.sparcs_new.DTO.RefreshTokenRequest
import com.example.sparcs_new.data.DataTokenStore
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    private val tokenStore: DataTokenStore,
    private val authApi: AuthApiService
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= 2) return null

        return runBlocking {
            val refreshToken = tokenStore.getRefreshToken() ?: return@runBlocking null

            val res = authApi.refreshToken(RefreshTokenRequest(refreshToken))
            if (!res.isSuccessful || res.body() == null) return@runBlocking null

            val newAccess = res.body()!!.accessToken
            tokenStore.saveAccessToken(newAccess)

            response.request.newBuilder()
                .header("Authorization", "Bearer $newAccess")
                .build()
        }
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var prior = response.priorResponse
        while (prior != null) {
            count++
            prior = prior.priorResponse
        }
        return count
    }
}
