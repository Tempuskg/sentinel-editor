package com.sentinel.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * File entity for storing markdown file content and metadata
 * - file path, repo association, markdown content
 * - Editor state: cursor position, selection
 * - Change tracking for auto-save
 */
@Entity(tableName = "files")
data class MarkdownFile(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val repoId: Long,
    val relativePath: String, // e.g., "docs/readme.md"
    val fileName: String,
    val content: String,
    val cursorPosition: Int = 0, // Character index
    val scrollOffset: Int = 0, // Pixels from top
    val modified: Long, // Timestamp of last modification
    val isDirty: Boolean = false // Has unsaved changes
)
