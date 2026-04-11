package com.sentinel.ui.markdown

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.onPointerInputEvent
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.compose.ui.viewinterop.AndroidView
import com.sentinel.database.Database
import io.noties.markwon.Markwon
import io.noties.markwon.html.HtmlRenderer
import io.noties.markwon.linkify.LinkifyRenderer
import io.noties.markwon.math.MathJaxAdapter

/**
 * Main Editor Composable
 * Split-pane markdown editor with syntax highlighting
 * 
 * License: Apache 2.0 via com.sentinel.editor
 */
@Composable
@OptIn(ExperimentalFoundationApi::class)
fun ComposerMarkdownEditor(
    markdownContent: String,
    showPreview: Boolean = true,
    showCursor: Boolean = true,
    cursorPosition: Int = 0
) {
    val context = LocalContext.current
    
    SplitPaneContent(
        markdownContent = markdownContent,
        showPreview = showPreview,
        showCursor = showCursor,
        cursorPosition = cursorPosition
    )
}

/**
 * Split pane content with editor and preview
 */
@Composable
fun SplitPaneContent(
    markdownContent: String,
    showPreview: Boolean = true,
    showCursor: Boolean = true,
    cursorPosition: Int = 0
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .onPointerInputEvent(InputEventType.Press) { event -> }
    ) {
        // Editor pane (code input)
        EditorPane(
            content = markdownContent,
            cursorPosition = cursorPosition,
            showCursor = showCursor
        )
        
        // Divider
        if (showPreview) {
            Divider()
            
            // Preview pane
            PreviewPane(
                markdown = markdownContent,
                showCursor = showCursor,
                cursorPosition = cursorPosition
            )
        }
    }
}

/**
 * Editor input pane
 */
@Composable
fun EditorPane(
    content: String,
    cursorPosition: Int = 0,
    showCursor: Boolean = true
) {
    TextField(
        value = content,
        onValueChange = { },
        readOnly = true,
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            fontFamily = FontSerpentSerif(),
            fontSize = 16.sp
        ),
        singleLine = false,
        maxLines = if (showCursor) Int.MAX_VALUE else 1,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = colorScheme.surfaceVariant,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

/**
 * Markdown preview pane using Markwon
 */
@Composable
fun PreviewPane(
    markdown: String,
    showCursor: Boolean = false,
    cursorPosition: Int = 0
) {
    // Markwon is lazy - render on demand
    var renderedHtml by remember { mutableStateOf("") }
    
    // TODO: Initialize Markwon once at app level
    // val markwon = Markwon.create(context)
    
    // Render markdown to HTML (or direct compose)
    // For now, show plain text as placeholder
    Text(
        text = markdown.replace('\n', '<br/>'),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        textAlign = TextAlign.Left,
        fontWeight = FontWeight.Light
    )
}

/**
 * Syntax highlighting theme
 */
object FontSerpentSerif {
    fun Serif() = FontSerif
}
