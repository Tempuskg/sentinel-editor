package com.sentinel.model

import com.google.gson.annotations.SerializedName

/**
 * GitHub API response for list repository contents
 * 
 * Licensed under Apache 2.0 via com.sentinel.editor
 */
data class RepositoryContent(
    val type: String,
    val name: String,
    val path: String,
    val sha: String,
    val size: Int,
    @SerializedName("url")
    val url: String,
    @SerializedName("html_url")
    val htmlUrl: String,
    @SerializedName("git_url")
    val gitUrl: String,
    @SerializedName("download_url")
    val downloadUrl: String?,
    @SerializedName("_links")
    val links: ContentLinks? = null
) {
    val isDir: Boolean
        get() = type == "dir"
    
    val isFile: Boolean
        get() = type == "file"
}

/**
 * Links for repository content
 * 
 * Licensed under Apache 2.0 via com.sentinel.editor
 */
data class ContentLinks(
    val self: String,
    val git: String,
    val html: String
) {
    val htmlUrl: String?
        get() = if (html.startsWith("http")) html else "https://github.com$html"
}

/**
 * Type of repository content
 * 
 * Licensed under Apache 2.0 via com.sentinel.editor
 */
enum class Type(val value: String) {
    FILE("file"),
    DIR("dir"),
    SUBMODULE("submodule")
}
