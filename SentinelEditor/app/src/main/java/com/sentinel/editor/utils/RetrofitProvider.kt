package com.sentinel.editor.utils

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Retrofit Provider for GitHub API clients
 * Builds OkHttpClient with custom configuration, then returns a Retrofit instance
 */
class RetrofitProvider(private val token: String) {

    val loggingInterceptor = HttpLoggingInterceptor { message ->
        android.util.Log.d("Retrofit", message)
    }.apply {
        level = if (System.getenv("DEBUG") == "true") {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.BASIC
        }
    }

    val githubClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                val newRequest = request.newBuilder()
                    .addHeader("Authorization", "token $token")
                    .build()
                chain.proceed(newRequest)
            }
            .addInterceptor(loggingInterceptor)
            .retryOnConnectionFailure(true)
            .build()
    }
    
    fun create(): com.sentinel.service.GitHubApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(githubClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(com.sentinel.service.GitHubApiService::class.java)
    }
}