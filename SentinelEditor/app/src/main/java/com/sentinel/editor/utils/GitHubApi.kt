package com.sentinel.editor.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.sentinel.model.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.JavaScriptInterceptors
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * GitHub API client with OkHttp + Retrofit
 * 
 * Licensed under Apache 2.0 via com.sentinel.editor
 */
class GitHubApi private constructor(
    private val token: String
) {
    interface GitHubClient {
        @get:org.openapi.kotlin.annotations.Path("/user/repos")
        suspend fun listRepositories(): List<RepositoryItem>

        @org.openapi.kotlin.annotations.Get("/repos/{owner}/{repo}")
        suspend fun getRepository(owner: String, repo: String): RepositoryInfo

        @org.openapi.kotlin.annotations.Get("/repos/{owner}/{repo}/contents/{path}")
        suspend fun listRepositoryContents(
            owner: String,
            repo: String,
            path: String = "",
            recursive: Boolean = true
        ): List<RepositoryContent>

        @org.openapi.kotlin.annotations.Get("/repos/{owner}/{repo}/contents/{path}")
        suspend fun getFileContent(
            owner: String,
            repo: String,
            path: String
        ): FileContent

        @org.openapi.kotlin.annotations.Post("/repos/{owner}/{repo}/contents/{path}")
        suspend fun readFile(
            owner: String,
            repo: String,
            path: String,
            content: String
        ): ResponseInfo

        @org.openapi.kotlin.annotations.Post("/repos/{owner}/{repo}/contents/{path}")
        suspend fun createFile(
            owner: String,
            repo: String,
            filename: String,
            branch: String = "main",
            message: String? = null
        ): ResponseInfo

        @org.openapi.kotlin.annotations.Put("/repos/{owner}/{repo}/contents/{path}")
        suspend fun updateFile(
            owner: String,
            repo: String,
            path: String,
            content: String,
            branch: String = "main",
            message: String? = null
        ): ResponseInfo

        @org.openapi.kotlin.annotations.Delete("/repos/{owner}/{repo}/contents/{path}")
        suspend fun deleteFile(
            owner: String,
            repo: String,
            path: String,
            branch: String = "main",
            message: String? = null
        ): ResponseInfo

        @org.openapi.kotlin.annotations.Get("/repos/{owner}/{repo}/pulls")
        suspend fun listPullRequests(
            owner: String,
            repo: String,
            state: String = "open"
        ): List<PullRequest>

        @org.openapi.kotlin.annotations.Get("/repos/{owner}/{repo}/pulls/{number}")
        suspend fun getPullRequest(
            owner: String,
            repo: String,
            number: Int
        ): PullRequest

        @org.openapi.kotlin.annotations.Post("/repos/{owner}/{repo}/pulls")
        suspend fun createPullRequest(request: NewPullRequest): PullRequest

        @org.openapi.kotlin.annotations.Get("/repos/{owner}/{repo}/commits/{sha}")
        suspend fun getCommit(
            owner: String,
            repo: String,
            sha: String
        ): GitCommit

        @org.openapi.kotlin.annotations.Get("/repos/{owner}/{repo}/commits")
        suspend fun listCommits(
            owner: String,
            repo: String,
            perPage: Int = 30
        ): List<GitCommit>
    }

    companion object {
        @Volatile
        private var INSTANCE: GitHubApi? = null

        @Deprecated("Use createForToken instead", ReplaceWith = "createForToken(token)")
        fun getInstance(token: String): GitHubApi {
            INSTANCE = GitHubApi(token)
            return INSTANCE!!
        }

        fun createForToken(token: String): GitHubApi {
            val client = createOkHttpClient()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

            val clientImpl = retrofit.create(GitHubClient::class.java)
            return GitHubApi(token)
        }

        private val gson = GsonBuilder()
            .setLenient()
            .disableHtmlEscaping()
            .serializeNulls()
            .create()

        private fun createOkHttpClient(): OkHttpClient {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    var originalRequest = chain.request()
                    originalRequest = originalRequest.newBuilder()
                        .header("Accept", "application/vnd.github.v3+json")
                        .header("User-Agent", "SentinelEditor")
                        .build()
                    chain.proceed(originalRequest)
                }
                .connectTimeout(50, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()

            return client
        }
    }
}
