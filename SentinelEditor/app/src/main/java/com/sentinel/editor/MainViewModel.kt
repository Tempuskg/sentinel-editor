package com.sentinel.editor

import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sentinel.editor.utils.DeviceAuthException
import com.sentinel.editor.utils.DeviceAuthManager
import com.sentinel.editor.utils.RetrofitProvider
import com.sentinel.service.ContentResponse
import com.sentinel.service.GitHubApiService
import com.sentinel.service.RepositoryResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val repositories: List<RepositoryResponse> = emptyList(),
    val selectedOwner: String = "",
    val selectedRepo: String = "",
    val currentPath: String = "",
    val files: List<ContentResponse> = emptyList(),
    val selectedFileName: String? = null,
    val selectedFileContent: String? = null,
    // Device auth flow state
    val deviceAuthUserCode: String? = null,
    val deviceAuthVerificationUri: String? = null,
    val deviceAuthPolling: Boolean = false,
    val deviceAuthError: String? = null
)

class MainViewModel : ViewModel() {

    companion object {
        /**
         * GitHub OAuth App client ID for device flow.
         * Create one at: https://github.com/settings/applications/new
         * - No client secret needed for the device flow.
         * - Enable "Device Authorization Flow" in the OAuth App settings.
         */
        const val GITHUB_CLIENT_ID = "YOUR_CLIENT_ID_HERE"
    }

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    private var apiService: GitHubApiService? = null
    private var deviceAuthJob: Job? = null

    fun setToken(token: String) {
        apiService = RetrofitProvider(token).create()
        _state.update { it.copy(error = null) }
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
        val service = apiService ?: return
        val owner = _state.value.selectedOwner
        val repo = _state.value.selectedRepo
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            try {
                val response = service.getFileContent(owner, repo, item.path)
                if (response.isSuccessful) {
                    val file = response.body()
                    val decoded = file?.content?.let { encoded ->
                        val clean = encoded.replace("\n", "").replace("\r", "")
                        String(Base64.decode(clean, Base64.DEFAULT))
                    } ?: ""
                    _state.update {
                        it.copy(
                            isLoading = false,
                            selectedFileName = item.name,
                            selectedFileContent = decoded
                        )
                    }
                } else {
                    _state.update { it.copy(isLoading = false, error = "Failed to load file: HTTP ${response.code()}") }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message ?: "Network error") }
            }
        }
    }

    fun navigateUp() {
        val path = _state.value.currentPath
        if (path.isEmpty()) return
        val parent = path.substringBeforeLast('/', "")
        loadFiles(parent)
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
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
                setToken(accessToken)
                _state.update {
                    it.copy(
                        deviceAuthPolling = false,
                        deviceAuthUserCode = null,
                        deviceAuthVerificationUri = null
                    )
                }
                loadRepositories()
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

    /** Returns true when the device auth flow completed successfully (token is set). */
    fun isDeviceAuthComplete(): Boolean {
        val s = _state.value
        return apiService != null && s.deviceAuthUserCode == null && !s.deviceAuthPolling
    }
}
