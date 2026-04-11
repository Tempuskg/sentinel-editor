package com.sentinel.editor.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sentinel.model.*

/**
 * File Explorer Screen
 * Shows file tree with expandable folders
 * 
 * License: Apache 2.0 via com.sentinel.editor
 */
@Composable
fun FileExplorerScreen(
    currentRepository: RepositoryItem? = null,
    fileTree: List<FileTreeItem> = emptyList(),
    onFileSelected: ((String) -> Unit)? = null,
    onDirectorySelected: ((String) -> Unit)? = null
) {
    if (fileTree.isEmpty()) {
        // Empty state
        Text(
            text = "No files in repository",
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            textAlign = TextAlign.Center
        )
        return
    }
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(
            items = fileTree,
            key = { it.path }
        ) { item ->
            FileTreeItem(
                item = item,
                onSelected = onFileSelected,
                onDirectorySelected = onDirectorySelected
            )
        }
    }
}

/**
 * File Tree Item Component
 * Handles file and folder display with expand/collapse
 */
@Composable
fun FileTreeItem(
    item: RepositoryContent,
    onSelected: ((String) -> Unit)? = null,
    onDirectorySelected: ((String) -> Unit)? = null
) {
    val colorScheme = MaterialTheme.colorScheme
    // Determine if this is a folder or file
    val isFolder = item.type == "dir"
    val icon: ImageVector = if (isFolder) Icons.Default.Folder else Icons.Default.Description
    val extension = item.name.substringAfterLast('.', "")
    
    // Click action
    val onClick: () -> Unit = if (onSelected != null) {
        { onSelected.invoke(item.path) }
    } else if (isFolder && onDirectorySelected != null) {
        { onDirectorySelected.invoke(item.path) }
    } else {
        {}
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(8.dp),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(contentColor = colorScheme.onSurface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Icon(
                icon,
                contentDescription = item.name,
                modifier = Modifier.size(24.dp),
                tint = if (isFolder) colorScheme.primary else colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Filename
            Text(
                text = item.name,
                style = if (isFolder) 
                    MaterialTheme.typography.bodyMedium 
                else 
                    MaterialTheme.typography.bodySmall,
                fontWeight = if (isFolder) FontWeight.Medium else FontWeight.Normal
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Extension if file
            if (!isFolder && extension.isNotBlank()) {
                Text(
                    text = ".$extension",
                    fontSize = 10.sp,
                    color = colorScheme.onSurfaceVariant
                )
            }
        }
    }
}