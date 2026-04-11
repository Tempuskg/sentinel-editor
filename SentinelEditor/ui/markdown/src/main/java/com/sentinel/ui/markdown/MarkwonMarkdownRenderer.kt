package com.sentinel.ui.markdown

import android.view.View
import io.noties.markwon.Markwon
import io.noties.markwon.autolink.AutolinkExtension
import io.noties.markwon.gfm.CommonMarkExtension
import io.noties.markwon.html.HtmlExtension
import io.noties.markwon.image.ImageExtension
import io.noties.markwon.image.glide.GlideImageLoader
import io.noties.markwon.spoiler.SpoilerExtension
import io.noties.markwon.smartypants.SmartypantsExtension
import io.noties.markwon.strikethrough.StrikethroughExtension
import io.noties.markwon.superscript.SuperScriptExtension
import io.noties.markwon.subscript.SubScriptExtension
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Markwon markdown converter for rendering markdown files
 * Handles syntax highlighting, tables, images, etc.
 * 
 * Licensed under Apache 2.0 via com.sentinel.editor
 */
class MarkwonMarkdownRenderer : ComponentNode {
    
    private val components = mutableListOf<ComponentNode>()
    private val contentFlow: MutableStateFlow<String> = MutableStateFlow("")
    
    init {
        components.addAll(listOf(
            CommonMarkExtension.create(),
            AutolinkExtension.create(),
            HtmlExtension.create(),
            ImageExtension.create().glideImageLoader(GlideImageLoader()),
            SpoilerExtension.create().create(),
            StrikethroughExtension.create(),
            SmartypantsExtension.create()
        ))
    }
    
    override val component: Markwon
        get() = Markwon.builder()
            .useLogger(io.noties.markwon.core.Logger.DebugLogger())
            .children(*components.toTypedArray())
            .build()
    
    val content: StateFlow<String>
        get() = contentFlow.asStateFlow()
    
    fun updateContent(content: String) {
        contentFlow.value = content
    }
    
    override fun onViewCreated(view: View, savedInstanceState: SaveableStateHolder) {
        // Hook for marking the content
        super.onViewCreated(view, savedInstanceState)
        view.layoutParams.let {
            val width = view.width
            val height = view.height
        }
    }
}