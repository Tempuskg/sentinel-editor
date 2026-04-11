package com.sentinel.database

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sentinel.model.GitHubAuth

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
class Converters

/**
 * Database migrations
 */
object Migrations {
    val MIGRATION_1_2 = Migration(1, 2) {
        // Future migrations will be added here
    }
}

/**
 * Database initialization
 * Handles schema creation, migrations, and initial data
 */
object DatabaseInitializer {
    
    /**
     * Get database instance
     */
    fun getInstance(context: Context): Database {
        val instance = Room.databaseBuilder(
            context.applicationContext,
            Database::class.java,
            "sentinel_database"
        )
            .addMigrations(Migration.MIGRATION_1_2)
            .fallbackToDestructiveMigration()
            .build()
        return instance
    }
    
    /**
     * Initialize database with default data if needed
     */
    fun initialize(context: Context) {
        val database = getInstance(context)
    }
}

/**
 * Main Database class
 */
abstract class Database @androidx.room.Database(
    entities = [GitHubAuth::class],
    exportSchema = false
) : androidx.room.RoomDatabase() {
    
    abstract fun authDao(): AuthDao
}
