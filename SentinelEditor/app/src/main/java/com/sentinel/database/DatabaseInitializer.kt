<<<<<<< HEAD
=======
package com.sentinel.database

import android.content.Context
import androidx.room.migration.Migration

/**
 * Room database version constants
 */
enum class Constants(val DATABASE_VERSION: Int) {
    V1(1),
    V2(2)
}

/**
 * Database converters
 */
object Migrations {
    val MIGRATION_1_2 = Migration(1, 2) {
        // Future migrations will be added here
    }
}

>>>>>>> 1012f7f7b6f433d99bda325c30b20ebfda82d363
/**
 * Database initialization
 * Handles schema creation, migrations, and initial data
 * NOTE: Removed abstract Database class - Room requires concrete entities
 */
object DatabaseInitializer {
    
    /**
     * Sentinel database - Concrete Room Database
     * This is what Room can actually instantiate
     */
<<<<<<< HEAD
    @androidx.room.Database(
        entities = [GitHubAuth::class, MarkdownFile::class],
        version = 1,
        exportSchema = false
    )
    @androidx.room.TypeConverters(Converters.TypeConverters)
    open class SentinelDatabase : androidx.room.RoomDatabase() {
        
        @androidx.room.Dao
        open interface AuthDao {
            @androidx.room.Query("SELECT * FROM github_auth")
            fun findAll(): Flow<List<GitHubAuth>>
            
            @androidx.room.Query("SELECT * FROM github_auth WHERE token = :token")
            fun findByToken(token: String): Flow<GitHubAuth?>
        }
        
        @androidx.room.Dao
        open interface FileDao {
            @androidx.room.Insert
            fun insertFile(file: MarkdownFile): Long
            
            @androidx.room.Update
            fun updateFile(file: MarkdownFile): Int
            
            @androidx.room.Delete
            fun deleteFile(id: Long): Int
            
            @androidx.room.Query("SELECT * FROM markdown_files")
            fun getAllFiles(): Flow<List<MarkdownFile>>
            
            @androidx.room.Query("SELECT * FROM markdown_files WHERE id = :id")
            fun findFileById(id: Long): Flow<MarkdownFile?>
        }
    }
    
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
                ).fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE
        }
=======
    fun getInstance(context: Context): SentinelDatabase {
        return SentinelDatabase.getInstance(context)
    }
    
    /**
     * Initialize database with default data if needed
     */
    fun initialize(context: Context) {
        getInstance(context)
>>>>>>> 1012f7f7b6f433d99bda325c30b20ebfda82d363
    }
}
