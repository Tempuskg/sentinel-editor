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
    @SerializedName("_encoding")
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

/**
 * Commit information for file content
 * 
 * Licensed under Apache 2.0 via com.sentinel.editor
 */
data class GitCommit(
    val url: String,
    val sha: String,
    @SerializedName("html_url")
    val htmlUrl: String,
    val author: GitCommitAuthor? = null,
    val committer: GitCommitAuthor? = null,
    val message: String,
    val timestamp: String,
    val verification: GitCommitVerification? = null,
    @SerializedName("parent_count")
    val parentCount: Int = 0,
    val parents: List<GitCommitCommit>
) {
    val parentSha: String
        get() = if (parents.isNotEmpty()) {
            parents[0].sha
        } else {
            sha
        }
}

/**
 * Commit author
 * 
 * Licensed under Apache 2.0 via com.sentinel.editor
 */
data class GitCommitAuthor(
    val name: String,
    @SerializedName("email")
    val email: String,
    val user: GitCommitAuthorUser? = null
) {
    val login: String?
        get() = user?.login
}

/**
 * Commit commit (parent)
 * 
 * Licensed under Apache 2.0 via com.sentinel.editor
 */
data class GitCommitCommit(
    val sha: String,
    @SerializedName("url")
    val url: String
)

/**
 * Commit verification
 * 
 * Licensed under Apache 2.0 via com.sentinel.editor
 */
data class GitCommitVerification(
    val verified: Boolean,
    val reason: String? = null,
    val payload: String? = null,
    @SerializedName("signature")
    val signature: String? = null,
    @SerializedName("signature_algorithm")
    val signatureAlgorithm: String? = null
) {
    data class Author(val name: String, @SerializedName("email") val email: String)
    data class Verification(val verified: Boolean)
}
