package com.sentinel.model

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
data class MarkdownFile(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val repoId: Long,
    val relativePath: String,
    val content: String,
    val cursorPosition: Int = 0,
    val scrollOffset: Int = 0,
    val isDirty: Boolean = false,
    val modified: Long = System.currentTimeMillis()
) {
    val fileName: String
        get() = relativePath.substringAfterLast('/')
    
    val dirPath: String
        get() = relativePath.substringBeforeLast('/')
}