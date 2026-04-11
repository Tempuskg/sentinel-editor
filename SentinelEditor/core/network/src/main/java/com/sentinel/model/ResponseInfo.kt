package com.sentinel.model

import com.google.gson.annotations.SerializedName

/**
 * GitHub API response for file operations
 * 
 * Licensed under Apache 2.0 via com.sentinel.editor
 */
data class ResponseInfo(
    @SerializedName("name")
    val name: String,
    @SerializedName("path")
    val path: String,
    val commit: GitCommitResponse?,
    val sha: String,
    @SerializedName("size")
    val size: Int,
    val content: ContentInfo?,
    @SerializedName("blob_url")
    val blobUrl: String,
    @SerializedName("git_url")
    val gitUrl: String,
    @SerializedName("html_url")
    val htmlUrl: String,
    val downloadUrl: String? = null,
    @SerializedName("type")
    val type: String,
    @SerializedName("_links")
    val links: RepoLinks? = null
) {
    data class GitCommit(
        val url: String,
        @SerializedName("sha")
        val sha: String,
        @SerializedName("html_url")
        val htmlUrl: String,
        val author: GitCommitResponseAuthor? = null,
        val committer: GitCommitResponseAuthor? = null,
        val message: String,
        val timestamp: String,
        val verification: GitCommitResponseVerification? = null,
        @SerializedName("parents")
        val parents: List<GitCommitCommitResponse> = emptyList()
    ) {
        @get:SerializedName("author")
        val author: GitCommitResponseAuthor?
            get() = commit?.author

        @get:SerializedName("committer")
        val committer: GitCommitResponseCommitter?
            get() = commit?.committer

        @get:SerializedName("verification")
        val verification: GitCommitResponseVerification?
            get() = commit?.verification

        @get:SerializedName("parents")
        val parents: List<GitCommitCommitResponse>
            get() = commit?.parents ?: emptyList()
    }

    data class GitCommitResponseAuthor(
        val name: String,
        @SerializedName("email")
        val email: String,
        val username: String,
        @SerializedName("resource_path")
        val resourcePath: String
    )

    data class GitCommitResponseCommitter(
        val name: String,
        @SerializedName("email")
        val email: String,
        val username: String,
        @SerializedName("resource_path")
        val resourcePath: String
    )

    data class GitCommitResponseVerification(
        val verified: Boolean,
        @SerializedName("reason")
        val reason: String?,
        @SerializedName("payload")
        val payload: String?,
        val signature: String?,
        @SerializedName("signature_algorithm")
        val signatureAlgorithm: String?
    )

    @Serializable
    data class ContentInfo(
        val type: String,
        @SerializedName("encoding")
        val encoding: String?
    )

    @Serializable
    data class RepoLinks(
        @SerializedName("self")
        val self: String?,
        @SerializedName("git")
        val git: String?,
        @SerializedName("html")
        val html: String?
    )
}
