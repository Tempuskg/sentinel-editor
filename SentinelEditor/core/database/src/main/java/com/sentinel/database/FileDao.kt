package com.sentinel.database

import androidx.room.*

/**
 * File DAO for managing markdown files
 * Licensed under Apache 2.0 via com.sentinel.editor
 */
@Dao
interface FileDao {
    
    /**
     * Insert or update a markdown file
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFile(file: MarkdownFile)
    
    /**
     * Get file by path
     */
    @Query("SELECT * FROM files WHERE repo_id = :repoId AND relative_path = :path LIMIT 1")
    suspend fun getFile(repoId: Long, path: String): MarkdownFile?
    
    /**
     * Get all files for a repository
     */
    @Query("SELECT * FROM files WHERE repo_id = :repoId ORDER BY relative_path")
    suspend fun getAllFiles(repoId: Long): List<MarkdownFile>
    
    /**
     * Update editor state (cursor position, scroll offset, etc.)
     */
    @Query("UPDATE files SET cursor_position = :cursorPosition, scroll_offset = :scrollOffset, is_dirty = :isDirty WHERE id = :fileId")
    suspend fun updateEditorState(fileId: Long, cursorPosition: Int, scrollOffset: Int, isDirty: Boolean)
    
    /**
     * Mark file as not dirty (auto-save succeeded)
     */
    @Query("UPDATE files SET is_dirty = 0 WHERE id = :fileId")
    suspend fun markAsSaved(fileId: Long)
    
    /**
     * Delete a file (e.g., from repo)
     */
    @Query("DELETE FROM files WHERE repo_id = :repoId AND relative_path = :path")
    suspend fun deleteFile(repoId: Long, path: String)
    
    /**
     * Get unsaved files
     */
    @Query("SELECT * FROM files WHERE is_dirty = 1 ORDER BY relative_path LIMIT 20")
    suspend fun getUnsavedFiles(): List<MarkdownFile>
    
    /**
     * Get files opened in current view
     */
    @Query("SELECT * FROM files ORDER BY relative_path LIMIT 30")
    suspend fun getOpenFiles(): List<MarkdownFile>
}
