package com.sentinel.ui.layout

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.sentinel.editor.ui.theme.SentinelEditorTheme

/**
 * Editor layout module
 * Defines layouts for:
 * - Repository list screen
 * - File explorer
 * - Editor (markdown preview + edit)
 * - Settings
 * - Pull request manager
 * 
 * Licensed under Apache 2.0 via com.sentinel.editor
 */

@Composable
fun EditorScreen(
    title: String = "Editor",
    currentFile: com.sentinel.model.MarkdownFile? = null,
    onNavigateUp: () -> Unit,
    onNewFile: () -> Unit,
    onSave: () -> Unit,
    onSettings: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top toolbar
        EditorToolbar(
            title = title,
            onNew = onNewFile,
            onSave = onSave,
            onSettings = onSettings
        )
        
        // Split view
        SplitLayout(currentFile = currentFile)
    }
}

@Composable
fun RepositoryScreen(
    repositories: List<RepositoryItem> = emptyList()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        repositories.forEach { repo ->
            RepositoryItemCard(repo = repo)
        }
    }
}

@Composable
fun FileExplorerScreen(
    currentFile: com.sentinel.model.MarkdownFile?,
    onNavigateUp: () -> Unit,
    onOpenFile: (path: String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // File list item
        FileListItem(
            file = currentFile
        ) { path ->
            onOpenFile(path)
        }
    }
}

@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Settings", style = MaterialTheme.typography.titleMedium)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Settings items
    }
}