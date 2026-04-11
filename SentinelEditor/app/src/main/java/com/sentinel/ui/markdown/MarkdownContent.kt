package com.sentinel.ui.markdown

import androidx.compose.runtime.*
import com.sentinel.database.DatabaseInitializer
import com.sentinel.model.*
import java.nio.file.Paths

/**
 * Markdown Content Model
 * Represents a markdown file with cursor and editor state
 * 
 * License: Apache 2.0 via com.sentinel.editor
 */
data class MarkdownContent(
    val id: Long,
    val relativePath: String,
    val content: String,
    val cursorPosition: Int = 0,
    val scrollOffset: Int = 0,
    val zoomLevel: Float = 90f,
    val theme: EditorTheme = EditorTheme.DARK
) {
    /**
     * Get the file's directory relative path
     */
    val directoryPath: String get() = Paths.get(relativePath).parent.toString()
    
    /**
     * Get the file's filename
     */
    val filename: String get() = relativePath.split('/').last()
    
    /**
     * Check if content changed since save
     */
    fun isChanged(oldContent: String): Boolean {
        return content != oldContent
    }
    
    /**
     * Get the number of lines in the markdown
     */
    fun lineCount(): Int {
        return content.split("\n").size
    }
    
    /**
     * Get lines from start to cursor
     */
    fun linesTillCursor(): String {
        val lines = content.split("\n").take(content.getLines().indexOf(content.substring(0, cursorPosition)) + 1)
        return lines.joinToString("\n")
    }
}

/**
 * Markdown Editor State
 * Manages editor state, cursor, and changes
 */
@Composable
fun MarkdownEditorState(
    fileContent: MarkdownContent?
): MarkdownEditorState {
    var content by remember { mutableStateOf("") }
    var cursor by remember { mutableIntStateOf(0) }
    var scroll by remember { mutableIntStateOf(0) }
    var zoom by remember { mutableFloatStateOf(90f) }
    val isPreviewVisible by remember { mutableStateOf(true) }
    
    fileContent?.let {
        content = it.content
        cursor = it.cursorPosition
        scroll = it.scrollOffset
        zoom = it.zoomLevel
    } ?: run {
        content = ""
    }
    
    // Observe content changes from file
    DisposableEffect(Unit) {
        // Setup file observer
        onDispose {}
    }
    
    return MarkdownEditorState(
        content = content,
        cursor = cursor,
        scroll = scroll,
        zoom = zoom,
        isPreviewVisible = isPreviewVisible
    )
}
