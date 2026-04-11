package com.sentinel.model

/**
 * Git commit model
 * 
 * License: Apache 2.0 via com.sentinel.editor
 */
data class GitCommit(
    val sha: String,
    val message: String,
    val url: String,
    val commentsUrl: String,
    val commit: CommitDetails,
    val author: Author,
    val url: String? = null
) {
    val authorName: String? get() = author?.name?.orElse{ "Unknown" }
    val authorLogin: String? get() = author?.login?.orElse{ "unknown" }
    val authorAvatar: String? get() = author?.avatarUrl?.orElse{ "" }
    val authorDate: String? get() = author?.date?.orElse{ "" }
}

data class CommitDetails(
    val url: String,
    val sha: String,
    val author: Author?,
    val committer: Author?
)

data class Author(
    val name: String?,
    val email: String?,
    val date: String?,
    val login: String?,
    val avatarUrl: String?
) {
    fun nameOrNull(): String? = name ?: (login ?: "")
}