package com.sentinel.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sentinel.model.MarkdownFile

@androidx.room.Database(
    entities = [MarkdownFile::class],
    version = 1,
    exportSchema = false
)
abstract class Database : RoomDatabase() {

    abstract fun fileDao(): FileDao

    companion object {
        @Volatile private var INSTANCE: Database? = null

        fun getInstance(context: Context): Database {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    Database::class.java,
                    "sentinel_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
