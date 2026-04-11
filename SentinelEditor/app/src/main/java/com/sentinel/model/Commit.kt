package com.sentinel.model

/**
 * Git commit details
 * 
 * License: Apache 2.0 via com.sentinel.editor
 */
data class CommitDetails(
    val message: String,
    val url: String,
    val hash: String,
    val shortHash: String,
    val author: GitAuthor
) {
    /**
     * Get abbreviated commit hash
     */
    val shortHash7: String get() = hash.take(7)
    
    /**
     * Get abbreviated commit hash (8 chars)
     */
    val shortHash8: String get() = hash.take(8)
}

/**
 * Commit author
 */
data class GitAuthor(
    val name: String = "Unknown",
    val email: String = "",
    val date: String = ""
)