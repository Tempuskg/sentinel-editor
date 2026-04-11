package com.sentinel.editor.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sentinel.ui.markdown.MarkdownContent

@Composable
fun EditorLayout(
    file: MarkdownContent?,
    modifier: Modifier = Modifier,
    isPreviewVisible: Boolean = true,
    onTogglePreview: () -> Unit = {},
    onScroll: ((Int) -> Unit)? = null,
    onZoom: ((Float) -> Unit)? = null,
    onNavigate: ((String) -> Unit)? = null
) {
    Box(modifier = modifier.fillMaxSize()) {
        Text(file?.content ?: "No file selected")
    }
}

@Composable
fun EditorToolbar(
    isPreviewVisible: Boolean,
    onTogglePreview: () -> Unit,
    onScroll: ((Int) -> Unit)? = null,
    zoom: Float,
    onZoom: ((Float) -> Unit)? = null,
    fileName: String
) {}

@Composable
fun PreviewContent(
    file: MarkdownContent?,
    isPreviewVisible: Boolean,
    onTogglePreview: () -> Unit
) {}
