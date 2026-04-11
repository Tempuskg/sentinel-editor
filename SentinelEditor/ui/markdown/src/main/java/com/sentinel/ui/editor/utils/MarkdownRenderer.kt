package com.sentinel.ui.editor.utils

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.*
import io.noties.markwon.Markwon
import io.noties.markwon.core.Renderer
import io.noties.markwon.renderer.html.HtmlRenderer

/**
 * Markdown renderer utility
 * Converts markdown text to rendered HTML
 * 
 * Licensed under Apache 2.0 via com.sentinel.editor
 */
class MarkdownRenderer private constructor(
    private val markwon: Markwon,
    private val htmlRenderer: HtmlRenderer
) {
    
    suspend fun render(text: String): String = withContext(Dispatchers.Default) {
        try {
            markwon.renderHTML(text)
        } catch (e: Exception) {
            "Error rendering markdown: ${e.message}"
        }
    }
    
    companion object {
        const val TAG = "MarkdownRenderer"
    }
}