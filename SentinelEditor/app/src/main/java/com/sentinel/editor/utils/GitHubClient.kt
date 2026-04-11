package com.sentinel.editor.utils

import android.content.Context
import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import okhttp3.internal.http.HttpMethod
import java.time.Instant
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
    private val baseurl: String
) {
    
    val baseurl: String = baseurl
    
    /**
     * Build GitHubAPI client
     */
    fun makeRequest(path: String): GitHubRequest {
        val url = baseurl + path
        val request = Request.Builder().url(url).build()
        return GitHubRequest(this, request)
    }
    
    /**
     * Check if rate limited
     */
    fun isRateLimited(): Boolean {
        val client = client
        val response = client.newCall(request).execute()
        val rateLimitHeader = response.headers["X-RateLimit-Remaining"].toIntOrNull() ?: 50
        return rateLimitHeader < 5
    }
    
    companion object {
        fun Builder(
            context: Context,
            baseUrl: String = "https://api.github.com",
            userAgent: String = "SentinelEditor/1.0"
        ): Builder {
            return Builder(context, baseUrl, userAgent)
        }
    }
}
