package com.sentinel.editor.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sentinel.database.DatabaseInitializer
import com.sentinel.ui.layout.*
import com.sentinel.ui.editor.EditFileScreen
import com.sentinel.ui.editor.ToolbarScreen
import com.sentinel.model.MarkdownFile
import java.util.UUID

/**
 * Main navigation graph with bottom navigation bar
 * Implements the 4 main screens: Repositories, Files, Pull Requests, Settings
 * 
 * License: Apache 2.0 via com.sentinel.editor
 */
@Composable
fun NavigatedGraph(
    viewModel: DatabaseViewModel = viewModel()
) {
    val navController = rememberNavController()
    val currentRepo by navController.haptic.currentBackStackEntryFlow.collectAsState(initial = null)
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sentinel Editor") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigation(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                BottomNavigationItem(
                    icon = {
                        Icon(if (navController.currentDestination?.route == "repos") Icons.Filled.Repository else Icons.Outlined.Repository,
                        contentDescription = "Repositories")
                    },
                    label = { Text("Repos") },
                    selected = navController.currentDestination?.route == "repos"
                ) {
                    navController.navigate("repos") {
                        popUpTo("repos") { inclusive = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
                
                BottomNavigationItem(
                    icon = {
                        Icon(if (navController.currentDestination?.route == "files") Icons.Filled.Folder else Icons.Outlined.Folder,
                        contentDescription = "Files")
                    },
                    label = { Text("Files") },
                    enabled = false, // Implement later
                    selected = false
                )
                
                BottomNavigationItem(
                    icon = {
                        Icon(if (navController.currentDestination?.route == "pr") Icons.Filled.CodeUpdate else Icons.Outlined.CodeUpdate,
                        contentDescription = "Pull Requests")
                    },
                    label = { Text("PRs") },
                    selected = navController.currentDestination?.route == "pr"
                ) {
                    navController.navigate("pr") {
                        popUpTo("pr") { inclusive = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
                
                BottomNavigationItem(
                    icon = {
                        Icon(if (navController.currentDestination?.route == "settings") Icons.Filled.Settings else Icons.Outlined.Settings,
                        contentDescription = "Settings")
                    },
                    label = { Text("Settings") },
                    selected = navController.currentDestination?.route == "settings"
                ) {
                    navController.navigate("settings") {
                        popUpTo("settings") { inclusive = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        }
    ) { paddingValues ->
        // Main bottom navigation
        if (currentRepo == null) {
            RepositoryScreen(
                repositories = viewModel.repos,
                onNavigateToRepo = { repo ->
                    navController.navigate("repo/$repo.htmlUrl") {
                        popUpTo(0) { inclusive = false }
                    }
                },
                onNavigateToRepos = { navController.navigate("repos") }
            ) { navController.navigate("repos") }
        } else {
            EditorScreen(
                currentFile = viewModel.currentFile,
                onNavigateUp = { navController.popBackStack() }
            )
        }
    }
}

/**
 * Repository list screen
 */
@Composable
fun RepositoryItemCard(repo: RepositoryItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .height(100.dp),
        onClick = { }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Column(modifier = Modifier.padding(start = 12.dp, end = 8.dp)) {
                Text(
                    text = repo.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = repo.htmlUrl?.substringAfterLast('/') ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

/**
 * File tree item for repository
 */
@Composable
fun FileListItem(file: MarkdownFile?, onClick: () -> Unit) {
    Card(onClick = { onClick }) {
        Row(modifier = Modifier.padding(8.dp)) {
            // File/folder icon
            Icon(
                imageVector = Icons.Default.Folder,
                contentDescription = null
            )
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(
                    text = file?.relativePath ?: "No file",
                    style = MaterialTheme.typography.bodyMedium,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
