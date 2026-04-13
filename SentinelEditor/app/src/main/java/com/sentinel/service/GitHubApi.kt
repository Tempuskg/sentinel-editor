package com.sentinel.service

import retrofit2.Response
import retrofit2.http.*

/**
 * GitHub API Service Interface
 * Maps GitHub REST API endpoints to Kotlin functions
 */
interface GitHubApiService {
    @GET("user")
    suspend fun getCurrentUser(): Response<CurrentUserInfo>

    @GET("users/{username}")
    suspend fun getUserInfo(@Path("username") username: String): Response<UserResponse>

    @GET("orgs/{org}")
    suspend fun getOrgInfo(@Path("org") org: String): Response<OrganizationResponse>

    @GET("search/repositories")
    suspend fun searchRepositories(
        @Query("q") q: String,
        @Query("per_page") perPage: Int = 30,
        @Query("page") page: Int = 1
    ): Response<SearchReposResponse>

    @GET("user/repos")
    suspend fun getUserRepos(
        @Query("type") type: String = "all",
        @Query("sort") sort: String = "updated",
        @Query("direction") direction: String = "desc",
        @Query("per_page") perPage: Int = 30,
        @Query("page") page: Int = 1
    ): Response<List<RepositoryResponse>>

    @GET("orgs/{org}/repos")
    suspend fun getOrgRepos(
        @Path("org") org: String,
        @Query("type") type: String = "all",
        @Query("sort") sort: String = "updated",
        @Query("direction") direction: String = "desc",
        @Query("per_page") perPage: Int = 30,
        @Query("page") page: Int = 1
    ): Response<List<RepositoryResponse>>

    @GET("repos/{owner}/{repo}/contents/{path}")
    suspend fun getRepoContent(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("path") path: String,
        @Query("ref") ref: String? = null
    ): Response<ContentResponse>

    @GET("repos/{owner}/{repo}/tags")
    suspend fun getRepoTags(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Query("page") page: Int = 1
    ): Response<PageResponse>

    @GET("repos/{owner}/{repo}/branches")
    suspend fun getRepoBranches(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Query("per_page") perPage: Int = 30
    ): Response<PageResponse>
}

// API Response Models
data class CurrentUserInfo(val login: String, val id: Long, val type: String)
data class UserResponse(val name: String?, val email: String?, val htmlUrl: String)
data class OrganizationResponse(val name: String, val htmlUrl: String)
data class SearchReposResponse(val items: List<RepositoryResponse>, val totalCount: Int)
data class PageResponse(val items: List<RepositoryResponse>, val totalSize: Int, val totalPages: Int)
data class ContentResponse(val sha: String, val name: String, val type: String, val encoding: String?, val size: Int)

// Repository response model
data class RepositoryResponse(
    val name: String,
    val fullName: String,
    val htmlUrl: String,
    val description: String?,
    val private: Boolean,
    val openIssuesCount: Int,
    val stargazersCount: Int,
    val forksCount: Int,
    val language: String?
)