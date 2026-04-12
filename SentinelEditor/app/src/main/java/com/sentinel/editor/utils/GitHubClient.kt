package com.sentinel.editor.utils

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

class GitHubClient private constructor(
    private val context: Context,
    private val client: OkHttpClient,
    val baseUrl: String,
    val userAgent: String
) {

    fun makeRequest(path: String): Request {
        val normalizedPath = if (path.startsWith("/")) path else "/$path"
        return Request.Builder()
            .url(baseUrl + normalizedPath)
            .build()
    }

    fun isRateLimited(): Boolean = false

    companion object {
        @JvmStatic
        fun create(context: Context, baseUrl: String = "https://api.github.com"): GitHubClient {
            return Builder(context)
                .baseUrl(baseUrl)
                .build()
        }
    }

    class Builder(
        private val context: Context,
        private var baseUrl: String = "https://api.github.com",
        private var userAgent: String = "SentinelEditor/1.0"
    ) {
        fun baseUrl(baseUrl: String) = apply { this.baseUrl = baseUrl }

        fun userAgents(userAgents: Set<String>) = apply {
            this.userAgent = userAgents.firstOrNull() ?: this.userAgent
        }

        fun build(): GitHubClient {
            val configuredClient = OkHttpClient.Builder()
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

            return GitHubClient(context, configuredClient, baseUrl, userAgent)
        }
    }
}
