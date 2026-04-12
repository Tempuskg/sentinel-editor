package com.sentinel.model

import com.google.gson.annotations.SerializedName

/**
 * GitHub API response for file content
 * 
 * Licensed under Apache 2.0 via com.sentinel.editor
 */
data class FileContent(
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
    val downloadUrl: String,
    val type: String,
    @SerializedName("_links")
    val links: FileContentLinks? = null,
    val content: String? = null,
    @SerializedName("encoding")
    val encoding: String? = null,
    val commit: GitCommit? = null
) {
    val isDir: Boolean
        get() = type == "dir"
    
    val isFile: Boolean
        get() = type == "file"
}

/**
 * Links for file content
 * 
 * Licensed under Apache 2.0 via com.sentinel.editor
 */
data class FileContentLinks(
    @SerializedName("self")
    val self: String?,
    @SerializedName("git")
    val git: String?,
    @SerializedName("html")
    val html: String?
)
