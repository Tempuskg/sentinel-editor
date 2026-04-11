package com.sentinel.database

import androidx.room.*
import com.sentinel.model.GitHubAuth

@Dao
interface AuthDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuth(authTokens: List<GitHubAuth>)
    
    @Update
    suspend fun updateAuth(authTokens: List<GitHubAuth>)
    
    @Delete
    suspend fun deleteAuth(authTokens: List<GitHubAuth>)
    
    @Query("SELECT * FROM auth WHERE userId = :userId LIMIT 1")
    suspend fun getAuthByUserId(userId: String): GitHubAuth?
    
    @Query("SELECT * FROM auth ORDER BY createdAt DESC LIMIT 1")
    suspend fun getLatestAuth(): GitHubAuth?
    
    @Query("SELECT * FROM auth")
    suspend fun getAllAuths(): List<GitHubAuth>
    
    @Query("DELETE FROM auth WHERE expiresAt IS NULL")
    suspend fun deleteExpiredAuths()
    
    @Query("DELETE FROM auth")
    suspend fun deleteAllAuths()
}
