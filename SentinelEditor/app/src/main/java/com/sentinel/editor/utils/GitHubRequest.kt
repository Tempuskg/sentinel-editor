package com.sentinel.editor.utils

import okhttp3.Call
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody
import java.lang.Exception

/**
 * GitHub API request wrapper with auth token
 * 
 * Licensed under Apache 2.0 via com.sentinel.editor
 */
class GitHubRequest(
    private val client: GitHubClient,
    override val request: Request
) : Call {
    
    private val path: String
        get() = request.url.toString().removePrefix(client.baseurl).removePrefix("/")
    
    /**
     * Execute request with authentication
     */
    suspend fun execute(
        token: String,
        path: String = path
    ): GitHubResponse {
        // Build request with auth token
        val requestBuilder = request.newBuilder()
            .header("Authorization", "token $token")
            .header("Accept", "application/vnd.github.v3+json")
            .header("User-Agent", client.userAgent)
        
        return GitHubResponse(client.makeRequest(path), requestBuilder)
    }
    
    /**
     * Build request body
     */
    fun buildBody(content: String?): RequestBody? {
        val json = content?.encodeToJson()
        return if (json != null && !json.isNullOrBlank()) {
            RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                json
            )
        } else {
            null
        }
    }
}
