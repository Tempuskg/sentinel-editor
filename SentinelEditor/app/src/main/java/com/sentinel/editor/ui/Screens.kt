package com.sentinel.editor.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Main Editor Layout
 */
@Composable
fun EditorLayout(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        Text("Editor", modifier = Modifier.padding(20.dp), fontWeight = FontWeight.Bold)
    }
}

/**
 * File Explorer Screen
 */
@Composable
fun FileExplorerScreen(modifier: Modifier = Modifier) {
    Text("File Explorer", modifier = Modifier.padding(20.dp), fontWeight = FontWeight.Bold)
}

/**
 * Token Entry Screen
 */
@Composable
fun TokenEntryScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize().padding(20.dp)) {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Enter Token") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/**
 * Repository List Screen
 */
@Composable
fun RepositoryListScreen(
    modifier: Modifier = Modifier,
    selectedFile: String? = null,
    onBack: (() -> Unit)? = null
) {
    // Placeholder for repository list
    Box(modifier = modifier.fillMaxSize()) {
        Text(
            "Repositories", 
            modifier = Modifier.padding(20.dp),
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Repository List Content
 */
@Composable
fun RepositoryListContent(
    modifier: Modifier = Modifier,
    selectedFile: String? = null
) {
    Column(modifier = modifier.fillMaxSize()) {
        Text("Repository List", modifier = Modifier.padding(20.dp), fontWeight = FontWeight.Bold)
    }
}