package com.sentinel.editor.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sentinel.model.*

/**
 * Editor Layout Composable
 * Split pane UI for markdown editing with preview
 * 
 * License: Apache 2.0 via com.sentinel.editor
 */
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
    var zoomLevel by remember { mutableFloatStateOf(90f) }
    var scroll by remember { mutableIntStateOf(0) }
    
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            // Toolbar
            EditorToolbar(
                isPreviewVisible = isPreviewVisible,
                onTogglePreview = onTogglePreview,
                onScroll = onScroll,
                zoom = zoomLevel,
                onZoom = onZoom,
                fileName = file?.filename ?: ""
            )
        }
        
        items(files ?: emptyList()) { file ->
            FileItem(
                file = file,
                modifier = Modifier.clickable { 
                    onNavigate?.invoke(file.relativePath)
                }
            )
        }
        
        item {
            Divider()
        }
        
        item {
            // Preview or Editor
            PreviewContent(
                file = file,
                isPreviewVisible = isPreviewVisible,
                onTogglePreview = onTogglePreview
            )
        }
    }
}

/**
 * Editor Toolbar
 * Contains editor controls and actions
 */
@Composable
fun EditorToolbar(
    isPreviewVisible: Boolean,
    onTogglePreview: () -> Unit,
    onScroll: ((Int) -> Unit)? = null,
    zoom: Float,
    onZoom: ((Float) -> Unit)? = null,
    fileName: String
) {
    Surface {
        Row {
            // File info
            Text(
                text = fileName,
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            )
            
            // Split pane toggle
            IconButton(onClick = onTogglePreview) {
                Icon(
                    if (isPreviewVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = "Toggle preview"
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Zoom controls
            if (onZoom != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextButton(onClick = { onZoom?.invoke(zoom - 5f) }) {
                        Icon(Icons.Default.ZoomOut, contentDescription = "Zoom out")
                    }
                    
                    Text(
                        text = "${zoom.toInt()}%",
                        fontSize = 12.sp
                    )
                    
                    TextButton(onClick = { onZoom?.invoke(zoom + 5f) }) {
                        Icon(Icons.Default.ZoomIn, contentDescription = "Zoom in")
                    }
                }
            }
        }
    }
}

/**
 * File Item Component
 * Shows repository and file paths
 */
@Composable
fun FileItem(
    file: com.sentinel.model.RepositoryContent,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Icon(
            Icons.Default.Folder,
            contentDescription = "File",
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        
        Text(
            text = file.path,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * Preview Content for Markdown Preview
 */
@Composable
fun PreviewContent(
    file: MarkdownContent?,
    isPreviewVisible: Boolean,
    onTogglePreview: () -> Unit
) {
    if (isPreviewVisible && file != null) {
        Surface {
            Column {
                // Render markdown
                // Preview pane with rendered markdown content
                // Uses Markwon renderer
                val markdown = file.content
                
                // Scrollable preview area
                ScrollablePreview(markdown = markdown)
            }
        }
    }
}
