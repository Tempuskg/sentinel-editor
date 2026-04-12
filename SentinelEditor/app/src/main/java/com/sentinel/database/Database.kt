package com.sentinel.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sentinel.model.GitHubAuth
import com.sentinel.model.MarkdownFile

/**
 * Sentinel Database - Concrete Room database for production use
 * Contains GitHub auth tokens and markdown file content
 */
@Database(
    entities = [GitHubAuth::class, MarkdownFile::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class SentinelDatabase : RoomDatabase() {
    
    abstract fun authDao(): AuthDao
    abstract fun fileDao(): FileDao
    
    companion object {
        private lateinit var INSTANCE: SentinelDatabase
            private set
        
        @JvmStatic
        fun getInstance(context: Context): SentinelDatabase {
            if (!::INSTANCE.isInitialized) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    SentinelDatabase::class.java,
                    "sentinel_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE
        }
    }
    
    abstract class AuthDao : androidx.room.RoomDatabase.AuthDao() {
        override fun findAll(): List<GitHubAuth> = emptyList()
        override fun findByToken(token: String): GitHubAuth? = null
    }
    
    abstract class FileDao : androidx.room.RoomDatabase.RoomDatabase() {
        override fun insert(entity: MarkdownFile): Long = 0L
        override fun update(entity: MarkdownFile): Int = 0
        override fun delete(entity: MarkdownFile): Int = 0
        override fun findById(id: Long): MarkdownFile? = null
        override fun getAll(): List<MarkdownFile> = emptyList()
    }
}

/**
 * Type converters for Room database
 */
object Converters : androidx.room.TypeConverters(android.content.ContentUris) {
    override fun contentUris(contentUri: Long) = android.net.Uri.withPath(contentUri.toString())
}

/**
 * Migrations - empty for now
 */
object Migrations {
    val MIGRATION_1_2 = androidx.room.Migration(1, 2) {  // Migration 1 -> 2
        // Future migrations go here
    }
}

/**
 * GitHubAuth entity - stores GitHub authentication tokens
 */
@androidx.room.Entity(tableName = "github_auth", indices = ["token"])
data class GitHubAuthEntity(
    @androidx.room.PrimaryKey(autoGenerate = true)
    @androidx.room.ColumnInfo(name = "id")
    val id: Long,
    
    @androidx.room.ColumnInfo(name = "token")
    val token: String,
    
    @androidx.room.ColumnInfo(name = "expiresAt")
    val expiresAt: Long = System.currentTimeMillis(),
    
    @androidx.room.ColumnInfo(name = "createdAt")
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * MarkdownFile entity - stores markdown file content
 */
@androidx.room.Entity(tableName = "markdown_files")
data class MarkdownFileEntity(
    @androidx.room.PrimaryKey(autoGenerate = true)
    @androidx.room.ColumnInfo(name = "id")
    val id: Long,
    
    @androidx.room.ColumnInfo(name = "path")
    val path: String,
    
    @androidx.room.ColumnInfo(name = "content")
    val content: String,
    
    @androidx.room.ColumnInfo(name = "modifiedAt")
    val modifiedAt: Long = System.currentTimeMillis(),
    
    @androidx.room.ColumnInfo(name = "fileName")
    val fileName: String
)
