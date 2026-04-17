package com.sentinel.editor.navigation

import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sentinel.editor.MainViewModel
import com.sentinel.editor.ui.DeviceAuthScreen
import com.sentinel.editor.ui.EditorLayout
import com.sentinel.editor.ui.FileExplorerScreen
import com.sentinel.editor.ui.RepositoryListScreen
import com.sentinel.editor.ui.TokenEntryScreen

@Composable
fun NavigationGraph(navController: NavHostController) {
    val viewModel: MainViewModel = viewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = "token"
    ) {
        composable(route = "token") {
            TokenEntryScreen(
                isLoading = state.isLoading,
                error = state.error,
                onConnect = { token ->
                    viewModel.setToken(token)
                    viewModel.loadRepositories()
                    navController.navigate("repos") {
                        popUpTo("token") { inclusive = true }
                    }
                },
                onDeviceAuth = {
                    viewModel.startDeviceAuth()
                    navController.navigate("device-auth")
                }
            )
        }

        composable(route = "device-auth") {
            // Navigate to repos once the device flow completes successfully
            LaunchedEffect(state.deviceAuthPolling, state.deviceAuthUserCode) {
                if (viewModel.isDeviceAuthComplete()) {
                    navController.navigate("repos") {
                        popUpTo("token") { inclusive = true }
                    }
                }
            }

            DeviceAuthScreen(
                userCode = state.deviceAuthUserCode ?: "",
                verificationUri = state.deviceAuthVerificationUri ?: "https://github.com/login/device",
                isPolling = state.deviceAuthPolling,
                error = state.deviceAuthError,
                onCancel = {
                    viewModel.cancelDeviceAuth()
                    navController.popBackStack()
                },
                onRetry = {
                    viewModel.startDeviceAuth()
                }
            )
        }

        composable(route = "repos") {
            RepositoryListScreen(
                repos = state.repositories,
                isLoading = state.isLoading,
                error = state.error,
                onRepoClick = { repo ->
                    viewModel.selectRepository(repo)
                    navController.navigate("files")
                }
            )
        }

        composable(route = "files") {
            FileExplorerScreen(
                files = state.files,
                currentPath = state.currentPath,
                repoName = state.selectedRepo,
                isLoading = state.isLoading,
                error = state.error,
                onItemClick = { item ->
                    if (item.isDir) {
                        viewModel.loadFiles(item.path)
                    } else {
                        viewModel.openFile(item)
                        navController.navigate("editor")
                    }
                },
                onBackClick = {
                    if (state.currentPath.isEmpty()) {
                        navController.popBackStack()
                    } else {
                        viewModel.navigateUp()
                    }
                }
            )
        }

        composable(route = "editor") {
            EditorLayout(
                fileName = state.selectedFileName,
                content = state.selectedFileContent,
                isLoading = state.isLoading,
                onBack = { navController.popBackStack() }
            )
        }
    }
}