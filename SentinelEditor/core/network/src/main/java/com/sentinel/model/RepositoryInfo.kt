package com.sentinel.model

import com.google.gson.annotations.SerializedName

/**
 * GitHub API response for single repository details
 * 
 * Licensed under Apache 2.0 via com.sentinel.editor
 */
data class RepositoryInfo(
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
    @SerializedName("forks_count")
    val forksCount: Int = 0,
    @SerializedName("stargazers_count")
    val stargazersCount: Int = 0,
    @SerializedName("language")
    val language: String? = null,
    val sshUrl: String,
    val cloneUrl: String,
    val gitUrl: String,
    @SerializedName("default_branch")
    val defaultBranch: String?,
    val pushedAt: String?,
    val updatedAt: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("http_url")
    val httpUrl: String,
    val svnUrl: String,
    @SerializedName("submodule_git_url")
    val submoduleGitUrl: String?,
    val mirrorUrl: String?,
    val archiveUrl: String,
    @SerializedName("assignees_url")
    val assigneesUrl: String,
    @SerializedName("blobs_url")
    val blobsUrl: String,
    @SerializedName("branches_url")
    val branchesUrl: String,
    @SerializedName("collaborators_url")
    val collaboratorsUrl: String,
    @SerializedName("comments_url")
    val commentsUrl: String,
    @SerializedName("commits_url")
    val commitsUrl: String,
    @SerializedName("compare_url")
    val compareUrl: String,
    @SerializedName("contents_url")
    val contentsUrl: String,
    @SerializedName("contributors_url")
    val contributorsUrl: String,
    @SerializedName("events_url")
    val eventsUrl: String,
    @SerializedName("forks_url")
    val forksUrl: String,
    @SerializedName("git_commits_url")
    val gitCommitUrl: String,
    @SerializedName("git_refs_url")
    val gitRefsUrl: String,
    @SerializedName("git_tags_url")
    val gitTagsUrl: String,
    @SerializedName("hooks_url")
    val hooksUrl: String,
    @SerializedName("issue_comment_url")
    val issueCommentUrl: String,
    @SerializedName("issue_events_url")
    val issueEventsUrl: String,
    @SerializedName("issues_url")
    val issuesUrl: String,
    @SerializedName("keys_url")
    val keysUrl: String,
    @SerializedName("labels_url")
    val labelsUrl: String,
    @SerializedName("languages_url")
    val languagesUrl: String,
    @SerializedName("merges_url")
    val mergesUrl: String,
    @SerializedName("milestones_url")
    val milestonesUrl: String,
    @SerializedName("notifications_url")
    val notificationsUrl: String,
    @SerializedName("open_issues_count")
    val openIssuesCount: Int,
    @SerializedName("owner")
    val owner: GitHubUser? = null,
    @SerializedName("pulls_url")
    val pullsUrl: String,
    @SerializedName("releases_url")
    val releasesUrl: String,
    val shieldsUrl: String,
    @SerializedName("size")
    val size: Int,
    val source: RepositoryInfo?,
    val dependenciesUrl: String?,
    val dependencyGraphUrl: String?,
    val stargazersUrl: String,
    @SerializedName("subscribers_url")
    val subscribersUrl: String,
    @SerializedName("subscribers_count")
    val subscribersCount: Int,
    @SerializedName("svn_url")
    val svn: String,
    val templatesUrl: String,
    @SerializedName("timestamp")
    val timestamp: String,
    val watchers: RepositoryInfo? = null,
    @SerializedName("watchers_count")
    val watchersCount: Int
)
