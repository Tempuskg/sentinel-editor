package com.sentinel.editor.utils

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.Request

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
            return GitHubClient(context, OkHttpClient(), baseUrl, userAgent)
        }
    }
}
