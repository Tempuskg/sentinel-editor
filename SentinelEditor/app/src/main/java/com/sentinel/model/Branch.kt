package com.sentinel.model

/**
 * Branch model from GitHub API
 * 
 * License: Apache 2.0 via com.sentinel.editor
 */
data class Branch(
    val name: String = "",
    val commit: GitCommit,
    val protected: Boolean = false,
    val protection: BranchProtection? = null
)