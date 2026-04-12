package com.sentinel.editor.utils

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

/**
 * GitHub HTTP client wrapper
 * Handles authentication, rate limiting, and request/response intercepting
 * 
 * License: Apache 2.0 via com.sentinel.editor
 */
class GitHubClient private constructor(
    private val context: Context,
    private val client: OkHttpClient,
    private val baseUrl: String
) {
    
    val baseUrl: String = baseUrl
    
    val userAgent: String = "SentinelEditor/1.0"
    
    /**
     * Create request from path
     */
    fun makeRequest(path: String): GitHubRequest {
        val url = baseUrl + path
        val request = Request.Builder().url(url).build()
        return GitHubRequest(this, request)
    }
    
    /**
     * Check if rate limited
     */
    @Deprecated("Use OkHttp response headers directly")
    fun isRateLimited(): Boolean {
        // In a real implementation, check response headers
        return false
    }
    
    /**
     * Create new client instance
     */
    @JvmStatic
    fun create(context: Context, baseUrl: String = "https://api.github.com"): GitHubClient {
        val loggingInterceptor = okhttp3.logging.HttpLoggingInterceptor().apply {
            level = okhttp3.logging.HttpLoggingInterceptor.Level.BODY
        }
        
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                var originalRequest = chain.request()
                originalRequest = originalRequest.newBuilder()
                    .header("Accept", "application/vnd.github.v3+json")
                    .header("User-Agent", userAgent)
                    .build()
                chain.proceed(originalRequest)
            }
            .connectTimeout(50, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
        
        return GitHubClient(context, client, baseUrl)
    }
}

/**
 * GitHub API request wrapper
 */
class GitHubRequest(
    private val client: GitHubClient,
    val request: Request
) {
    
    private val path: String
        get() = request.url.toString().removePrefix(client.baseUrl).removePrefix("/")
    
    /**
     * Execute request with authentication
     */
    suspend fun execute(
        token: String,
        path: String = this.path
    ): GitHubResponse {
        val requestBuilder = request.newBuilder()
            .header("Authorization", "token $token")
            .header("Accept", "application/vnd.github.v3+json")
            .header("User-Agent", client.userAgent)
        
        // TODO: Implement actual GitHub API call
        val response = GitHubResponse(client.baseUrl + path, requestBuilder.build())
        return response
    }
}

/**
 * GitHub API response wrapper
 */
data class GitHubResponse(
    val code: Int,
    val body: String? = null,
    val headers: Map<String, String> = emptyMap()
) {
    
    val success: Boolean
        get() = code in 200..299
    
    val isOk: Boolean
        get() = code == 200
}
