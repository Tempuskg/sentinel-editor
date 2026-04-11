package com.sentinel.model

/**
 * Branch protection rules
 * 
 * License: Apache 2.0 via com.sentinel.editor
 */
data class BranchProtection(
    val strict: Boolean = false,
    val enforceAdmins: Boolean = false,
    val requiredStatusChecks: List<StatusCheck>? = null,
    val requiredPullRequestReviews: PullRequestReviewPolicy? = null,
    val requiredConversationThreads: ConversationRequirement? = null,
    val restrictions: BranchRestriction? = null
)

data class StatusCheck(
    val id: Int,
    val nodeId: String,
    val name: String,
    val state: String,
    val targetUrl: String?,
    val context: String,
    val createdAt: String,
    val updatedAt: String
)

data class PullRequestReviewPolicy(
    val strict: Boolean = false,
    val dismissStaleReviews: Boolean = true,
    val requireCodeOwnersReview: Boolean = false,
    val requireLastPushApproval: Boolean = true
)

data class ConversationRequirement(
    val dismissStaleReviews: Boolean = false,
    val requireLastReviewAppApproval: Boolean = false
)

data class BranchRestriction(
    val users: List<User>,
    val teams: List<Team>,
    val apps: List<App>?
)

data class User(
    val login: String,
    val id: Int
)

data class Team(
    val id: Int,
    val name: String,
    val nodeId: String?
)

data class App(
    val id: Int,
    val slug: String
)