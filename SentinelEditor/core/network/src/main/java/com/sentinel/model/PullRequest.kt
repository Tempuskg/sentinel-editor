package com.sentinel.model

import com.google.gson.annotations.SerializedName

/**
 * GitHub API response for pull request list (simplified)
 * 
 * Licensed under Apache 2.0 via com.sentinel.editor
 */
data class PullRequest(
    val url: String,
    @SerializedName("id")
    val id: Long,
    @SerializedName("node_id")
    val nodeId: String,
    val htmlUrl: String,
    @SerializedName("number")
    val number: Int,
    @SerializedName("state")
    val state: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("user")
    val user: PullRequestUser? = null,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("closed_at")
    val closedAt: String?,
    @SerializedName("merged_at")
    val mergedAt: String?,
    @SerializedName("pull_request")
    val pullRequest: PullRequestState? = null,
    val body: String? = null,
    val commentsUrl: String,
    val reviewCommentsUrl: String,
    val reviewCommentUrl: String,
    @SerializedName("comments")
    val commentsCount: Int,
    @SerializedName("review_comments")
    val reviewCommentsCount: Int,
    @SerializedName("review_comments_url")
    val reviewCommentsUrl2: String,
    @SerializedName("issue_comment_url")
    val issueCommentUrl: String,
    @SerializedName("labels")
    val labels: List<PullRequestLabel>? = null,
    val milestone: PullRequestMilestone? = null,
    @SerializedName("commits")
    val commits: Int = 0,
    @SerializedName("additions")
    val additions: Int = 0,
    @SerializedName("deletions")
    val deletions: Int = 0,
    @SerializedName("changed_files")
    val changedFiles: Int = 0,
    val mergedBy: PullRequestUser? = null,
    @SerializedName("head")
    val head: PullRequestRef? = null,
    @SerializedName("base")
    val base: PullRequestRef? = null
) {
    data class PullRequestState(
        val merged: Boolean,
        @SerializedName("merged_by")
        val mergedBy: PullRequestUser?,
        val merge_commit_sha: String?,
        @SerializedName("rebaseable")
        val rebaseable: Boolean?,
        val url: String,
        @SerializedName("html_url")
        val htmlUrl: String,
        @SerializedName("diff_url")
        val diffUrl: String,
        @SerializedName("patch_url")
        val patchUrl: String
    )
    
    data class PullRequestUser(
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
    
    data class PullRequestRef(
        @SerializedName("ref")
        val ref: String,
        @SerializedName("sha")
        val sha: String,
        @SerializedName("user")
        val user: PullRequestUser,
        @SerializedName("repo")
        val repo: PullRequestRepo
    )
    
    data class PullRequestRepo(
        val id: Long,
        @SerializedName("name")
        val name: String,
        @SerializedName("full_name")
        val fullName: String,
        @SerializedName("html_url")
        val htmlUrl: String,
        @SerializedName("description")
        val description: String?,
        val fork: Boolean,
        @SerializedName("private")
        val private: Boolean,
        @SerializedName("owner")
        val owner: GitHubUser,
        val pushedAt: String?
    )
    
    data class PullRequestLabel(
        val id: Long,
        @SerializedName("node_id")
        val nodeId: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("color")
        val color: String,
        @SerializedName("default")
        val default: Boolean,
        @SerializedName("description")
        val description: String?
    )
    
    data class PullRequestMilestone(
        val url: String,
        @SerializedName("html_url")
        val htmlUrl: String,
        @SerializedName("labels_url")
        val labelsUrl: String,
        @SerializedName("id")
        val id: Long,
        @SerializedName("node_id")
        val nodeId: String,
        @SerializedName("number")
        val number: Int,
        @SerializedName("state")
        val state: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("description")
        val description: String?,
        @SerializedName("closed_at")
        val closedAt: String?,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("creator")
        val creator: PullRequestUser?,
        val closedBy: PullRequestUser?
    )
}
