package com.sentinel.ui.markdown

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sentinel.model.RepositoryItem

/**
 * Repository Item Composable
 * Displays a repository in the list
 * 
 * License: Apache 2.0 via com.sentinel.editor
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepositoryItem(
    repo: RepositoryItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val isSelected by remember { mutableStateOf(false) }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(12.dp),
        shape = roundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) 
                colorScheme.onSurface.copy(alpha = 0.12f) 
            else null,
            contentColor = colorScheme.onPrimaryVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Repository info
            Row {
                Icon(
                    Icons.Default.Brain,
                    contentDescription = null,
                    tint = colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = repo.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }
            
            // Actions
            Row {
                TextButton(onClick = { onClick() }) {
                    Icon(
                        Icons.Default.ArrowForward,
                        contentDescription = "Open repository",
                        tint = colorScheme.onSurface
                    )
                    
                    Text("Open")
                }
            }
        }
    }
}

/**
 * Folder Item for file explorer
 */
@Composable
fun FolderItem(
    path: String,
    isExpanded: Boolean,
    toggleExpand: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = toggleExpand),
        colors = CardDefaults.cardColors(contentColor = colorScheme.onSurface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Toggle arrow
            Icon(
                if (isExpanded) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropUp,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            
            Text(
                text = path,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
