package com.sentinel.editor.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sentinel.editor.MainViewModel
import com.sentinel.editor.ui.DeviceAuthScreen
import com.sentinel.editor.ui.EditorLayout
import com.sentinel.editor.ui.FileExplorerScreen
import com.sentinel.editor.ui.RepositoryListScreen
import com.sentinel.editor.ui.SettingsScreen
import com.sentinel.editor.ui.TokenEntryScreen
import com.sentinel.editor.utils.ThemeMode

@Composable
fun NavigationGraph(
    navController: NavHostController,
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit
) {
    val viewModel: MainViewModel = viewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    if (state.isCheckingAuth) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
        return
    }

    LaunchedEffect(state.isAuthenticated, state.isCheckingAuth, state.shouldNavigateToRestoredFile) {
        if (state.isCheckingAuth) {
            return@LaunchedEffect
        }

        val currentRoute = navController.currentBackStackEntry?.destination?.route
        if (state.isAuthenticated) {
            if (state.shouldNavigateToRestoredFile && currentRoute != "editor") {
                navController.navigate("editor") {
                    launchSingleTop = true
                }
                viewModel.consumeRestoredFileNavigation()
                return@LaunchedEffect
            }

            if (currentRoute == "token" || currentRoute == "device-auth") {
                navController.navigate("repos") {
                    popUpTo(navController.graph.findStartDestination().id) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        } else if (currentRoute != null && currentRoute != "token") {
            navController.navigate("token") {
                popUpTo(navController.graph.findStartDestination().id) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = "token"
    ) {
        composable(route = "token") {
            TokenEntryScreen(
                isLoading = state.isLoading,
                error = state.error,
                onConnect = viewModel::connectWithToken,
                onDeviceAuth = {
                    viewModel.startDeviceAuth()
                    navController.navigate("device-auth")
                }
            )
        }

        composable(route = "device-auth") {
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
                onLogout = viewModel::logout,
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
                filePath = state.selectedFilePath,
                content = state.selectedFileContent,
                cursorPosition = state.selectedFileCursorPosition,
                scrollOffset = state.selectedFileScrollOffset,
                isLoading = state.isLoading,
                isDirty = state.selectedFileDirty,
                isSaving = state.isSavingFile,
                saveError = state.saveError,
                lastCommitMessage = state.lastCommitMessage,
                onContentChange = viewModel::updateSelectedFileContent,
                onEditorPositionChange = viewModel::updateSelectedFilePosition,
                onSave = viewModel::saveSelectedFile,
                onBack = { navController.popBackStack() },
                onOpenSettings = { navController.navigate("settings") }
            )
        }

        composable(route = "settings") {
            SettingsScreen(
                themeMode = themeMode,
                onThemeModeChange = onThemeModeChange,
                onBack = { navController.popBackStack() }
            )
        }
    }
}