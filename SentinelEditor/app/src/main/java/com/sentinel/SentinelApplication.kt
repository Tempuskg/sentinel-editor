package com.sentinel

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import com.sentinel.editor.utils.GitHubClient
import com.sentinel.editor.utils.RateLimiter
import com.sentinel.editor.utils.GitHubAuthenticator
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

/**
 * SentinelApplication - Main Application Class
 * 
 * Responsibilities:
 * - Initialize GitHub API client with authentication
 * - Setup DataStore for user preferences
 * - Handle rate limiting and authentication cache
 * - Register background services
 * - Setup dependency injection
 * 
 * License: Apache 2.0 via com.sentinel.editor
 */
class SentinelApplication : Application() {
    
    companion object {
        const val APP_TAG = "SentinelApp"
        val DATA_STORE: DataStore<Preferences> = ContextWrapper(applicationContext)
            .applicationContext
            .dataStoreFile("sentinel_prefs")
            .preferencesDataStore()
    }
    
    /**
     * GitHub API client singleton
     */
    lateinit var githubClient: GitHubClient
    
    /**
     * Rate limiter for API requests
     */
    lateinit var rateLimiter: RateLimiter
    
    /**
     * GitHub authenticator for token management
     */
    lateinit var authenticator: GitHubAuthenticator
    
    override fun onCreate() {
        super.onCreate()
        
        setupGitHubClient()
        setupRateLimiter()
        setupAuthenticator()
        setupDatabase()
        
        Log.i(APP_TAG, "SentinelApplication initialized")
    }
    
    /**
     * Setup GitHub API client
     */
    private fun setupGitHubClient() {
        val baseUrl = getString(R.string.github_api_base_url)
        val userAgents = setOf("SentinelEditor/1.0")
        
        githubClient = GitHubClient.Builder(this)
            .baseUrl(baseUrl)
            .userAgents(userAgents)
            .build()
    }
    
    /**
     * Setup rate limiter
     */
    private fun setupRateLimiter() {
        val remaining = prefsInt("rate_limit_remaining") ?: 50
        val reset = prefsLong("rate_limit_reset_epoch")?.let { it + (20 * 60) } ?: System.currentTimeMillis()
        
        rateLimiter = RateLimiter(remaining, reset)
    }
    
    /**
     * Setup authenticator (OAuth token management)
     */
    private fun setupAuthenticator() {
        val token = prefsString("github_token") ?: ""
        
        authenticator = GitHubAuthenticator.Builder(this)
            .token(token)
            .scopes(getAuthScopes())
            .build()
    }
    
    /**
     * Get authentication scopes from DataStore
     */
    private fun getAuthScopes(): List<String> {
        return listOf("repo", "read:org", "user")
    }
    
    /**
     * Setup Room database
     */
    private fun setupDatabase() {
        // TODO: Initialize Room database
    }
    
    /**
     * Update rate limit tracking when new headers received
     */
    fun updateRateLimit(remaining: Int, reset: Long) {
        GlobalScope.launch {
            context = applicationContext
            DataStore<Preferences>.run {
                edit {
                    persistIn("rate_limit_remaining", remaining)
                    persistIn("rate_limit_reset_epoch", reset)
                }
            }
        }
    }
    
    /**
     * Get authenticated token
     */
    fun getAccessToken(): String? {
        return authenticator.token
    }
    
    /**
     * Check if rate limited
     */
    fun isRateLimited(): Boolean {
        return rateLimiter.isRateLimited()
    }
    
    /**
     * Clear rate limit cache
     */
    fun clearRateLimit() {
        rateLimiter.reset()
    }
    
    /**
     * Get application package name
     */
    fun getPackageName(): String {
        return packageName
    }
    
    /**
     * Check if version is stable (not -SNAPSHOT)
     */
    fun isVersionStable(): Boolean {
        return BuildConfig.VERSION_NAME?.endsWith("-SNAPSHOT")?.not() == true
    }
}