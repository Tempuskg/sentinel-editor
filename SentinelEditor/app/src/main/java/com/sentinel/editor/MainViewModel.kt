package com.sentinel.editor

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.android.github.OAuthCredentials
import com.sentinel.model.RepositoryContent
import com.sentinel.model.RepositoryItem
import com.sentinel.model.RepositoryInfo
import com.sentinel.utils.RetrofitProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class MainViewModel(
    private val retrofitProvider: RetrofitProvider
) : ViewModel() {

    private val _token = MutableStateFlow<String>("")
    val token: StateFlow<String> = _token.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _repositories = MutableStateFlow<List<RepositoryItem>>(emptyList())
    val repositories: StateFlow<List<RepositoryItem>> = _repositories.asStateFlow()

    private val _selectedRepo = MutableStateFlow<RepositoryItem?>(null)
    val selectedRepo: StateFlow<RepositoryItem?> = _selectedRepo.asStateFlow()

    private val _currentPath = MutableStateFlow("")
    val currentPath: StateFlow<String> = _currentPath.asStateFlow()

    private val _files = MutableStateFlow<List<RepositoryContent>>(emptyList())
    val files: StateFlow<List<RepositoryContent>> = _files.asStateFlow()

    private val _selectedFileContent = MutableStateFlow<String?>(null)
    val selectedFileContent: StateFlow<String?> = _selectedFileContent.asStateFlow()

    private val _selectedFileName = MutableStateFlow<String?>(null)
    val selectedFileName: StateFlow<String?> = _selectedFileName.asStateFlow()

    private val apiService: com.sentinel.service.GitHubApiService
        get() = retrofitProvider.create()

    fun setToken(token: String?) {
        token?.let { t ->
            _token.value = t
            _error.value = null
            loadRepositories()
        } ?: run {
            _token.value = ""
        }
    }

    fun loadRepositories() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val repos: List<RepositoryItem> = apiService.listRepositories()
                _repositories.value = repos
            } catch (e: IOException) {
                _error.value = "Failed to load repositories: ${e.message}"
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadFiles(path: String?) {
        _currentPath.value = path ?: ""
        _files.value = emptyList()
        _selectedFileContent.value = null
        _selectedFileName.value = null
        viewModelScope.launch {
            try {
                val owner = _selectedRepo.value?.owner?.login ?: return@launch
                val name = _selectedRepo.value?.name ?: return@launch
                val contents = apiService.listRepositoryContents(owner, name, path)
                _files.value = contents
            } catch (e: IOException) {
                _error.value = "Failed to load files: ${e.message}"
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun openFile(item: RepositoryContent) {
        if (item.type == "dir") {
            loadFiles(item.path)
        } else {
            if (item.type == "file") {
                viewModelScope.launch {
                    try {
                        val owner = _selectedRepo.value?.owner?.login ?: return@launch
                        val name = _selectedRepo.value?.name ?: return@launch
                        val content = apiService.getFileContent(owner, name, item.path)
                        val decoded = java.lang.String(content.content, "UTF-8")
                        _selectedFileContent.value = decoded
                        _selectedFileName.value = item.name
                    } catch (e: IOException) {
                        _error.value = "Failed to load file: ${e.message}"
                    } catch (e: Exception) {
                        _error.value = "Error: ${e.message}"
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    fun goBack() {
        val current = _currentPath.value
        val parent = if (current.isNotEmpty()) {
            current.substringBefore("/").ifEmpty { current.substringBefore("/") }
        } else {
            ""
        }
        loadFilesIfNecessary(parent)
    }

    private fun loadFilesIfNecessary(path: String) {
        if (_currentPath.value != path) {
            loadFiles(path)
        }
    }
}