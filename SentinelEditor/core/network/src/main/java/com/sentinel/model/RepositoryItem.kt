package com.sentinel.model

import com.google.gson.annotations.SerializedName

/**
 * GitHub API response for repository list
 * 
 * Licensed under Apache 2.0 via com.sentinel.editor
 */
data class RepositoryItem(
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("html_url")
    val htmlUrl: String,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("fork")
    val fork: Boolean = false,
    @SerializedName("private")
    val private: Boolean = false,
    @SerializedName("owner")
    val owner: GitHubUser? = null,
    @SerializedName("forks_count")
    val forksCount: Int = 0,
    @SerializedName("stargazers_count")
    val stargazersCount: Int = 0,
    @SerializedName("language")
    val language: String? = null,
    val defaultBranch: String? = null,
    val pushedAt: String? = null,
    val updatedAt: String
)

/**
 * GitHub API response for repository owner/user
 * 
 * Licensed under Apache 2.0 via com.sentinel.editor
 */
data class GitHubUser(
    val login: String,
    @SerializedName("id")
    val id: Long,
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("html_url")
    val htmlUrl: String,
    @SerializedName("type")
    val type: String
)
