package com.sentinel.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sentinel.model.GitHubAuth

@Database(
    version = Constants.DATABASE_VERSION,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class SentinelDatabase : RoomDatabase() {
    
    abstract fun authDao(): AuthDao
    
    // TODO: Add other DAOs for repositories and files
    // abstract fun repositoryDao(): RepositoryDao
    // abstract fun fileDao(): FileDao
    
    companion object {
        @Volatile private var INSTANCE: SentinelDatabase? = null
        
        fun getInstance(context: Context): SentinelDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = SentinelDatabase.Builder(context)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}