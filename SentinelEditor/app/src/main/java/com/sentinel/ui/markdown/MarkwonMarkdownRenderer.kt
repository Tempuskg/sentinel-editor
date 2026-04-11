package com.sentinel.ui.markdown

import io.noties.markwon.Markwon

/**
 * Stub markdown renderer. Full implementation lives in the ui:markdown module.
 */
class MarkwonMarkdownRenderer {
    companion object {
        fun render(context: android.content.Context, markdown: String): CharSequence {
            return Markwon.create(context).toMarkdown(markdown)
        }
    }
}
