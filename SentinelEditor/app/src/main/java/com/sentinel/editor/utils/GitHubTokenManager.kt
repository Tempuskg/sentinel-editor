package com.sentinel.editor.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sentinel.model.GitHubAuth
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "github_tokens")

object GitHubTokenManager {

    fun getCurrentToken(context: Context): GitHubAuth? {
        return runBlocking {
            val prefs = context.dataStore.data
            null // TODO: deserialise from prefs
        }
    }

    suspend fun storeToken(context: Context, auth: GitHubAuth) {
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey("token")] = auth.accessToken
            preferences[booleanPreferencesKey("isLoggedIn")] = true
            preferences[stringPreferencesKey("userId")] = auth.userId
            preferences[stringPreferencesKey("createdAt")] = auth.createdAt.toString()
            preferences[stringPreferencesKey("expiresAt")] = (auth.expiresAt ?: 0L).toString()
        }
    }

    suspend fun logout(context: Context) {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    fun isTokenExpired(token: GitHubAuth?): Boolean {
        return token?.expiresAt?.let { it < System.currentTimeMillis() } ?: false
    }

    fun isTokenAboutToExpire(token: GitHubAuth?, minutes: Int = 5): Boolean {
        return token?.expiresAt?.let { expiresAt ->
            val threshold = TimeUnit.MINUTES.toMillis(minutes.toLong())
            expiresAt < (System.currentTimeMillis() + threshold)
        } ?: false
    }

    fun getRefreshExpireTimeRemaining(token: GitHubAuth?): Long {
        return token?.expiresAt?.let { (it - System.currentTimeMillis()).coerceAtLeast(0L) }
            ?: Long.MAX_VALUE
    }

    fun updateRateLimiterFromToken(context: Context, rateLimiter: RateLimiter?) {
        // No-op stub
    }
}
