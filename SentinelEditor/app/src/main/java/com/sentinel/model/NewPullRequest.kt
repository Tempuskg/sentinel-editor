package com.sentinel.model

/**
 * New Pull Request request body
 * 
 * License: Apache 2.0 via com.sentinel.editor
 */
data class NewPullRequest(
    val title: String,
    val body: String? = null,
    val head: String,
    val base: String,
    val draft: Boolean = false
)