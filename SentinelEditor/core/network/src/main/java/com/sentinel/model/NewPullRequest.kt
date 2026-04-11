package com.sentinel.model

import com.google.gson.annotations.SerializedName

/**
 * Request body for creating a pull request
 * 
 * Licensed under Apache 2.0 via com.sentinel.editor
 */
data class NewPullRequest(
    @SerializedName("title")
    val title: String,
    @SerializedName("body")
    val body: String,
    @SerializedName("head")
    val head: String,
    @SerializedName("base")
    val base: BaseRef,
    @SerializedName("maintainer_can_modify")
    val maintainerCanModify: Boolean? = null,
    @SerializedName("draft")
    val isDraft: Boolean? = null,
    @SerializedName("assignee")
    val assignee: Assignee? = null,
    @SerializedName("assignees")
    val assignees: List<String>? = null,
    @SerializedName("requested_reviewers")
    val requestedReviewers: List<Assignee>? = null,
    @SerializedName("milestone")
    val milestone: Milestone? = null,
    @SerializedName("labels")
    val labels: List<String>? = null
) {
    data class BaseRef(
        val label: String,
        @SerializedName("ref")
        val ref: String
    )
    
    data class Assignee(
        val user: GithubUser,
        @SerializedName("organization")
        val organization: String
    )
    
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
    
    data class Milestone(
        val number: Int,
        @SerializedName("title")
        val title: String
    )
}
