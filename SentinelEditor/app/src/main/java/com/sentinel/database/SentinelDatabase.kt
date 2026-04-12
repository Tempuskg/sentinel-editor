package com.sentinel.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sentinel.model.GitHubAuth

@Database(
    entities = [GitHubAuth::class],
    version = 1,
    exportSchema = false
)
abstract class SentinelDatabase : RoomDatabase() {
    
    abstract fun authDao(): AuthDao
    
    // TODO: Add other DAOs for repositories and files
    // abstract fun repositoryDao(): RepositoryDao
    // abstract fun fileDao(): FileDao
    
    companion object {
        @Volatile private var INSTANCE: SentinelDatabase? = null
        
        fun getInstance(context: Context): SentinelDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SentinelDatabase::class.java,
                    "sentinel_database"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}