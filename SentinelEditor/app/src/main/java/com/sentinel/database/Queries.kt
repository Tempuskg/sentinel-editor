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
) {
}

@Query("UPDATE auth SET accessToken = :accessToken, scope = :scope, expiresAt = :expiresAt WHERE userId = :userId")
suspend fun updateAuth(
    userId: String,
    accessToken: String,
    scope: String,
    expiresAt: LocalDateTime?
) {
}

@Query("DELETE FROM auth WHERE userId = :userId")
suspend fun deleteAuth(userId: String) {
}

@Query("DELETE FROM auth WHERE expiresAt IS NOT NULL AND expiresAt < :now")
suspend fun deleteExpiredAuth(now: LocalDateTime) {
}