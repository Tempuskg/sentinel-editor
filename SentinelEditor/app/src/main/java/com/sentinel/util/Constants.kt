package com.sentinel.util

object Constants {
    // ==================== App ====================
    const val APP_NAME = "SentinelEditor"
    const val MIN_SDK_VERSION = 26
    const val TARGET_SDK_VERSION = 35
    const val APP_ID = "com.sentinel.editor"
    
    // ==================== GitHub API ====================
    const val GITHUB_BASE_URL = "https://api.github.com"
    const val GITHUB_API_VERSION = "2022-11-28"
    const val GITHUB_USER_AGENT = "SentinelEditor/${BuildConfig.VERSION_NAME} (GitHub Android API v${GITHUB_API_VERSION})"
    
    const val GITHUB_AUTH_HEADER = "Authorization"
    const val GITHUB_AUTH_FORMAT = "Bearer %s" // %s = token
    
    const val GITHUB_RATE_LIMIT_REMAINING = 50
    const val GITHUB_RATE_LIMIT_RESET = 120
    
    const val GITHUB_REPO_LIST_LIMIT = 100
    const val GITHUB_FILE_LIST_LIMIT = 300
    
    // ==================== Room ====================
    const val DATABASE_NAME = "sentinel_database"
    const val DATABASE_VERSION = 1
    
    // ==================== Editor ====================
    const val EDITOR_MIN_LINE_HEIGHT = 16f // dp
    const val EDITOR_MIN_CHAR_WIDTH = 16f // dp
    const val EDITOR_DEFAULT_LINE_HEIGHT = 20f // dp
    const val EDITOR_DEFAULT_CHAR_WIDTH = 20f // dp
    const val EDITOR_FONT_FAMILY = "monospace"
    
    // ==================== File Paths ====================
    const val FILE_PATH_CACHE_NAME = "file_paths_cache"
    const val FILE_CONTENT_CACHE_NAME = "file_contents_cache"
    const val EDITOR_STATE_CACHE_NAME = "editor_states_cache"
    const val LAST_READ_REPO_NAME = "last_read_repo"
    const val SYNC_PROGRESS_CACHE_NAME = "sync_progress_cache"
}