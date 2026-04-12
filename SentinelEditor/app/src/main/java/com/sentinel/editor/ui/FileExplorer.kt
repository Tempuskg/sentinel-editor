package com.sentinel.editor.ui

import android.content.res.Resources
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sentinel.model.RepositoryContent
import com.sentinel.model.RepositoryItem
import com.sentinel.util.Constants
import com.sentinel.ui.editor.MarkdownContent
import com.sentinel.ui.markdown.MarkdownContent
import java.nio.charset.StandardCharsets

@Composable
fun FileExplorerScreen(
    currentRepository: RepositoryItem? = null,
    fileTree: List<RepositoryContent> = emptyList(),
    onFileSelected: ((RepositoryContent) -> Unit)? = null,
    onDirectorySelected: ((RepositoryContent) -> Unit)? = null,
    onBackClick: () -> Unit = {},
    currentPath: String = ""
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (currentPath.isNotEmpty()) {
                        Text(text = currentPath)
                    } else {
                        Text(
                            text = if (currentRepository != null) {
                                "${currentRepository.name}"
                            } else {
                                stringResource(id = R.string.app_name)
                            }
                        )
                    }
                },
                navigationIcon = {
                    if (currentPath.isNotEmpty()) {
                        Button(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(fileTree, key = { item -> item.name }) { item ->
                val type = item.type
                val name = item.name
                val path = item.path
                
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .padding(horizontal = 16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(
                                    if (type == "dir") Color.LightGray else Color.Blue,
                                    MaterialTheme.shapes.small
                                )
                        ) {
                            Icon(
                                imageVector = if (type == "dir") Icons.Default.Folder else Icons.Default.Description,
                                contentDescription = if (type == "dir") "Directory" else "File",
                                tint = Color.White,
                                modifier = Modifier.padding(6.dp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .verticalAlignment(Alignment.CenterVertically)
                        ) {
                            Text(
                                text = name,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = if (type == "dir") FontFamily.Default else FontFamily.Monospace
                                )
                            )
                            Text(
                                text = path.ifEmpty { name },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        Button(
                            onClick = {
                                if (type == "dir" && onDirectorySelected != null) {
                                    onDirectorySelected(item)
                                } else if (onFileSelected != null) {
                                    onFileSelected(item)
                                }
                            },
                            enabled = type == "file" && onFileSelected != null
                        ) {
                            Text(
                                text = if (type == "dir") "Enter" else "View",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }
            
            item {
                fileTree.ifEmpty {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No files in repository",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}