package com.sentinel.editor.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sentinel.model.GitHubAuth
import com.sentinel.database.Database
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit

/**
 * GitHub Token Manager
 * Handles token storage, validation, refresh, and token expiration
 * 
 * License: Apache 2.0 via com.sentinel.editor
 */
object GitHubTokenManager {
    
    private val Context.dataStore: DataStore<Preferences> by Preferences.dataStore(
        fileName = "github_tokens.pb",
        context = this
    )
    
    /**
     * Get current GitHub token
     */
    fun getCurrentToken(context: Context): GitHubAuth? {
        return runBlocking {
            Database.getInstance(context).authDao().getLatestAuth()
        }
    }
    
    /**
     * Store new token
     */
    suspend fun storeToken(context: Context, auth: GitHubAuth) {
        runBlocking {
            context.dataStore.edit { preferences ->
                preferences[stringPreferencesKey("token")] = auth.accessToken
                preferences[booleanPreferencesKey("isLoggedIn")] = true
                preferences[stringPreferencesKey("userId")] = auth.userId
                preferences[stringPreferencesKey("createdAt")] = auth.createdAt.toString()
                preferences[stringPreferencesKey("expiresAt")] = (auth.expiresAt?:0).toString()
            }
        }
    }
    
    /**
     * Remove current token
     */
    suspend fun logout(context: Context) {
        runBlocking {
            context.dataStore.edit { preferences ->
                preferences.clear()
            }
        }
    }
    
    /**
     * Check if token is expired
     */
    fun isTokenExpired(token: GitHubAuth?): Boolean {
        return token?.expiresAt?.let { tokenExpiresAt ->
            val now = System.currentTimeMillis()
            return tokenExpiresAt < now
        } ?: false
    }
    
    /**
     * Check if token is about to expire (within 5 minutes)
     */
    fun isTokenAboutToExpire(token: GitHubAuth?): Boolean {
        return isTokenAboutToExpire(token, minutes = 5)
    }
    
    /**
     * Check if token is about to expire (custom minutes)
     * 
     * @param token GitHub token to check
     * @param minutes Minutes until expiration threshold
     * @return true if token expires within specified minutes
     */
    @JvmOverloads
    fun isTokenAboutToExpire(token: GitHubAuth?, minutes: Int = 5): Boolean {
        return token?.expiresAt?.let { tokenExpiresAt ->
            val now = System.currentTimeMillis()
            val threshold = TimeUnit.MINUTES.toMillis(minutes)
            return tokenExpiresAt < (now + threshold)
        } ?: false
    }
    
    /**
     * Refresh/expire time remaining in milliseconds
     */
    fun getRefreshExpireTimeRemaining(token: GitHubAuth?): Long {
        return token?.expiresAt?.let { tokenExpiresAt ->
            (tokenExpiresAt - System.currentTimeMillis()).coerceAtLeast(0)
        } ?: Long.MAX_VALUE
    }
    
    /**
     * Refresh token (call refresh endpoint if needed)
     */
    @Deprecated("No refresh endpoint for PAT tokens")
    fun refreshToken(context: Context, userId: String, refreshToken: String?) {
        // Currently no refresh endpoint for PAT tokens
        // OAuth 2.0 with refresh tokens would require this logic
        // For now, we just check expiration and log out if expired
    }
    
    /**
     * Update rate limiter based on token status
     */
    fun updateRateLimiterFromToken(context: Context, rateLimiter: RateLimiter?) {
        getCurrentToken(context)?.let { token ->
            if (!isTokenExpired(token)) {
                // Token is valid, get remaining requests from headers
                // In a real implementation, this would parse response headers
                rateLimiter?.update(500, System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(20))
            }
        }
    }
}
