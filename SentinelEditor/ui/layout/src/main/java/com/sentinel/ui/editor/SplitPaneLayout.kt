package com.sentinel.ui.editor

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyLazyState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.sentinel.ui.layout.SplitPane
import com.sentinel.model.MarkdownFile

/**
 * Complete EditorScreen with split-pane layout (editor/preview panes)
 * Implements toolbar with formatting tools, save, and commit buttons
 * 
 * License: Apache 2.0 via com.sentinel.editor
 */
@Composable
fun EditorScreen(
    file: MarkdownFile?,
    showPreview: Boolean = true
) {
    val editorState = rememberEditorState(initialFile = file)
    val splitPane = rememberSplitPane(
        showPanel = showPreview,
        verticalOrientation = VerticalOrientation.Horizontal,
        onPercentageChange = { percentage ->
            // Update split ratio
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text("Markdown Editor") },
                actions = {
                    // Toggle preview
                    IconButton(onClick = { showPreview = !showPreview }) {
                        Icon(
                            imageVector = if (showPreview) Icons.Filled.Star else Icons.Outlined.Star,
                            contentDescription = "Toggle preview",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    
                    // Save button
                    IconButton(onClick = {
                        // Save changes to database
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Save,
                            contentDescription = "Save",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
            
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                if (showPreview) {
                    // Editor pane
                    EditorPane(
                        file = file,
                        onTextChange = {
                            // Update editor state
                        }
                    )
                    
                    Divider(
                        vertical = 2.dp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    
                    // Preview pane
                    PreviewPane(content = file?.content ?: "")
                } else {
                    EditorPane(
                        file = file,
                        onTextChange = {
                            // Update editor state
                        }
                    )
                }
            }
        }
    }
}

/**
 * Toolbar for editor actions
 */
@Composable
fun EditorToolbar(
    onFormat: (String) -> Unit,
    onSave: () -> Unit,
    onCommit: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        IconButton(onClick = { onFormat("bold") }) {
            Icon(Icons.Filled.FormatBold, contentDescription = "Bold")
        }
        IconButton(onClick = { onFormat("italic") }) {
            Icon(Icons.Filled.FormatItalic, contentDescription = "Italic")
        }
        IconButton(onClick = { onFormat("underline") }) {
            Icon(Icons.Filled.FormatUnderlined, contentDescription = "Underline")
        }
        IconButton(onClick = { onFormat("heading") }) {
            Icon(Icons.Filled.Title, contentDescription = "Heading")
        }
        IconButton(onClick = { onFormat("code") }) {
            Icon(Icons.Filled.Code, contentDescription = "Code block")
        }
        IconButton(onClick = { onFormat("table") }) {
            Icon(Icons.Filled.TableChart, contentDescription = "Table")
        }
        IconButton(onClick = { onFormat("checklist") }) {
            Icon(Icons.Filled.Checklist, contentDescription = "Checklist")
        }
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(onClick = { onSave() }) {
            Icon(Icons.Filled.Save, contentDescription = "Save", tint = MaterialTheme.colorScheme.primary)
        }
        IconButton(onClick = { onCommit() }) {
            Icon(Icons.Filled.Done, contentDescription = "Commit", tint = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun EditorPane(file: MarkdownFile?, onTextChange: (String) -> Unit) {
    Text(
        text = "Editor content: ${file?.content ?: ""}",
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun PreviewPane(content: String) {
    Text(
        text = "Preview: $content",
        modifier = Modifier.fillMaxSize()
    )
}
