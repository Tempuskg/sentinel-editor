package com.sentinel.editor.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.sentinel.model.GitHubAuth
import kotlinx.coroutines.flow.first
import java.io.IOException
import java.util.concurrent.TimeUnit

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "github_tokens")

private val tokenKey = stringPreferencesKey("token")
private val isLoggedInKey = booleanPreferencesKey("isLoggedIn")
private val userIdKey = stringPreferencesKey("userId")
private val scopeKey = stringPreferencesKey("scope")
private val createdAtKey = stringPreferencesKey("createdAt")
private val expiresAtKey = stringPreferencesKey("expiresAt")
private const val encryptedPrefsName = "github_tokens_encrypted"

object GitHubTokenManager {

    suspend fun getCurrentToken(context: Context): GitHubAuth? {
        val encryptedPreferences = encryptedPreferences(context)
        migrateLegacyTokenIfNeeded(context, encryptedPreferences)

        if (!encryptedPreferences.getBoolean(isLoggedInKey.name, false)) {
            return null
        }

        val accessToken = encryptedPreferences.getString(tokenKey.name, null).orEmpty()
        val userId = encryptedPreferences.getString(userIdKey.name, null).orEmpty()
        if (accessToken.isBlank() || userId.isBlank()) {
            return null
        }

        val createdAt = encryptedPreferences
            .getString(createdAtKey.name, null)
            ?.toLongOrNull()
            ?: System.currentTimeMillis()
        val expiresAt = encryptedPreferences
            .getString(expiresAtKey.name, null)
            ?.toLongOrNull()
            ?.takeIf { it > 0L }
        val auth = GitHubAuth(
            userId = userId,
            accessToken = accessToken,
            scope = encryptedPreferences.getString(scopeKey.name, null) ?: "repo,read:org",
            createdAt = createdAt,
            expiresAt = expiresAt
        )

        return auth.takeUnless(::isTokenExpired)
    }

    suspend fun storeToken(context: Context, auth: GitHubAuth) {
        encryptedPreferences(context).edit()
            .putString(tokenKey.name, auth.accessToken)
            .putBoolean(isLoggedInKey.name, true)
            .putString(userIdKey.name, auth.userId)
            .putString(scopeKey.name, auth.scope)
            .putString(createdAtKey.name, auth.createdAt.toString())
            .putString(expiresAtKey.name, (auth.expiresAt ?: 0L).toString())
            .apply()

        clearLegacyStore(context)
    }

    suspend fun logout(context: Context) {
        encryptedPreferences(context).edit().clear().apply()
        clearLegacyStore(context)
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

    private fun encryptedPreferences(context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            encryptedPrefsName,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private suspend fun migrateLegacyTokenIfNeeded(
        context: Context,
        encryptedPreferences: SharedPreferences
    ) {
        if (encryptedPreferences.contains(tokenKey.name)) {
            return
        }

        val prefs = try {
            context.dataStore.data.first()
        } catch (_: IOException) {
            emptyPreferences()
        }

        if (prefs[isLoggedInKey] != true) {
            return
        }

        val accessToken = prefs[tokenKey].orEmpty()
        val userId = prefs[userIdKey].orEmpty()
        if (accessToken.isBlank() || userId.isBlank()) {
            clearLegacyStore(context)
            return
        }

        encryptedPreferences.edit()
            .putString(tokenKey.name, accessToken)
            .putBoolean(isLoggedInKey.name, true)
            .putString(userIdKey.name, userId)
            .putString(scopeKey.name, prefs[scopeKey] ?: "repo,read:org")
            .putString(createdAtKey.name, prefs[createdAtKey] ?: System.currentTimeMillis().toString())
            .putString(expiresAtKey.name, prefs[expiresAtKey] ?: "0")
            .apply()

        clearLegacyStore(context)
    }

    private suspend fun clearLegacyStore(context: Context) {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
