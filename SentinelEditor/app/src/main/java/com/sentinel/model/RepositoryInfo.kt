package com.sentinel.model

/**
 * Repository info model for GitHub API
 * 
 * License: Apache 2.0 via com.sentinel.editor
 */
data class RepositoryInfo(
    val name: String = "",
    val full_name: String = "",
    val private: Boolean = false,
    val html_url: String = "",
    val description: String? = null,
    val fork: Boolean = false,
    val url: String = "",
    val forks: Int = 0,
    val open_issues_count: Int = 0,
    val stargazers_count: Int = 0,
    val language: String? = null,
    val default_branch: String = "",
    val watchers_count: Int = 0,
    val size: Int = 0,
    val updated_at: String = ""
)