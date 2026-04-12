package com.sentinel.ui.markdown

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MarkwonEditor(
    content: String,
    modifier: Modifier = Modifier
) {
    Text(text = content, modifier = modifier.fillMaxSize())
}

@Composable
fun PreviewPane(
    markdownContent: String,
    modifier: Modifier = Modifier,
    onPreviewError: ((Exception) -> Unit)? = null
) {
    Text(text = markdownContent, modifier = modifier.fillMaxSize())
}
