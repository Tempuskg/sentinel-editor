package com.sentinel.database

import androidx.room.*

@Database(
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {
    
    abstract fun authDao(): AuthDao
    abstract fun fileDao(): FileDao
    
    companion object {
        @Volatile private var INSTANCE: Database? = null
        
        fun getInstance(context: Context): Database {
            return INSTANCE ?: synchronized(this) {
                val instance = Database.Builder(context.applicationContext)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
