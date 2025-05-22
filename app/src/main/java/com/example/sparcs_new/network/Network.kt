package com.example.sparcs_new.network

import android.content.Context
import com.example.sparcs_new.data.DataTokenStore
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


object NetworkSetModule {

    fun provideAuthApi(context: Context): AuthApiService {
        val tokenStore = DataTokenStore(context)
        val noAuthApi = provideAuthApiWithoutToken()
        val authInterceptor = AuthInterceptor(tokenStore, noAuthApi)

        val header = Interceptor {
            val original = it.request()
            val requestBuilder = original.newBuilder()

            runBlocking {
                val token = tokenStore.getAccessToken()
                token?.let {
                    requestBuilder.header("Authorization", "Bearer $it")
                }
            }

            it.proceed(requestBuilder.build())
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(header)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        return Retrofit.Builder()
            .baseUrl("http://210.117.237.78:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
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
