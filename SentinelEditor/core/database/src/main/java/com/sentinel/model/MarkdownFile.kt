package com.sentinel.model

import androidx.room.Entity
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

/**
 * Markdown file content with editor state
 * 
 * @property repoId Association with a repository
 * @property relativePath Full path within repository
 * @property cursorPosition Cursor position in character buffer
 * @property scrollOffset Scroll position in pixels from top
 * @property isDirty Has unsaved changes
 * 
 * Licensed under Apache 2.0 via com.sentinel.editor
 */
@Entity(tableName = "files")
data class MarkdownFile(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "repo_id")
    val repoId: Long,
    @ColumnInfo(name = "relative_path")
    val relativePath: String,
    val content: String,
    @ColumnInfo(name = "cursor_position")
    val cursorPosition: Int = 0,
    @ColumnInfo(name = "scroll_offset")
    val scrollOffset: Int = 0,
    @ColumnInfo(name = "is_dirty")
    val isDirty: Boolean = false,
    val modified: Long = System.currentTimeMillis()
) {
    val fileName: String
        get() = relativePath.substringAfterLast('/')
    
    val dirPath: String
        get() = relativePath.substringBeforeLast('/')
}