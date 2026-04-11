package com.sentinel.model

import retrofit2.Response
import retrofit2.http.*

/**
 * GitHub API client interface
 * 
 * Licensed under Apache 2.0 via com.sentinel.editor
 */
interface GitHubApi {
    
    @GET("user")
    suspend fun getUser(): Response<User>
    
    @GET("user/repos")
    suspend fun getRepositories(): Response<List<RepositoryInfo>>
    
    @GET("repos/{owner}/{repo}/contents/{path}")
    suspend fun getRepoContents(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("path") path: String = ""
    ): Response<RepositoryContent>
    
    @GET("repos/{owner}/{repo}/git/ref/{ref}")
    suspend fun getBranchRef(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("ref") ref: String
    ): Response<BranchRef>
    
    @POST("repos/{owner}/{repo}/git/commits")
    suspend fun createCommit(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        message: String,
        treeSha: String,
        parents: List<String>
    ): Response<GitCommit>
    
    @GET("repos/{owner}/{repo}/branches/{branch}")
    suspend fun getBranch(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("branch") branch: String
    ): Response<Branch>
    
    @GET("repos/{owner}/{repo}/pulls/{pull_number}")
    suspend fun getPullRequest(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("pull_number") pullNumber: Int
    ): Response<PullRequest>
    
    @Get("repos/{owner}/{repo}/pulls")
    suspend fun getPullRequests(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Response<List<PullRequest>>
}