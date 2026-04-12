package com.sentinel.ui.markdown

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

/**
 * Compose Markdown Editor - WYSIWYG editor component
 *
 * Licensed under Apache 2.0 via com.sentinel.editor
 */
@Composable
fun ComposeMarkdownEditor(
    initialContent: String = "",
    onContentChange: (String) -> Unit = {}
) {
    var content by remember { mutableStateOf(initialContent) }
    BasicTextField(
        value = content,
        onValueChange = {
            content = it
            onContentChange(it)
        },
        modifier = Modifier.fillMaxSize()
    )
}