package com.sentinel.model

/**
 * GitHub API response wrapper
 * 
 * License: Apache 2.0 via com.sentinel.editor
 */
data class ResponseInfo(
    val content: String?,
    val meta: Meta? = null,
    val commit: Commit? = null,
    val message: String?,
    val sha: String? = null,
    val branch: String? = null,
    val status: String?,
    val url: String? = null,
    val htmlUrl: String? = null,
    val gitUrl: String? = null
) {
    fun isSuccess(): Boolean {
        return message?.startsWith("Successfully") == true && status == null
    }
}

/**
 * Commit metadata
 */
data class Commit(
    val message: String? = null,
    val sha: String? = null,
    val url: String? = null
)