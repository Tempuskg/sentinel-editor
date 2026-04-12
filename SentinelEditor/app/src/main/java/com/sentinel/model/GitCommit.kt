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
    val author: Author
) {
    val authorName: String? get() = author.name ?: "Unknown"
    val authorLogin: String? get() = author.login ?: "unknown"
    val authorAvatar: String? get() = author.avatarUrl ?: ""
    val authorDate: String? get() = author.date ?: ""
}

data class Author(
    val name: String?,
    val email: String?,
    val date: String?,
    val login: String?,
    val avatarUrl: String?
) {
    fun nameOrNull(): String? = name ?: (login ?: "")
}