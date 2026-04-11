package com.sentinel.ui.markdown

/**
 * Markdown file content with editor state
 * 
 * @property owner Repository owner
 * @property name Repository name
 * @property path File path
 * @property content File content (markdown)
 * @property cursorPosition Cursor position in character buffer
 * 
 * Licensed under Apache 2.0 via com.sentinel.editor
 */
data class MarkdownContent(
    val owner: String,
    val name: String,
    val path: String,
    val content: String,
    val cursorPosition: Int = 0,
    val isDirty: Boolean = false
) {
    val displayName: String
        get() = "$owner / $name / path"
}