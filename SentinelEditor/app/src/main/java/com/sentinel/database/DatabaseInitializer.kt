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

/**
 * Database initialization
 * Handles schema creation, migrations, and initial data
 */
object DatabaseInitializer {
    
    /**
     * Get database instance
     */
    fun getInstance(context: Context): SentinelDatabase {
        return SentinelDatabase.getInstance(context)
    }
    
    /**
     * Initialize database with default data if needed
     */
    fun initialize(context: Context) {
        getInstance(context)
    }
}
