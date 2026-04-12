package com.sentinel.editor.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sentinel.model.RepositoryContent
import com.sentinel.model.RepositoryItem

@Composable
fun FileExplorerScreen(
    currentRepository: RepositoryItem? = null,
    fileTree: List<RepositoryContent> = emptyList(),
    onFileSelected: ((String) -> Unit)? = null,
    onDirectorySelected: ((String) -> Unit)? = null
) {
    Text(
        text = if (fileTree.isEmpty()) "No files in repository" else "${fileTree.size} files",
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun FileTreeItem(
    item: RepositoryContent,
    onSelected: ((String) -> Unit)? = null,
    onDirectorySelected: ((String) -> Unit)? = null
) {
    Text(item.path)
}
