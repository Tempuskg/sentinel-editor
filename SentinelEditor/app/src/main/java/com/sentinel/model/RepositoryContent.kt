package com.sentinel.model

/**
 * Repository file content
 * 
 * License: Apache 2.0 via com.sentinel.editor
 */
data class RepositoryContent(
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
    val encoding: String? = "UTF-8",
    val sizeInBytes: Int,
    val lastModified: Long,
    val gitMetadata: GitMetadata?
) {
    val displayName: String get() = if (name.contains('/')) name.substringAfterLast('/') else name
    val extension: String get() = displayName.substringAfterLast('.', "").ifBlank { displayName }
    val isFile: Boolean get() = type == "file"
    val isTruncated: Boolean get() = content?.length?.let { it > 0 && it < size } ?: false
}