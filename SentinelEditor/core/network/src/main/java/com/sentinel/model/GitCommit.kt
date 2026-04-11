package com.sentinel.model

import com.google.gson.annotations.SerializedName

/**
 * GitHub API response for commit details.
 */
data class GitCommit(
    val sha: String,
    @SerializedName("node_id")
    val nodeId: String? = null,
    @SerializedName("html_url")
    val htmlUrl: String? = null,
    @SerializedName("comments_url")
    val commentsUrl: String? = null,
    val commit: GitCommitDetails? = null,
    val author: GitCommitUser? = null,
    val committer: GitCommitUser? = null,
    val parents: List<GitCommitParent> = emptyList(),
    val message: String? = null,
    val timestamp: String? = null,
    val url: String? = null
)

data class GitCommitDetails(
    val author: GitCommitIdentity? = null,
    val committer: GitCommitIdentity? = null,
    val message: String? = null,
    val tree: GitCommitTree? = null,
    val url: String? = null
)

data class GitCommitIdentity(
    val name: String? = null,
    val email: String? = null,
    val date: String? = null
)

data class GitCommitParent(
    val sha: String,
    @SerializedName("url")
    val url: String
)

data class GitCommitTree(
    @SerializedName("sha")
    val sha: String,
    @SerializedName("url")
    val url: String
)

data class GitCommitUser(
    val login: String? = null,
    @SerializedName("id")
    val id: Long? = null,
    @SerializedName("avatar_url")
    val avatarUrl: String? = null,
    @SerializedName("url")
    val url: String? = null,
    @SerializedName("html_url")
    val htmlUrl: String? = null,
    @SerializedName("type")
    val type: String? = null
)
