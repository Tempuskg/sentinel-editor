package com.sentinel.editor.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sentinel.service.ContentResponse
import com.sentinel.service.RepositoryResponse

@Composable
fun TokenEntryScreen(
    isLoading: Boolean = false,
    error: String? = null,
    onConnect: (String) -> Unit = {},
    onDeviceAuth: () -> Unit = {}
) {
    var token by remember { mutableStateOf("") }
    var showHelp by remember { mutableStateOf(false) }

    if (showHelp) {
        AlertDialog(
            onDismissRequest = { showHelp = false },
            confirmButton = {
                TextButton(onClick = { showHelp = false }) { Text("Got it") }
            },
            title = { Text("Personal Access Token") },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("How to generate a token:", fontWeight = FontWeight.Bold)
                    Text("1. Go to github.com → Settings → Developer settings → Personal access tokens → Tokens (classic)")
                    Text("2. Click \"Generate new token (classic)\"")
                    Text("3. Set a name, e.g. \"Sentinel Editor\"")
                    Text("4. Set expiration to 90 days (recommended)")
                    Text("5. Select the scopes below, then click Generate token")
                    Text("6. Copy it immediately — GitHub won't show it again")
                    Spacer(Modifier.height(4.dp))
                    Text("Recommended scopes:", fontWeight = FontWeight.Bold)
                    Text("• repo — full access to public and private repos (required)")
                    Text("• read:org — list organization repositories (optional)")
                    Text("• read:user — read your profile info (optional)")
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Tip: Fine-grained tokens also work — grant Contents (read) and Metadata (read) on the repos you want to access.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Sentinel Editor",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Enter your GitHub Personal Access Token",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(4.dp))
        TextButton(
            onClick = { showHelp = true },
            modifier = Modifier.align(Alignment.Start),
            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
        ) {
            Icon(
                Icons.Default.Info,
                contentDescription = null,
                modifier = Modifier.size(14.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                "How do I get a token?",
                style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = token,
            onValueChange = { token = it },
            label = { Text("Personal Access Token") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        if (error != null) {
            Spacer(Modifier.height(8.dp))
            Text(
                error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { onConnect(token) },
            enabled = token.isNotBlank() && !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
            } else {
                Text("Connect")
            }
        }

        Spacer(Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f))
            Text(
                "  or  ",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            HorizontalDivider(modifier = Modifier.weight(1f))
        }

        Spacer(Modifier.height(16.dp))

        OutlinedButton(
            onClick = onDeviceAuth,
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign in with GitHub")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepositoryListScreen(
    repos: List<RepositoryResponse> = emptyList(),
    isLoading: Boolean = false,
    error: String? = null,
    onRepoClick: (RepositoryResponse) -> Unit = {}
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Repositories") }) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                error != null -> Text(
                    error,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    color = MaterialTheme.colorScheme.error
                )
                repos.isEmpty() -> Text(
                    "No repositories found",
                    modifier = Modifier.align(Alignment.Center)
                )
                else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(repos, key = { it.fullName }) { repo ->
                        ListItem(
                            headlineContent = {
                                Text(repo.name, fontWeight = FontWeight.Medium)
                            },
                            supportingContent = {
                                Column {
                                    if (!repo.description.isNullOrBlank()) {
                                        Text(repo.description, maxLines = 1)
                                    }
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        if (repo.language != null) {
                                            Text(
                                                repo.language,
                                                style = MaterialTheme.typography.labelSmall
                                            )
                                        }
                                        Text(
                                            "★ ${repo.stargazersCount}",
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    }
                                }
                            },
                            modifier = Modifier.clickable { onRepoClick(repo) }
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileExplorerScreen(
    files: List<ContentResponse> = emptyList(),
    currentPath: String = "",
    repoName: String = "",
    isLoading: Boolean = false,
    error: String? = null,
    onItemClick: (ContentResponse) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (currentPath.isEmpty()) repoName
                        else currentPath.substringAfterLast('/')
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                error != null -> Text(
                    error,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    color = MaterialTheme.colorScheme.error
                )
                files.isEmpty() -> Text(
                    "Empty directory",
                    modifier = Modifier.align(Alignment.Center)
                )
                else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(files, key = { it.path }) { item ->
                        ListItem(
                            headlineContent = { Text(item.name) },
                            leadingContent = {
                                Icon(
                                    imageVector = if (item.isDir) Icons.Default.Folder
                                    else Icons.Default.Description,
                                    contentDescription = null
                                )
                            },
                            supportingContent = if (item.isFile) ({
                                Text(
                                    "${item.size} bytes",
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }) else null,
                            modifier = Modifier.clickable { onItemClick(item) }
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorLayout(
    fileName: String? = null,
    content: String? = null,
    isLoading: Boolean = false,
    onBack: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(fileName ?: "Editor") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                content == null -> Text(
                    "No file selected",
                    modifier = Modifier.align(Alignment.Center)
                )
                else -> SelectionContainer {
                    Text(
                        text = content,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}