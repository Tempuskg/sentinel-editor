package com.sentinel.model

import com.google.gson.annotations.SerializedName

/**
 * GitHub API response for file operations.
 */
data class ResponseInfo(
    @SerializedName("name")
    val name: String,
    @SerializedName("path")
    val path: String,
    val commit: ResponseCommit? = null,
    val sha: String,
    @SerializedName("size")
    val size: Int,
    val content: ResponseContentInfo? = null,
    @SerializedName("blob_url")
    val blobUrl: String,
    @SerializedName("git_url")
    val gitUrl: String,
    @SerializedName("html_url")
    val htmlUrl: String,
    @SerializedName("download_url")
    val downloadUrl: String? = null,
    @SerializedName("type")
    val type: String,
    @SerializedName("_links")
    val links: ResponseRepoLinks? = null
)

data class ResponseCommit(
    val url: String,
    @SerializedName("sha")
    val sha: String,
    @SerializedName("html_url")
    val htmlUrl: String,
    val author: ResponseCommitAuthor? = null,
    val committer: ResponseCommitAuthor? = null,
    val message: String,
    val timestamp: String,
    val verification: ResponseCommitVerification? = null,
    @SerializedName("parents")
    val parents: List<ResponseCommitParent> = emptyList()
)

data class ResponseCommitAuthor(
    val name: String,
    @SerializedName("email")
    val email: String,
    val username: String,
    @SerializedName("resource_path")
    val resourcePath: String
)

data class ResponseCommitVerification(
    val verified: Boolean,
    @SerializedName("reason")
    val reason: String?,
    @SerializedName("payload")
    val payload: String?,
    val signature: String?,
    @SerializedName("signature_algorithm")
    val signatureAlgorithm: String?
)

data class ResponseCommitParent(
    val sha: String,
    @SerializedName("url")
    val url: String
)

data class ResponseContentInfo(
    val type: String,
    @SerializedName("encoding")
    val encoding: String?
)

data class ResponseRepoLinks(
    @SerializedName("self")
    val self: String?,
    @SerializedName("git")
    val git: String?,
    @SerializedName("html")
    val html: String?
)
