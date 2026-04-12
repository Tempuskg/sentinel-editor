package com.sentinel.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * GitHub auth credentials stored securely
 * Note: In production, use Keystore/KeyStore integration
 * For this project, simple encrypted storage with device lock
 */
@Entity(tableName = "auth")
data class GitHubAuth(
    @PrimaryKey
    val userId: String, // Unique identifier for auth session
    val accessToken: String, // Bearer token
    val scope: String = "repo,read:org",
    val createdAt: Long = System.currentTimeMillis(),
    val expiresAt: Long? = null // null = permanent, otherwise timestamp
)
