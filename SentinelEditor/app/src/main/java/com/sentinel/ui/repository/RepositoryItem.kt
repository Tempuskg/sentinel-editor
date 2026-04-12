package com.sentinel.ui.repository

import com.sentinel.model.RepositoryInfo

/**
 * Repository display item for list screens
 * Shows repository name, description, and stats
 *
 * License: Apache 2.0 via com.sentinel.editor
 */
data class RepositoryItem(
    val repository: RepositoryInfo,
    val starred: Boolean = false,
    val forkCount: Int = 0,
    val createdAt: String? = null
) {
    fun getDisplayName(): String = repository.name

    fun getFullName(): String = repository.full_name

    fun getLink(): String = repository.html_url
}
