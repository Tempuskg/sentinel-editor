package com.sentinel.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

/**
 * Repository item view model for list
 * 
 * License: Apache 2.0 via com.sentinel.editor
 */
data class RepositoryItem(
    val name: String,
    val fullName: String,
    val htmlUrl: String,
    val cloneUrl: String,
    val description: String?,
    val fork: Boolean = false,
    val updatedAt: Date,
    val private: Boolean = false,
    val open_issues_count: Int = 0,
    val stargazers_count: Int = 0,
    val watchers_count: Int = 0,
    val language: String? = null,
    val forks_count: Int = 0,
    val empty: Boolean = false,
    val archived: Boolean = false,
    val disabled: Boolean = false,
    val id: Long = 0,
    val defaultBranch: String? = null,
    val masterBranch: String? = null
) {
    val displayPath: String get() = if (name != fullName) name else "$fullName/$name"
    val updatedAtFormatted: String get() {
        val utc = TimeZone.getTimeZone("UTC")
        return if (updatedAt.after(Date(System.currentTimeMillis() - 86400000))) {
            SimpleDateFormat("MMM dd HH:mm").apply { timeZone = utc }.format(updatedAt)
        } else {
            SimpleDateFormat("MMM dd yyyy").apply { timeZone = utc }.format(updatedAt)
        }
    }
    
    val fullNameWithLanguage: String get() = "${language ?: name}  -  $stargazers_count stars"
}