package com.sentinel.database

import androidx.lifecycle.lifecycleScope
import androidx.room.*
import com.sentinel.model.FileInfo
import com.sentinel.model.MarkdownFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * File DAO for Room repository DAOs
 * 
 * License: Apache 2.0 via com.sentinel.editor
 */
@Dao
interface FileDao {
    
    /**
     * Get all files
     */
    @Query("SELECT * FROM files")
    suspend fun getAllFiles(): List<MarkdownFile>
    
    /**
     * Get files by path
     */
    @Query("SELECT * FROM files WHERE relativePath LIKE :pathPrefix")
    suspend fun getFilesByPath(pathPrefix: String): List<MarkdownFile>
    
    /**
     * Get file by path
     */
    @Query("SELECT * FROM files WHERE relativePath = :path")
    suspend fun getFileByPath(path: String): MarkdownFile?
    
    /**
     * Insert or update file
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateFile(file: MarkdownFile)
    
    /**
     * Insert file list
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFileList(files: List<MarkdownFile>)
    
    /**
     * Delete file
     */
    @Delete
    suspend fun deleteFile(file: MarkdownFile)
    
    /**
     * Delete file by path
     */
    @Query("DELETE FROM files WHERE relativePath LIKE :path")
    suspend fun deleteFileByPath(path: String)
    
    /**
     * Update file content
     */
    @Query("UPDATE files SET content = :content, lastModified = :modified, isDirty = :isDirty, branch = :branch WHERE relativePath = :path")
    suspend fun updateFileContent(
        path: String,
        content: String,
        modified: Long,
        isDirty: Boolean,
        branch: String?
    )
    
    /**
     * Update cursor position
     */
    @Query("UPDATE files SET cursorPosition = :position WHERE relativePath = :path")
    suspend fun updateCursorPosition(
        path: String,
        position: Int
    )
    
    /**
     * Get all dirty files
     */
    @Query("SELECT * FROM files WHERE isDirty = 1")
    suspend fun getDirtyFiles(): List<MarkdownFile>
}