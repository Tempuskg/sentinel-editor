package com.sentinel.editor

import android.app.Application
import android.util.Base64
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sentinel.editor.utils.EditorSessionManager
import com.sentinel.editor.utils.LastOpenedDocument
import com.sentinel.editor.utils.DeviceAuthException
import com.sentinel.editor.utils.DeviceAuthManager
import com.sentinel.editor.utils.GitHubTokenManager
import com.sentinel.editor.utils.RetrofitProvider
import com.sentinel.model.GitHubAuth
import com.sentinel.service.ContentResponse
import com.sentinel.service.GitHubApiService
import com.sentinel.service.RepositoryResponse
import com.sentinel.service.UpdateFileRequest
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UiState(
    val isCheckingAuth: Boolean = true,
    val isAuthenticated: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val repositories: List<RepositoryResponse> = emptyList(),
    val selectedOwner: String = "",
    val selectedRepo: String = "",
    val currentPath: String = "",
    val files: List<ContentResponse> = emptyList(),
    val selectedFileName: String? = null,
    val selectedFilePath: String? = null,
    val selectedFileSha: String? = null,
    val selectedFileContent: String? = null,
    val selectedFileOriginalContent: String? = null,
    val selectedFileCursorPosition: Int = 0,
    val selectedFileScrollOffset: Int = 0,
    val selectedFileDirty: Boolean = false,
    val isSavingFile: Boolean = false,
    val saveError: String? = null,
    val lastCommitMessage: String? = null,
    val shouldNavigateToRestoredFile: Boolean = false,
    // Device auth flow state
    val deviceAuthUserCode: String? = null,
    val deviceAuthVerificationUri: String? = null,
    val deviceAuthPolling: Boolean = false,
    val deviceAuthError: String? = null
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        /**
         * GitHub OAuth App client ID for device flow.
         * Create one at: https://github.com/settings/applications/new
         * - No client secret needed for the device flow.
         * - Enable "Device Authorization Flow" in the OAuth App settings.
         */
        const val GITHUB_CLIENT_ID = "Ov23liGPBcf5lh0UrL4q"
    }

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    private val context = application.applicationContext
    private var apiService: GitHubApiService? = null
    private var deviceAuthJob: Job? = null
    private var persistEditorPositionJob: Job? = null

    init {
        restoreSession()
    }

    fun connectWithToken(token: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    deviceAuthError = null
                )
            }

            val authError = authenticateWithToken(token = token, persistToken = true)
            if (authError == null) {
                _state.update { it.copy(isLoading = false, isCheckingAuth = false) }
                loadRepositories()
                restoreLastOpenedDocument()
            } else {
                _state.update {
                    it.copy(
                        isLoading = false,
                        isCheckingAuth = false,
                        isAuthenticated = false,
                        error = authError
                    )
                }
            }
        }
    }

    fun loadRepositories() {
        val service = apiService ?: return
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            try {
                val response = service.getUserRepos()
                if (response.isSuccessful) {
                    val repos = response.body() ?: emptyList()
                    _state.update { it.copy(isLoading = false, repositories = repos) }
                } else {
                    _state.update { it.copy(isLoading = false, error = "Failed to load repos: HTTP ${response.code()}") }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message ?: "Network error") }
            }
        }
    }

    fun selectRepository(repo: RepositoryResponse) {
        val owner = repo.fullName.substringBefore('/')
        _state.update {
            it.copy(
                selectedOwner = owner,
                selectedRepo = repo.name,
                currentPath = "",
                files = emptyList(),
                error = null
            )
        }
        loadFiles("")
    }

    fun loadFiles(path: String) {
        val service = apiService ?: return
        val owner = _state.value.selectedOwner
        val repo = _state.value.selectedRepo
        _state.update { it.copy(isLoading = true, error = null, currentPath = path) }
        viewModelScope.launch {
            try {
                val response = if (path.isEmpty()) {
                    service.getRootContents(owner, repo)
                } else {
                    service.getDirContents(owner, repo, path)
                }
                if (response.isSuccessful) {
                    val contents = (response.body() ?: emptyList())
                        .sortedWith(compareBy({ it.type != "dir" }, { it.name.lowercase() }))
                    _state.update { it.copy(isLoading = false, files = contents) }
                } else {
                    _state.update { it.copy(isLoading = false, error = "Failed to load files: HTTP ${response.code()}") }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message ?: "Network error") }
            }
        }
    }

    fun openFile(item: ContentResponse) {
        val owner = _state.value.selectedOwner
        val repo = _state.value.selectedRepo
        openFile(owner = owner, repo = repo, path = item.path, fileName = item.name)
    }

    fun navigateUp() {
        val path = _state.value.currentPath
        if (path.isEmpty()) return
        val parent = path.substringBeforeLast('/', "")
        loadFiles(parent)
    }

    fun clearError() {
        _state.update { it.copy(error = null, saveError = null, deviceAuthError = null) }
    }

    fun updateSelectedFileContent(content: String) {
        _state.update { currentState ->
            val originalContent = currentState.selectedFileOriginalContent
            currentState.copy(
                selectedFileContent = content,
                selectedFileDirty = originalContent != null && content != originalContent,
                saveError = null
            )
        }
    }

    fun updateSelectedFilePosition(cursorPosition: Int, scrollOffset: Int = 0) {
        val currentState = _state.value
        val path = currentState.selectedFilePath ?: return
        val owner = currentState.selectedOwner
        val repo = currentState.selectedRepo
        if (owner.isBlank() || repo.isBlank()) {
            return
        }

        val normalizedCursorPosition = cursorPosition.coerceIn(0, currentState.selectedFileContent?.length ?: 0)
        val normalizedScrollOffset = scrollOffset.coerceAtLeast(0)

        if (currentState.selectedFileCursorPosition == normalizedCursorPosition &&
            currentState.selectedFileScrollOffset == normalizedScrollOffset
        ) {
            return
        }

        _state.update {
            it.copy(
                selectedFileCursorPosition = normalizedCursorPosition,
                selectedFileScrollOffset = normalizedScrollOffset
            )
        }

        persistEditorPositionJob?.cancel()
        persistEditorPositionJob = viewModelScope.launch {
            EditorSessionManager.updateLastOpenedDocumentPosition(
                context = context,
                owner = owner,
                repo = repo,
                path = path,
                cursorPosition = normalizedCursorPosition,
                scrollOffset = normalizedScrollOffset
            )
        }
    }

    fun consumeRestoredFileNavigation() {
        _state.update { it.copy(shouldNavigateToRestoredFile = false) }
    }

    fun saveSelectedFile(commitMessage: String) {
        val service = apiService ?: return
        val currentState = _state.value
        val owner = currentState.selectedOwner
        val repo = currentState.selectedRepo
        val path = currentState.selectedFilePath
        val sha = currentState.selectedFileSha
        val content = currentState.selectedFileContent

        if (owner.isBlank() || repo.isBlank() || path.isNullOrBlank() || sha.isNullOrBlank() || content == null) {
            _state.update { it.copy(saveError = "File metadata is incomplete. Reopen the file and try again.") }
            return
        }

        val message = commitMessage.ifBlank { "Update $path" }
        val encodedContent = Base64.encodeToString(content.toByteArray(Charsets.UTF_8), Base64.NO_WRAP)

        _state.update { it.copy(isSavingFile = true, saveError = null) }

        viewModelScope.launch {
            try {
                val response = service.updateFileContent(
                    owner = owner,
                    repo = repo,
                    path = path,
                    request = UpdateFileRequest(
                        message = message,
                        content = encodedContent,
                        sha = sha
                    )
                )

                if (response.isSuccessful) {
                    val updatedSha = response.body()?.content?.sha ?: sha
                    _state.update {
                        it.copy(
                            isSavingFile = false,
                            selectedFileSha = updatedSha,
                            selectedFileOriginalContent = content,
                            selectedFileDirty = false,
                            saveError = null,
                            lastCommitMessage = message
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            isSavingFile = false,
                            saveError = "Failed to save file: HTTP ${response.code()}"
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isSavingFile = false,
                        saveError = e.message ?: "Unable to save file"
                    )
                }
            }
        }
    }

    // ── Device Authorization Flow ──────────────────────────────

    /**
     * Start device auth: request a device code, then begin polling.
     * The UI navigates to the device-auth screen to show the user code.
     */
    fun startDeviceAuth() {
        deviceAuthJob?.cancel()
        _state.update {
            it.copy(
                isLoading = true,
                error = null,
                deviceAuthUserCode = null,
                deviceAuthVerificationUri = null,
                deviceAuthPolling = false,
                deviceAuthError = null
            )
        }
        deviceAuthJob = viewModelScope.launch {
            try {
                val manager = DeviceAuthManager(GITHUB_CLIENT_ID)
                val codeResponse = manager.requestDeviceCode()

                _state.update {
                    it.copy(
                        isLoading = false,
                        deviceAuthUserCode = codeResponse.userCode,
                        deviceAuthVerificationUri = codeResponse.verificationUri,
                        deviceAuthPolling = true,
                        deviceAuthError = null
                    )
                }

                // Block until user authorizes or code expires
                val accessToken = manager.pollForToken(
                    deviceCode = codeResponse.deviceCode,
                    interval = codeResponse.interval,
                    expiresIn = codeResponse.expiresIn
                )

                // Success — use the token just like a PAT
                val authError = authenticateWithToken(token = accessToken, persistToken = true)
                if (authError == null) {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isCheckingAuth = false,
                            deviceAuthPolling = false,
                            deviceAuthUserCode = null,
                            deviceAuthVerificationUri = null,
                            deviceAuthError = null
                        )
                    }
                    loadRepositories()
                } else {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isCheckingAuth = false,
                            isAuthenticated = false,
                            deviceAuthPolling = false,
                            deviceAuthError = authError
                        )
                    }
                }
            } catch (e: DeviceAuthException) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        deviceAuthPolling = false,
                        deviceAuthError = e.message
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        deviceAuthPolling = false,
                        deviceAuthError = e.message ?: "Network error"
                    )
                }
            }
        }
    }

    fun logout() {
        deviceAuthJob?.cancel()
        deviceAuthJob = null
        persistEditorPositionJob?.cancel()
        persistEditorPositionJob = null
        viewModelScope.launch {
            GitHubTokenManager.logout(context)
            EditorSessionManager.clearLastOpenedDocument(context)
            apiService = null
            _state.value = UiState(isCheckingAuth = false)
        }
    }

    fun cancelDeviceAuth() {
        deviceAuthJob?.cancel()
        deviceAuthJob = null
        _state.update {
            it.copy(
                isLoading = false,
                deviceAuthUserCode = null,
                deviceAuthVerificationUri = null,
                deviceAuthPolling = false,
                deviceAuthError = null
            )
        }
    }

    private fun restoreSession() {
        viewModelScope.launch {
            val savedToken = GitHubTokenManager.getCurrentToken(context)
            if (savedToken == null) {
                _state.update { it.copy(isCheckingAuth = false, isAuthenticated = false) }
                return@launch
            }

            _state.update { it.copy(isLoading = true, error = null) }
            val authError = authenticateWithToken(
                token = savedToken.accessToken,
                persistToken = false
            )

            if (authError == null) {
                _state.update { it.copy(isLoading = false, isCheckingAuth = false) }
                loadRepositories()
                restoreLastOpenedDocument()
            } else {
                GitHubTokenManager.logout(context)
                EditorSessionManager.clearLastOpenedDocument(context)
                _state.update {
                    UiState(
                        isCheckingAuth = false,
                        error = "Saved session is no longer valid. Please sign in again."
                    )
                }
            }
        }
    }

    private suspend fun authenticateWithToken(token: String, persistToken: Boolean): String? {
        val service = RetrofitProvider(token).create()

        return try {
            val response = service.getCurrentUser()
            if (!response.isSuccessful) {
                apiService = null
                return "Failed to authenticate with GitHub: HTTP ${response.code()}"
            }

            val currentUser = response.body()
                ?: return "GitHub did not return user details."

            apiService = service
            if (persistToken) {
                GitHubTokenManager.storeToken(
                    context = context,
                    auth = GitHubAuth(
                        userId = currentUser.login,
                        accessToken = token
                    )
                )
            }

            _state.update {
                it.copy(
                    error = null,
                    deviceAuthError = null,
                    isAuthenticated = true
                )
            }
            null
        } catch (e: Exception) {
            apiService = null
            e.message ?: "Network error"
        }
    }

    private fun restoreLastOpenedDocument() {
        viewModelScope.launch {
            val lastDocument = EditorSessionManager.getLastOpenedDocument(context) ?: return@launch
            openFile(
                owner = lastDocument.owner,
                repo = lastDocument.repo,
                path = lastDocument.path,
                fileName = lastDocument.path.substringAfterLast('/'),
                restoredDocument = lastDocument,
                navigateToEditor = true
            )
        }
    }

    private fun openFile(
        owner: String,
        repo: String,
        path: String,
        fileName: String? = null,
        restoredDocument: LastOpenedDocument? = null,
        navigateToEditor: Boolean = false
    ) {
        val service = apiService ?: return
        if (owner.isBlank() || repo.isBlank() || path.isBlank()) {
            return
        }

        _state.update {
            it.copy(
                isLoading = true,
                error = null,
                selectedOwner = owner,
                selectedRepo = repo,
                currentPath = path.substringBeforeLast('/', "")
            )
        }

        viewModelScope.launch {
            try {
                val response = service.getFileContent(owner, repo, path)
                if (response.isSuccessful) {
                    val file = response.body()
                    val decoded = file?.content?.let { encoded ->
                        val clean = encoded.replace("\n", "").replace("\r", "")
                        String(Base64.decode(clean, Base64.DEFAULT))
                    } ?: ""
                    val resolvedPath = file?.path ?: path
                    val resolvedFileName = file?.name ?: fileName ?: resolvedPath.substringAfterLast('/')
                    val sessionDocument = restoredDocument
                        ?: EditorSessionManager.getLastOpenedDocument(context)
                    val matchesCurrentDocument = sessionDocument?.owner == owner &&
                        sessionDocument.repo == repo &&
                        sessionDocument.path == resolvedPath
                    val resolvedSessionDocument = sessionDocument.takeIf { matchesCurrentDocument }
                    val restoredCursorPosition = if (resolvedSessionDocument != null) {
                        resolvedSessionDocument.cursorPosition.coerceIn(0, decoded.length)
                    } else {
                        0
                    }
                    val restoredScrollOffset = if (resolvedSessionDocument != null) {
                        resolvedSessionDocument.scrollOffset.coerceAtLeast(0)
                    } else {
                        0
                    }

                    _state.update {
                        it.copy(
                            isLoading = false,
                            selectedOwner = owner,
                            selectedRepo = repo,
                            currentPath = resolvedPath.substringBeforeLast('/', ""),
                            selectedFileName = resolvedFileName,
                            selectedFilePath = resolvedPath,
                            selectedFileSha = file?.sha,
                            selectedFileContent = decoded,
                            selectedFileOriginalContent = decoded,
                            selectedFileCursorPosition = restoredCursorPosition,
                            selectedFileScrollOffset = restoredScrollOffset,
                            selectedFileDirty = false,
                            isSavingFile = false,
                            saveError = null,
                            lastCommitMessage = null,
                            shouldNavigateToRestoredFile = navigateToEditor
                        )
                    }

                    EditorSessionManager.storeLastOpenedDocument(
                        context = context,
                        owner = owner,
                        repo = repo,
                        path = resolvedPath,
                        cursorPosition = restoredCursorPosition,
                        scrollOffset = restoredScrollOffset
                    )
                } else {
                    _state.update { it.copy(isLoading = false, error = "Failed to load file: HTTP ${response.code()}") }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message ?: "Network error") }
            }
        }
    }
}
