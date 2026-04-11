package com.sentinel.model

import com.google.gson.annotations.SerializedName

/**
 * GitHub API response for commit details
 * 
 * Licensed under Apache 2.0 via com.sentinel.editor
 */
data class GitCommit(
    val sha: String,
    @SerializedName("node_id")
    val nodeId: String?,
    @SerializedName("html_url")
    val htmlUrl: String,
    @SerializedName("comments_url")
    val commentsUrl: String,
    @SerializedName("commit")
    val commit: GitCommitCommit? = null,
    @SerializedName("author")
    val author: GitCommitUser? = null,
    @SerializedName("committer")
    val committer: GitCommitUser? = null,
    val parents: List<GitCommitParent>
) {
    data class GitCommitCommit(
        val author: GitCommitUser?,
        @SerializedName("url")
        val url: String,
        @SerializedName("timestamp")
        val timestamp: String,
        val message: String,
        val distinct: Boolean,
        @SerializedName("tree")
        val tree: GitCommitTree?,
        @SerializedName("parents")
        val parents: List<GitCommitParent>
    ) {
        data class GitCommitParent(
            val sha: String,
            @SerializedName("url")
            val url: String
        )
        
        data class GitCommitTree(
            @SerializedName("url")
            val url: String,
            @SerializedName("sha")
            val sha: String
        )
    }
    
    data class GitCommitUser(
        val name: String,
        @SerializedName("email")
        val email: String,
        @SerializedName("date")
        val date: String,
        val username: String,
        @SerializedName("repository")
        val repository: GitCommitUserRepository? = null,
        @SerializedName("user")
        val user: GithubUser? = null
    ) {
        data class GitCommitUserRepository(
            val name: String,
            @SerializedName("full_name")
            val fullName: String,
            val url: String,
            @SerializedName("owner")
            val owner: GitCommitUserOwner,
            @SerializedName("private")
            val private: Boolean,
            val created_at: String,
            @SerializedName("updated_at")
            val updatedAt: String,
            @SerializedName("http_url")
            val httpUrl: String,
            @SerializedName("ssh_url")
            val sshUrl: String,
            @SerializedName("clone_url")
            val cloneUrl: String,
            val htmlUrl: String,
            @SerializedName("description")
            val description: String?
        ) {
            data class GitCommitUserOwner(
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
        }
        
        data class GithubUser(
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
    }
}
