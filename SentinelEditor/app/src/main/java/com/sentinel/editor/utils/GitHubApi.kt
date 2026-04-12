package com.sentinel.editor.utils

import com.sentinel.model.PullRequest
import com.sentinel.model.RepositoryContent
import com.sentinel.model.RepositoryInfo
import com.sentinel.model.RepositoryItem

class GitHubApi private constructor(private val token: String) {

    interface GitHubClient {
        suspend fun listRepositories(): List<RepositoryItem>
        suspend fun getRepository(owner: String, repo: String): RepositoryInfo
        suspend fun listRepositoryContents(owner: String, repo: String, path: String = ""): List<RepositoryContent>
        suspend fun getPullRequests(owner: String, repo: String): List<PullRequest>
    }

    companion object {
        private var INSTANCE: GitHubApi? = null

        fun getInstance(token: String): GitHubApi {
            return INSTANCE ?: GitHubApi(token).also { INSTANCE = it }
        }

        fun createForToken(token: String): GitHubApi = GitHubApi(token)
    }
}
