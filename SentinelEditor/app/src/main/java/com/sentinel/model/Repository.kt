package com.sentinel.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Repository entity for storing GitHub repository metadata
 * - repo id, name, description, visibility, owner info
 * - Last sync timestamp
 * - Pull request / issue stats
 */
@Entity(tableName = "repositories")
data class Repository(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val owner: String,
    val name: String,
    val htmlUrl: String,
    val description: String? = null,
    val fork: Boolean = false,
    val private: Boolean = false,
    val visibility: String = "public",
    val stargazersCount: Int = 0,
    val forksCount: Int = 0,
    val language: String? = null,
    val updatedDate: String, // ISO date string
    val syncDate: String // ISO date string of last sync
)
