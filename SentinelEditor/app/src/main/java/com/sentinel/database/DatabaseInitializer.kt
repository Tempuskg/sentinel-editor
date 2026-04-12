package com.sentinel.database

import android.content.Context
/**
 * Database initialization
 * Centralized accessor for app database startup.
 */
object DatabaseInitializer {
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
