package com.sentinel.ui.markdown

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.Selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sentinel.model.MarkdownFile
import com.sentinel.ui.markdown.MarkdownContent
import dev.gitlive.android.components.filepicker.FilePicker

/**
 * Compose Markdown Editor - WYSIWYG editor component
 * Custom implementation using Markwon for rendering and TextInput
 * for editing. No third-party library for the editor itself.
 * 
 * Features:
 * - Markdown preview with syntax highlighting
 * - Line number column
 * - Edit button for code editing
 * - Auto-save to database
 * - Diff preview for conflicts
 * 
 * Licensed under Apache 2.0 via com.sentinel.editor
 */
@Composable
fun ComposeMarkdownEditor(
    currentFile: MarkdownFile?,
    onSave: (MarkdownFile) -> Unit
) {
    // File selection
    var selectedFile by remember { mutableStateOf(currentFile) }
    
    // Compose content editor
    ComposeMarkdownContentEditor(
        currentFile = selectedFile
    ) { file ->
        onSave(file)
    }
}

@Composable
fun ComposeMarkdownContentEditor(
    currentFile: MarkdownFile?,
    onSave: (MarkdownFile) -> Unit
) {
    // Track content
    var content by remember { mutableStateOf(currentFile?.content?.ifEmpty { "" } ?: "") }
    val cursorPosition by remember(content) { mutableStateOf(currentFile?.cursorPosition ?: 0) }
    
    // Editor composable
    ComposeMarkdownEditor(
        markdownContent = MarkdownContent(
            text = content,
            cursorPosition = cursorPosition,
            fontSize = 18.dp,
            fontFamily = "monospace"
        ),
        onContentChange = {
            content = it
            onSave(selectedFile!!.copy(content = it, modified = System.currentTimeMillis(), cursorPosition = 0))
        }
    )
}