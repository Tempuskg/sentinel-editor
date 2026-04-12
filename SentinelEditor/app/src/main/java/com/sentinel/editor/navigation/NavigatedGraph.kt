package com.sentinel.editor.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sentinel.editor.MainViewModel
import com.sentinel.editor.ui.EditorLayout
import com.sentinel.editor.ui.FileExplorerScreen
import com.sentinel.editor.ui.TokenEntryScreen
import com.sentinel.editor.ui.RepositoryListScreen

/**
 * Navigation graph with 4-screen flow: Token → Repos → Files → Editor
 */
@Composable
fun NavigatedGraph(
    navController: NavHostController? = null,
    viewModel: MainViewModel = viewModel()
) {
    val controller = navController ?: rememberNavController()
    val token by viewModel.token.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val repos by viewModel.repositories.collectAsState()
    val error by viewModel.error.collectAsState()
    val selectedRepo by viewModel.selectedRepo.collectAsState()
    val files by viewModel.files.collectAsState()
    val currentPath by viewModel.currentPath.collectAsState()
    val selectedFile by viewModel.selectedFile.collectAsState()

    NavHost(navController = controller, startDestination = "token") {
        composable("token") { backStackEntry ->
            TokenEntryScreen(
                onTokenSubmitted = { tokenIt ->
                    viewModel.setToken(tokenIt)
                    // Navigate to repos after token is submitted
                    if (tokenIt.isNotEmpty()) {
                        controller.navigate("repos")
                    }
                }
            )
        }

        composable(
            route = "repos",
            exitAction = {
                if (controller.currentBackStackEntryFlow.value?.destination?.route == "repos") {
                    controller.popBackStack()
                }
            }
        ) { backStackEntry ->
            if (isLoading) {
                Scaffold { padding ->
                    Box(
                        modifier = Modifier.fillMaxSize().padding(padding),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            } else if (error != null) {
                Scaffold { padding ->
                    Column(
                        modifier = Modifier.fillMaxSize().padding(padding),
                        verticalArrangement = androidx.compose.foundation.layout.Alignment.CenterVertically
                    ) {
                        Text(
                            text = error ?: "Error",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Button(onClick = { controller.navigate("token") }) {
                            Text("Try Again")
                        }
                    }
                }
            } else {
                RepositoryListScreen(
                    repos = repos,
                    isLoading = isLoading,
                    onRepoClick = { repo ->
                        viewModel.loadFiles("")
                        viewModel.selectRepository(repo)
                        controller.navigate("files")
                    }
                )
            }
        }

        composable(
            route = "files",
            exitAction = {
                controller.popBackStack(destination = "repos", inclusive = false)
            }
        ) { backStackEntry ->
            FileExplorerScreen(
                currentRepository = selectedRepo,
                fileTree = files,
                onBackClick = {
                    if (currentPath.isEmpty()) {
                        controller.navigate("repos")
                    } else {
                        // Go back one level
                        val parentPath = currentPath.substringBefore("/").ifEmpty { "" }
                        viewModel.loadFiles(parentPath)
                    }
                },
                currentPath = currentPath,
                onFileSelected = { item ->
                    viewModel.openFile(item)
                    controller.navigate("editor")
                },
                onDirectorySelected = { item ->
                    viewModel.loadFiles(item.path)
                }
            )
        }

        composable(
            route = "editor",
            exitAction = {
                controller.popBackStack(destination = "files", inclusive = false)
            }
        ) { backStackEntry ->
            EditorLayout(
                file = selectedFile,
                fileName = selectedFile?.name,
                content = selectedFile?.content,
                onBack = { controller.navigate("files") }
            )
        }
    }
}