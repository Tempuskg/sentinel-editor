package com.sentinel.ui.markdown

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import io.noties.markwon.*
import io.noties.markwon.coremark.CoreMark
import io.noties.markwon.math.MathJaxAdapter
import io.noties.markwon.table.plugin.Table.plugin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

/**
 * Markwon Renderer for Preview Pane
 * Renders markdown with syntax highlighting
 * 
 * License: Apache 2.0 via com.sentinel.editor
 */
@Composable
fun PreviewPane(
    markdownContent: String,
    modifier: Modifier = Modifier,
    onPreviewError: ((Exception) -> Unit)? = null
) {
    val scrollState = rememberScrollState()
    val markdown by remember {
        MutableStateFlow(markdownContent)
    }
    
    val renderedContent by remember {
        MutableStateFlow(emptyString())
    }
    
    // Render markdown on compose event
    LaunchedEffect(markdown, renderedContent.text) {
        withContext(Dispatchers.IO) {
            try {
                val text = markdown.render().rendered
                renderedContent.value = text
            } catch (e: Exception) {
                onPreviewError?.invoke(e)
            }
        }
    }
    
    Surface {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxSize(),
                colors = CardDefaults.cardColors(
                    containerColor = colorScheme.surfaceVariant
                )
            ) {
                Text(
                    text = renderedContent.text,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}
