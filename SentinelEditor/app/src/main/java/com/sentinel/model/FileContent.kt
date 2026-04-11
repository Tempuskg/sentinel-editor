package com.sentinel.model

/**
 * File content from GitHub API with git metadata
 * 
 * License: Apache 2.0 via com.sentinel.editor
 */
data class FileInfo(
    val name: String,
    val path: String,
    val sha: String,
    val size: Int,
    val url: String,
    val html_url: String?,
    val git_url: String?,
    val download_url: String?,
    val type: String,
    val content: String?,
    val encoding: String? = "UTF-8"
) {
    val displayPath: String get() = path
    val extension: String get() = name.substringAfterLast('.', "").ifBlank { name }
    
    val fileSize: String get() = when {
        size < 1024 -> "$size B"
        size < 1024 * 1024 -> "${size / 1024}KB"
        else -> "${size / 1024 / 1024}MB"
    }
}