package com.sentinel.database

import androidx.room.*
import java.time.LocalDateTime

@Insert(onConflict = OnConflictStrategy.REPLACE)
@Query("INSERT OR REPLACE INTO auth (userId, accessToken, scope, createdAt, expiresAt) VALUES (:userId, :accessToken, :scope, :createdAt, :expiresAt)")
suspend fun insertAuth(
    userId: String,
    accessToken: String,
    scope: String,
    createdAt: LocalDateTime = LocalDateTime.now(),
    expiresAt: LocalDateTime? = null
)

<query("UPDATE auth SET accessToken = :accessToken, scope = :scope, expiresAt = :expiresAt WHERE userId = :userId")
@Query("DELETE FROM auth WHERE userId = :userId")
@Query("DELETE FROM auth WHERE expiresAt IS NOT NULL AND expiresAt < :now")