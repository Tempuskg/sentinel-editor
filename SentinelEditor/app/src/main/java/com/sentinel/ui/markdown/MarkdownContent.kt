package com.sentinel.ui.markdown

data class MarkdownContent(
    val id: Long = 0,
    val relativePath: String = "",
    val content: String = "",
    val cursorPosition: Int = 0,
    val scrollOffset: Int = 0,
    val zoomLevel: Float = 90f
) {
    val filename: String get() = relativePath.split('/').last().ifBlank { "untitled.md" }
    val directoryPath: String get() = relativePath.substringBeforeLast('/', "")

    fun isChanged(oldContent: String): Boolean = content != oldContent
    fun lineCount(): Int = content.split("\n").size
}
