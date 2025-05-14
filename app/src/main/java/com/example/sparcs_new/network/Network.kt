// 파일: network/NetworkSetModule.kt
package com.example.sparcs_new.network

import android.content.Context
import com.example.sparcs_new.data.DataTokenStore
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkSetModule {

    fun provideAuthApi(context: Context): AuthApiService {
        val tokenStore = DataTokenStore(context)
        val noAuthApi = provideAuthApiWithoutToken()
        val authenticator = TokenAuthenticator(tokenStore, noAuthApi)

        val authInterceptor = Interceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()

            runBlocking {
                val token = tokenStore.getAccessToken()
                token?.let {
                    requestBuilder.header("Authorization", "Bearer $it")
                }
            }

            chain.proceed(requestBuilder.build())
        }

        val client = OkHttpClient.Builder()
            .authenticator(authenticator)
            .addInterceptor(authInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        return Retrofit.Builder()
            .baseUrl("http://210.117.237.78:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(AuthApiService::class.java)
    }

    private fun provideAuthApiWithoutToken(): AuthApiService {
        return Retrofit.Builder()
            .baseUrl("http://210.117.237.78:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApiService::class.java)
    }
}
