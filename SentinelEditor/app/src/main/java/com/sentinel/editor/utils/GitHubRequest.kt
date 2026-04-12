package com.sentinel.editor.utils

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class GitHubRequest(
    private val client: GitHubClient
) {
    fun buildBody(content: String?): RequestBody? {
        if (content.isNullOrBlank()) return null
        return content.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
    }
}
