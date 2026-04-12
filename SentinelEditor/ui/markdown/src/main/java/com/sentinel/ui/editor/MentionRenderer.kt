package com.sentinel.ui.markdown

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Markdown content rendering component
 *
 * Licensed under Apache 2.0 via com.sentinel.editor
 */
@Composable
fun MarkdownContent(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(text = text, modifier = modifier.fillMaxWidth())
}
