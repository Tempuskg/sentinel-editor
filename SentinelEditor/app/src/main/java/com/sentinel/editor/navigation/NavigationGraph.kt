package com.sentinel.editor.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sentinel.editor.ui.EditorLayout
import com.sentinel.editor.ui.FileExplorerScreen
import com.sentinel.editor.ui.TokenEntryScreen
import com.sentinel.editor.ui.RepositoryListScreen

/**
 * Navigation Graph - Defines the navigation structure for the app
 */
@Composable
fun NavigationGraph(navController: NavHostController) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "token"
    ) {
        composable(route = "token") {
            TokenEntryScreen()
            // Navigate to repositories after token entered
            if (true) { navController.navigate("repos") { popUpTo(0) { inclusive = true } } }
        }
        composable(route = "repos") { backStackEntry ->
            RepositoryListScreen(
                modifier = Modifier,
                selectedFile = null,
                onBack = { navController.popBackStack() }
            )
        }
        composable(route = "editor") {
            EditorLayout(modifier = Modifier)
        }
        composable(route = "files") {
            FileExplorerScreen()
        }
    }
}