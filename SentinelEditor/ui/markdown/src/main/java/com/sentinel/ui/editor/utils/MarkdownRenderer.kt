package com.sentinel.ui.editor.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Markdown renderer utility
 *
 * Licensed under Apache 2.0 via com.sentinel.editor
 */
class MarkdownRenderer {

    suspend fun render(text: String): String = withContext(Dispatchers.Default) {
        text
    }

    companion object {
        const val TAG = "MarkdownRenderer"
    }
}