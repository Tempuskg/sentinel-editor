package com.sentinel.editor.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import java.io.IOException

private val Context.editorSessionDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "editor_session"
)

private val lastOwnerKey = stringPreferencesKey("last_owner")
private val lastRepoKey = stringPreferencesKey("last_repo")
private val lastPathKey = stringPreferencesKey("last_path")
private val lastCursorPositionKey = intPreferencesKey("last_cursor_position")
private val lastScrollOffsetKey = intPreferencesKey("last_scroll_offset")

data class LastOpenedDocument(
    val owner: String,
    val repo: String,
    val path: String,
    val cursorPosition: Int,
    val scrollOffset: Int
)

object EditorSessionManager {

    suspend fun getLastOpenedDocument(context: Context): LastOpenedDocument? {
        val preferences = try {
            context.editorSessionDataStore.data.first()
        } catch (_: IOException) {
            emptyPreferences()
        }

        val owner = preferences[lastOwnerKey].orEmpty()
        val repo = preferences[lastRepoKey].orEmpty()
        val path = preferences[lastPathKey].orEmpty()
        if (owner.isBlank() || repo.isBlank() || path.isBlank()) {
            return null
        }

        return LastOpenedDocument(
            owner = owner,
            repo = repo,
            path = path,
            cursorPosition = preferences[lastCursorPositionKey] ?: 0,
            scrollOffset = preferences[lastScrollOffsetKey] ?: 0
        )
    }

    suspend fun storeLastOpenedDocument(
        context: Context,
        owner: String,
        repo: String,
        path: String,
        cursorPosition: Int,
        scrollOffset: Int
    ) {
        if (owner.isBlank() || repo.isBlank() || path.isBlank()) {
            return
        }

        context.editorSessionDataStore.edit { preferences ->
            preferences[lastOwnerKey] = owner
            preferences[lastRepoKey] = repo
            preferences[lastPathKey] = path
            preferences[lastCursorPositionKey] = cursorPosition.coerceAtLeast(0)
            preferences[lastScrollOffsetKey] = scrollOffset.coerceAtLeast(0)
        }
    }

    suspend fun updateLastOpenedDocumentPosition(
        context: Context,
        owner: String,
        repo: String,
        path: String,
        cursorPosition: Int,
        scrollOffset: Int
    ) {
        val currentDocument = getLastOpenedDocument(context)
        if (currentDocument == null ||
            currentDocument.owner != owner ||
            currentDocument.repo != repo ||
            currentDocument.path != path
        ) {
            storeLastOpenedDocument(context, owner, repo, path, cursorPosition, scrollOffset)
            return
        }

        context.editorSessionDataStore.edit { preferences ->
            preferences[lastCursorPositionKey] = cursorPosition.coerceAtLeast(0)
            preferences[lastScrollOffsetKey] = scrollOffset.coerceAtLeast(0)
        }
    }

    suspend fun clearLastOpenedDocument(context: Context) {
        context.editorSessionDataStore.edit { preferences ->
            preferences.remove(lastOwnerKey)
            preferences.remove(lastRepoKey)
            preferences.remove(lastPathKey)
            preferences.remove(lastCursorPositionKey)
            preferences.remove(lastScrollOffsetKey)
        }
    }
}