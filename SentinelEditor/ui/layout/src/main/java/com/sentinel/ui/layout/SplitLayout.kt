package com.sentinel.ui.layout

import com.sentinel.model.MarkdownFile
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyLazyState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

/**
 * Split layout for editor with resizable panes
 * Implements editor-preview split pane
 * 
 * License: Apache 2.0 via com.sentinel.editor
 */
@Composable
fun SplitLayout(
    currentFile: MarkdownFile?
) {
    // Split panes: Editor - Preview
    var showPanel by remember { mutableStateOf(true) }
    val splitPane = rememberSplitPane(
        showPanel = showPanel,
        verticalOrientation = VerticalOrientation.Horizontal
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxWidth()
                .padding(16.dp)
            ,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Editor pane
            EditorPane(
                file = currentFile,
                showPanel = showPanel
            )
            Divider(
                vertical = 2.dp,
                modifier = Modifier.padding(8.dp)
            )
            // Preview pane
            PreviewPane(currentFile = currentFile)
        }
    }
}

/**
 * Editor pane
 */
@Composable
fun EditorPane(
    file: MarkdownFile?,
    showPanel: Boolean = true
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures(
                    onHorizontalDrag = { direction, _ ->
                        showPanel = false
                    }
                )
            }
    ) {
        if (file != null) {
            item {
                // File header
                Text(
                    text = file.fileName,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            item {
                // Editor content
                Text(
                    text = "Editor content for: ${file.fileName}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            item {
                // Empty state
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "Select a file to edit",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

/**
 * Preview pane
 */
@Composable
fun PreviewPane(
    @ParameterizedMarker("Preview") file: MarkdownFile? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Preview",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Preview content will be rendered here using Markwon
    }
}
