package com.sentinel.model

/**
 * Branch reference from GitHub API
 * 
 * License: Apache 2.0 via com.sentinel.editor
 */
data class BranchRef(
    val name: String = "",
    val commit: GitCommit
)