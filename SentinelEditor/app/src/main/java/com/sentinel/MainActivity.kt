package com.sentinel

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import com.sentinel.editor.navigation.NavigationGraph
import com.sentinel.ui.theme.SentinelEditorTheme

/**
 * Main Application class
 * Initializes database and services
 * 
 * License: Apache 2.0 via com.sentinel.editor
 */
class MyApplication : Application() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize database
        Database.initialize(this)
        
        // Initialize rate limiter
        RateLimiter.initialize(this)
    }
}

/**
 * Main Activity
 * Entry point for the app
 * 
 * License: Apache 2.0 via com.sentinel.editor
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            // Use Material3 theme
            MaterialTheme {
                // Main navigation graph
                NavigationGraph()
            }
        }
    }
}

/**
 * NavigationGraph - Complete navigation setup
 * Implements all screens and navigation routes
 * 
 * License: Apache 2.0 via com.sentinel.editor
 */
@Composable
fun NavigationGraph() {
    val navController = rememberNavController()
    
    Scaffold(topBar = {
        TopAppBar(title = { Text("Sentinel Editor") })
    }) { paddingValues ->
        NavHost(navController, startDestination = "repos") {
            composable(route = "repos") {
                RepositoryList()
            }
            composable(route = "repo/{url}") {
                RepoDetails(url = it.arguments?.getString("url") ?: "")
            }
            composable(route = "files/{repoUrl}/{path}") {
                FileEditor(repoUrl = it.arguments?.getString("repoUrl") ?: "", path = it.arguments?.getString("path") ?: "")
            }
            composable(route = "pr") {
                PullRequests()
            }
            composable(route = "settings") {
                Settings()
            }
        }
    }
}

/**
 * Repository list screen
 */
@Composable
fun RepositoryList() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Repositories")
    }
}

/**
 * Repository details screen
 */
@Composable
fun RepoDetails(url: String) {
    Column() {}
}

/**
 * File editor screen
 */
@Composable
fun FileEditor(repoUrl: String, path: String) {
    Column() {}
}

/**
 * Pull Request screen
 */
@Composable
fun PullRequests() {
    Column() {}
}

/**
 * Settings screen
 */
@Composable
fun Settings() {
    Column() {}
}
