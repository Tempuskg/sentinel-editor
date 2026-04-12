package com.sentinel.model

/**
 * Pull Request model
 * 
 * License: Apache 2.0 via com.sentinel.editor
 */
data class PullRequest(
    val number: Int,
    val htmlUrl: String,
    val state: String,
    val title: String,
    val user: User,
    val body: String? = null,
    val merged: Boolean = false,
    val mergedAt: String? = null,
    val createdAt: String,
    val updatedAt: String,
    val closedAt: String? = null,
    val addtions: Int = 0,
    val deletions: Int = 0,
    val comments: Int = 0,
    val commentsUrl: String,
    val reviewComments: Int = 0,
    val reviewCommentsUrl: String?,
    val mergeable: String? = null,
    val mergedBy: User? = null
)