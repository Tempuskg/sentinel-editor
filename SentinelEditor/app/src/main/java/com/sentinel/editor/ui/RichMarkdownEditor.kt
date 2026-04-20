package com.sentinel.editor.ui

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.text.Editable
import android.text.InputType
import android.text.Spanned
import android.text.TextPaint
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.BackgroundColorSpan
import android.text.style.BulletSpan
import android.text.style.ForegroundColorSpan
import android.text.style.QuoteSpan
import android.text.style.RelativeSizeSpan
import android.text.style.ReplacementSpan
import android.text.style.StyleSpan
import android.text.style.TypefaceSpan
import android.text.style.UnderlineSpan
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AssistChip
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import io.noties.markwon.Markwon
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.ext.tasklist.TaskListPlugin
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.linkify.LinkifyPlugin

private enum class MarkdownEditorMode {
    Source,
    Wysiwyg,
    Split,
    Preview
}

private val headingPattern = Regex("^(#{1,6})\\s+(.+)$", RegexOption.MULTILINE)
private val blockQuotePattern = Regex("^([ \\t]*)>\\s+(.+)$", RegexOption.MULTILINE)
private val taskListPattern = Regex("^([ \\t]*)([-*])\\s+\\[([ xX])]\\s+(.+)$", RegexOption.MULTILINE)
private val bulletListPattern = Regex("^([ \\t]*)([-*])\\s+(?!\\[[ xX]])(.+)$", RegexOption.MULTILINE)
private val boldPattern = Regex("\\*\\*(.+?)\\*\\*")
private val italicPattern = Regex("(?<!\\*)\\*(?!\\*)(.+?)(?<!\\*)\\*(?!\\*)")
private val inlineCodePattern = Regex("(?<!`)`([^`]+)`(?!`)")
private val linkPattern = Regex("\\[([^\\]]+)]\\(([^)]+)\\)")
private val editorPaneHorizontalPadding = 6.dp
private val editorContentHorizontalPadding = 6.dp
private val editorContentVerticalPadding = 12.dp

@Composable
fun RichMarkdownEditor(
    documentKey: String,
    content: String,
    initialCursorPosition: Int,
    initialScrollOffset: Int,
    onContentChange: (String) -> Unit,
    onEditorPositionChange: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val markwon = remember(context) {
        Markwon.builder(context)
            .usePlugin(StrikethroughPlugin.create())
            .usePlugin(TablePlugin.create(context))
            .usePlugin(TaskListPlugin.create(context))
            .usePlugin(LinkifyPlugin.create())
            .usePlugin(HtmlPlugin.create())
            .build()
    }
    var currentEditText by remember(documentKey) { mutableStateOf<EditText?>(null) }
    var editorMode by remember(documentKey) { mutableStateOf(MarkdownEditorMode.Wysiwyg) }

    Column(modifier = modifier.fillMaxSize()) {
        EditorModeSelector(
            editorMode = editorMode,
            onModeSelected = { editorMode = it }
        )

        if (editorMode != MarkdownEditorMode.Preview) {
            MarkdownToolbar(
                onFormat = { prefix, suffix, placeholder ->
                    currentEditText?.applyWrappedFormat(prefix, suffix, placeholder)
                },
                onPrefix = { prefix, placeholder ->
                    currentEditText?.applyLinePrefix(prefix, placeholder)
                }
            )
        }

        HorizontalDivider(modifier = Modifier.padding(bottom = 4.dp))

        when (editorMode) {
            MarkdownEditorMode.Source -> {
                SourceEditorPane(
                    documentKey = documentKey,
                    content = content,
                    initialCursorPosition = initialCursorPosition,
                    initialScrollOffset = initialScrollOffset,
                    onContentChange = onContentChange,
                    onEditorPositionChange = onEditorPositionChange,
                    onEditTextReady = { currentEditText = it },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = editorPaneHorizontalPadding)
                )
            }

            MarkdownEditorMode.Wysiwyg -> {
                WysiwygEditorPane(
                    documentKey = documentKey,
                    content = content,
                    initialCursorPosition = initialCursorPosition,
                    initialScrollOffset = initialScrollOffset,
                    onContentChange = onContentChange,
                    onEditorPositionChange = onEditorPositionChange,
                    onEditTextReady = { currentEditText = it },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = editorPaneHorizontalPadding)
                )
            }

            MarkdownEditorMode.Split -> {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = editorPaneHorizontalPadding),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(),
                        tonalElevation = 1.dp,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        WysiwygEditorPane(
                            documentKey = documentKey,
                            content = content,
                            initialCursorPosition = initialCursorPosition,
                            initialScrollOffset = initialScrollOffset,
                            onContentChange = onContentChange,
                            onEditorPositionChange = onEditorPositionChange,
                            onEditTextReady = { currentEditText = it },
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .clipToBounds(),
                        tonalElevation = 1.dp,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        MarkdownPreviewPane(
                            markdown = content,
                            initialScrollOffset = initialScrollOffset,
                            lastKnownCursorPosition = initialCursorPosition,
                            markwon = markwon,
                            onEditorPositionChange = onEditorPositionChange,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            MarkdownEditorMode.Preview -> {
                MarkdownPreviewPane(
                    markdown = content,
                    initialScrollOffset = initialScrollOffset,
                    lastKnownCursorPosition = initialCursorPosition,
                    markwon = markwon,
                    onEditorPositionChange = onEditorPositionChange,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = editorPaneHorizontalPadding)
                        .clipToBounds()
                )
            }
        }
    }
}

@Composable
private fun EditorModeSelector(
    editorMode: MarkdownEditorMode,
    onModeSelected: (MarkdownEditorMode) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = editorMode == MarkdownEditorMode.Source,
            onClick = { onModeSelected(MarkdownEditorMode.Source) },
            label = { Text("Source") }
        )
        FilterChip(
            selected = editorMode == MarkdownEditorMode.Wysiwyg,
            onClick = { onModeSelected(MarkdownEditorMode.Wysiwyg) },
            label = { Text("WYSIWYG") }
        )
        FilterChip(
            selected = editorMode == MarkdownEditorMode.Split,
            onClick = { onModeSelected(MarkdownEditorMode.Split) },
            label = { Text("Split") }
        )
        FilterChip(
            selected = editorMode == MarkdownEditorMode.Preview,
            onClick = { onModeSelected(MarkdownEditorMode.Preview) },
            label = { Text("Preview") }
        )
    }
}

@Composable
private fun MarkdownToolbar(
    onFormat: (String, String, String) -> Unit,
    onPrefix: (String, String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AssistChip(onClick = { onFormat("**", "**", "bold") }, label = { Text("Bold") })
        AssistChip(onClick = { onFormat("*", "*", "italic") }, label = { Text("Italic") })
        AssistChip(onClick = { onPrefix("# ", "Heading") }, label = { Text("H1") })
        AssistChip(onClick = { onFormat("`", "`", "code") }, label = { Text("Code") })
        AssistChip(onClick = { onPrefix("- ", "List item") }, label = { Text("List") })
        AssistChip(onClick = { onPrefix("> ", "Quote") }, label = { Text("Quote") })
        AssistChip(onClick = { onFormat("[", "](https://)", "label") }, label = { Text("Link") })
    }
}

@Composable
private fun SourceEditorPane(
    documentKey: String,
    content: String,
    initialCursorPosition: Int,
    initialScrollOffset: Int,
    onContentChange: (String) -> Unit,
    onEditorPositionChange: (Int, Int) -> Unit,
    onEditTextReady: (EditText) -> Unit,
    modifier: Modifier = Modifier
) {
    PlainTextMarkdownEditorPane(
        documentKey = documentKey,
        content = content,
        initialCursorPosition = initialCursorPosition,
        initialScrollOffset = initialScrollOffset,
        onContentChange = onContentChange,
        onEditorPositionChange = onEditorPositionChange,
        onEditTextReady = onEditTextReady,
        modifier = modifier
    )
}

@Composable
private fun WysiwygEditorPane(
    documentKey: String,
    content: String,
    initialCursorPosition: Int,
    initialScrollOffset: Int,
    onContentChange: (String) -> Unit,
    onEditorPositionChange: (Int, Int) -> Unit,
    onEditTextReady: (EditText) -> Unit,
    modifier: Modifier = Modifier
) {
    MarkdownEditorPane(
        documentKey = documentKey,
        content = content,
        initialCursorPosition = initialCursorPosition,
        initialScrollOffset = initialScrollOffset,
        onContentChange = onContentChange,
        onEditorPositionChange = onEditorPositionChange,
        onEditTextReady = onEditTextReady,
        enableInlineRendering = true,
        modifier = modifier
    )
}

@Composable
private fun PlainTextMarkdownEditorPane(
    documentKey: String,
    content: String,
    initialCursorPosition: Int,
    initialScrollOffset: Int,
    onContentChange: (String) -> Unit,
    onEditorPositionChange: (Int, Int) -> Unit,
    onEditTextReady: (EditText) -> Unit,
    modifier: Modifier = Modifier
) {
    MarkdownEditorPane(
        documentKey = documentKey,
        content = content,
        initialCursorPosition = initialCursorPosition,
        initialScrollOffset = initialScrollOffset,
        onContentChange = onContentChange,
        onEditorPositionChange = onEditorPositionChange,
        onEditTextReady = onEditTextReady,
        enableInlineRendering = false,
        modifier = modifier
    )
}

@Composable
private fun MarkdownEditorPane(
    documentKey: String,
    content: String,
    initialCursorPosition: Int,
    initialScrollOffset: Int,
    onContentChange: (String) -> Unit,
    onEditorPositionChange: (Int, Int) -> Unit,
    onEditTextReady: (EditText) -> Unit,
    enableInlineRendering: Boolean,
    modifier: Modifier = Modifier
) {
    val inProgrammaticUpdate = remember(documentKey) { mutableStateOf(false) }
    val contentHorizontalPaddingPx = with(LocalDensity.current) { editorContentHorizontalPadding.roundToPx() }
    val contentVerticalPaddingPx = with(LocalDensity.current) { editorContentVerticalPadding.roundToPx() }

    AndroidView(
        factory = { viewContext ->
            val inputMethodManager = viewContext.getSystemService(InputMethodManager::class.java)

            object : EditText(viewContext) {
                override fun onSelectionChanged(selStart: Int, selEnd: Int) {
                    super.onSelectionChanged(selStart, selEnd)
                    if (!inProgrammaticUpdate.value) {
                        onEditorPositionChange(selStart.coerceAtLeast(0), scrollY.coerceAtLeast(0))
                    }
                }

                override fun onScrollChanged(horizontal: Int, vertical: Int, oldHorizontal: Int, oldVertical: Int) {
                    super.onScrollChanged(horizontal, vertical, oldHorizontal, oldVertical)
                    if (!inProgrammaticUpdate.value) {
                        onEditorPositionChange(selectionStart.coerceAtLeast(0), vertical.coerceAtLeast(0))
                    }
                }
            }.apply {
                setBackgroundColor(Color.TRANSPARENT)
                gravity = Gravity.TOP or Gravity.START
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                setPadding(
                    contentHorizontalPaddingPx,
                    contentVerticalPaddingPx,
                    contentHorizontalPaddingPx,
                    contentVerticalPaddingPx
                )
                inputType = InputType.TYPE_CLASS_TEXT or
                    InputType.TYPE_TEXT_FLAG_MULTI_LINE or
                    InputType.TYPE_TEXT_FLAG_CAP_SENTENCES or
                    InputType.TYPE_TEXT_FLAG_AUTO_CORRECT
                imeOptions = EditorInfo.IME_FLAG_NO_ENTER_ACTION
                setHorizontallyScrolling(false)
                isFocusable = true
                isFocusableInTouchMode = true
                isClickable = true
                isLongClickable = true
                isCursorVisible = true
                showSoftInputOnFocus = true
                overScrollMode = View.OVER_SCROLL_IF_CONTENT_SCROLLS

                val markdownWatcher = if (enableInlineRendering) {
                    InlineMarkdownStylingWatcher(
                        editText = this,
                        onMarkdownChanged = onContentChange
                    ).also { watcher ->
                        addTextChangedListener(watcher)
                        addTextChangedListener(object : TextWatcher {
                            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                            override fun afterTextChanged(s: Editable?) {
                                if (inProgrammaticUpdate.value) return
                                watcher.renderNow()
                            }
                        })
                    }
                } else {
                    addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                        override fun afterTextChanged(s: Editable?) {
                            if (inProgrammaticUpdate.value) return
                            onContentChange(s?.toString().orEmpty())
                        }
                    })
                    null
                }

                setOnFocusChangeListener { focusedView, hasFocus ->
                    if (hasFocus) {
                        inputMethodManager?.showSoftInput(focusedView, InputMethodManager.SHOW_IMPLICIT)
                    }
                }

                inProgrammaticUpdate.value = true
                setText(content)
                val maxPosition = length()
                setSelection(initialCursorPosition.coerceIn(0, maxPosition))
                inProgrammaticUpdate.value = false
                markdownWatcher?.renderNow()

                post {
                    scrollTo(0, initialScrollOffset.coerceAtLeast(0))
                    requestFocus()
                }
            }.also(onEditTextReady)
        },
        update = { editText ->
            onEditTextReady(editText)
            if (!inProgrammaticUpdate.value && content != editText.text.toString()) {
                inProgrammaticUpdate.value = true
                val selectionStart = editText.selectionStart.coerceAtLeast(0)
                val selectionEnd = editText.selectionEnd.coerceAtLeast(0)
                editText.setText(content)
                val maxPosition = editText.length()
                editText.setSelection(
                    selectionStart.coerceIn(0, maxPosition),
                    selectionEnd.coerceIn(0, maxPosition)
                )
                inProgrammaticUpdate.value = false
                if (enableInlineRendering) {
                    InlineMarkdownStylingWatcher.render(editText.editableText)
                }
            }
        },
        modifier = modifier
    )
}

@Composable
private fun MarkdownPreviewPane(
    markdown: String,
    initialScrollOffset: Int,
    lastKnownCursorPosition: Int,
    markwon: Markwon,
    onEditorPositionChange: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val contentHorizontalPaddingPx = with(LocalDensity.current) { editorContentHorizontalPadding.roundToPx() }
    val contentVerticalPaddingPx = with(LocalDensity.current) { editorContentVerticalPadding.roundToPx() }

    AndroidView(
        factory = { viewContext ->
            val textView = TextView(viewContext).apply {
                setTextColor(currentTextColor)
                setPadding(
                    contentHorizontalPaddingPx,
                    contentVerticalPaddingPx,
                    contentHorizontalPaddingPx,
                    contentVerticalPaddingPx
                )
                movementMethod = LinkMovementMethod.getInstance()
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }

            object : ScrollView(viewContext) {
                override fun onScrollChanged(horizontal: Int, vertical: Int, oldHorizontal: Int, oldVertical: Int) {
                    super.onScrollChanged(horizontal, vertical, oldHorizontal, oldVertical)
                    onEditorPositionChange(lastKnownCursorPosition, vertical.coerceAtLeast(0))
                }
            }.apply {
                clipToPadding = true
                addView(textView)
                post {
                    scrollTo(0, initialScrollOffset.coerceAtLeast(0))
                }
                tag = textView
            }
        },
        update = { scrollView ->
            val textView = scrollView.tag as? TextView ?: return@AndroidView
            textView.text = markwon.toMarkdown(markdown)
            scrollView.post {
                scrollView.scrollTo(0, initialScrollOffset.coerceAtLeast(0))
            }
        },
        modifier = modifier
    )
}

private fun EditText.applyWrappedFormat(
    prefix: String,
    suffix: String,
    placeholder: String
) {
    val selectionStart = this.selectionStart.coerceAtLeast(0)
    val selectionEnd = this.selectionEnd.coerceAtLeast(selectionStart)
    val selectedText = text.substring(selectionStart, selectionEnd)
    val insertedText = selectedText.ifEmpty { placeholder }
    val replacement = prefix + insertedText + suffix

    text.replace(selectionStart, selectionEnd, replacement)

    val cursor = if (selectedText.isEmpty()) {
        selectionStart + prefix.length + insertedText.length
    } else {
        selectionStart + replacement.length
    }
    setSelection(cursor.coerceIn(0, length()))
}

private fun EditText.applyLinePrefix(
    prefix: String,
    placeholder: String
) {
    val selectionStart = this.selectionStart.coerceAtLeast(0)
    val selectionEnd = this.selectionEnd.coerceAtLeast(selectionStart)
    val selectedText = text.substring(selectionStart, selectionEnd)

    val replacement = if (selectedText.isEmpty()) {
        prefix + placeholder
    } else {
        selectedText
            .split('\n')
            .joinToString("\n") { line ->
                if (line.isBlank()) line else prefix + line
            }
    }

    text.replace(selectionStart, selectionEnd, replacement)

    val cursor = if (selectedText.isEmpty()) {
        selectionStart + prefix.length + placeholder.length
    } else {
        selectionStart + replacement.length
    }
    setSelection(cursor.coerceIn(0, length()))
}

private class InlineMarkdownStylingWatcher(
    private val editText: EditText,
    private val onMarkdownChanged: (String) -> Unit
) : TextWatcher {
    private var isRendering = false

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        if (isRendering) return
        val editable = s ?: return
        isRendering = true
        render(editable)
        onMarkdownChanged(editable.toString())
        isRendering = false
    }

    fun renderNow() {
        val editable = editText.editableText ?: return
        if (isRendering) return
        isRendering = true
        render(editable)
        isRendering = false
    }

    companion object {
        fun render(editable: Editable) {
            clearInlineMarkdownSpans(editable)
            applyHeadingSpans(editable)
            applyBlockQuoteSpans(editable)
            applyTaskListSpans(editable)
            applyListSpans(editable)
            applyInlineCodeSpans(editable)
            applyLinkSpans(editable)
            applyBoldSpans(editable)
            applyItalicSpans(editable)
        }
    }
}

private fun clearInlineMarkdownSpans(editable: Editable) {
    editable.getSpans(0, editable.length, HiddenSyntaxSpan::class.java).forEach(editable::removeSpan)
    editable.getSpans(0, editable.length, StyleSpan::class.java).forEach(editable::removeSpan)
    editable.getSpans(0, editable.length, RelativeSizeSpan::class.java).forEach(editable::removeSpan)
    editable.getSpans(0, editable.length, ForegroundColorSpan::class.java).forEach(editable::removeSpan)
    editable.getSpans(0, editable.length, BackgroundColorSpan::class.java).forEach(editable::removeSpan)
    editable.getSpans(0, editable.length, UnderlineSpan::class.java).forEach(editable::removeSpan)
    editable.getSpans(0, editable.length, BulletSpan::class.java).forEach(editable::removeSpan)
    editable.getSpans(0, editable.length, QuoteSpan::class.java).forEach(editable::removeSpan)
    editable.getSpans(0, editable.length, TypefaceSpan::class.java).forEach(editable::removeSpan)
    editable.getSpans(0, editable.length, TaskMarkerSpan::class.java).forEach(editable::removeSpan)
}

private fun applyHeadingSpans(editable: Editable) {
    headingPattern.findAll(editable).forEach { match ->
        val markerRange = match.groups[1] ?: return@forEach
        val contentRange = match.groups[2] ?: return@forEach
        editable.setSpan(HiddenSyntaxSpan(), markerRange.range.first, markerRange.range.last + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        val markerEnd = markerRange.range.last + 1
        if (markerEnd < contentRange.range.first) {
            editable.setSpan(HiddenSyntaxSpan(), markerEnd, contentRange.range.first, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        editable.setSpan(StyleSpan(Typeface.BOLD), contentRange.range.first, contentRange.range.last + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        editable.setSpan(
            RelativeSizeSpan(headingScaleForLevel(markerRange.value.length)),
            contentRange.range.first,
            contentRange.range.last + 1,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
}

private fun applyBlockQuoteSpans(editable: Editable) {
    blockQuotePattern.findAll(editable).forEach { match ->
        val indentRange = match.groups[1] ?: return@forEach
        val contentRange = match.groups[2] ?: return@forEach
        editable.setSpan(
            HiddenSyntaxSpan(),
            indentRange.range.first,
            contentRange.range.first,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        editable.setSpan(
            QuoteSpan(Color.parseColor("#90A4AE"), 6, 24),
            contentRange.range.first,
            lineEndFor(editable, contentRange.range.first),
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        editable.setSpan(
            ForegroundColorSpan(Color.parseColor("#546E7A")),
            contentRange.range.first,
            lineEndFor(editable, contentRange.range.first),
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
}

private fun applyTaskListSpans(editable: Editable) {
    taskListPattern.findAll(editable).forEach { match ->
        val indentRange = match.groups[1] ?: return@forEach
        val checkState = match.groups[3]?.value.orEmpty()
        val contentRange = match.groups[4] ?: return@forEach
        editable.setSpan(
            HiddenSyntaxSpan(),
            indentRange.range.first,
            indentRange.range.last + 1,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        editable.setSpan(
            TaskMarkerSpan(isChecked = checkState.equals("x", ignoreCase = true)),
            indentRange.range.last + 1,
            contentRange.range.first,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        editable.setSpan(
            ForegroundColorSpan(Color.parseColor("#37474F")),
            contentRange.range.first,
            lineEndFor(editable, contentRange.range.first),
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
}

private fun applyListSpans(editable: Editable) {
    bulletListPattern.findAll(editable).forEach { match ->
        val indentRange = match.groups[1] ?: return@forEach
        val contentRange = match.groups[3] ?: return@forEach
        val hideEnd = contentRange.range.first
        editable.setSpan(HiddenSyntaxSpan(), indentRange.range.first, hideEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        editable.setSpan(
            BulletSpan(24),
            contentRange.range.first,
            lineEndFor(editable, contentRange.range.first),
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
}

private fun applyInlineCodeSpans(editable: Editable) {
    inlineCodePattern.findAll(editable).forEach { match ->
        val contentRange = match.groups[1] ?: return@forEach
        hideDelimitedRange(
            editable,
            match.range.first,
            contentRange.range.first,
            contentRange.range.last + 1,
            match.range.last + 1
        )
        editable.setSpan(
            TypefaceSpan("monospace"),
            contentRange.range.first,
            contentRange.range.last + 1,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        editable.setSpan(
            BackgroundColorSpan(Color.parseColor("#ECEFF1")),
            contentRange.range.first,
            contentRange.range.last + 1,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        editable.setSpan(
            ForegroundColorSpan(Color.parseColor("#263238")),
            contentRange.range.first,
            contentRange.range.last + 1,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
}

private fun applyBoldSpans(editable: Editable) {
    boldPattern.findAll(editable).forEach { match ->
        val contentRange = match.groups[1] ?: return@forEach
        hideDelimitedRange(editable, match.range.first, contentRange.range.first, contentRange.range.last + 1, match.range.last + 1)
        editable.setSpan(StyleSpan(Typeface.BOLD), contentRange.range.first, contentRange.range.last + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
}

private fun applyItalicSpans(editable: Editable) {
    italicPattern.findAll(editable).forEach { match ->
        val contentRange = match.groups[1] ?: return@forEach
        hideDelimitedRange(editable, match.range.first, contentRange.range.first, contentRange.range.last + 1, match.range.last + 1)
        editable.setSpan(StyleSpan(Typeface.ITALIC), contentRange.range.first, contentRange.range.last + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
}

private fun applyLinkSpans(editable: Editable) {
    linkPattern.findAll(editable).forEach { match ->
        val labelRange = match.groups[1] ?: return@forEach
        val urlRange = match.groups[2] ?: return@forEach
        editable.setSpan(HiddenSyntaxSpan(), match.range.first, labelRange.range.first, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        editable.setSpan(HiddenSyntaxSpan(), labelRange.range.last + 1, urlRange.range.last + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        editable.setSpan(ForegroundColorSpan(Color.parseColor("#1565C0")), labelRange.range.first, labelRange.range.last + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        editable.setSpan(UnderlineSpan(), labelRange.range.first, labelRange.range.last + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
}

private fun hideDelimitedRange(
    editable: Editable,
    fullStart: Int,
    contentStart: Int,
    contentEndExclusive: Int,
    fullEndExclusive: Int
) {
    if (fullStart < contentStart) {
        editable.setSpan(HiddenSyntaxSpan(), fullStart, contentStart, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    if (contentEndExclusive < fullEndExclusive) {
        editable.setSpan(HiddenSyntaxSpan(), contentEndExclusive, fullEndExclusive, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
}

private fun lineEndFor(editable: Editable, start: Int): Int {
    val nextBreak = editable.indexOf('\n', start)
    return if (nextBreak == -1) editable.length else nextBreak
}

private fun CharSequence.indexOf(character: Char, startIndex: Int): Int {
    for (index in startIndex until length) {
        if (this[index] == character) return index
    }
    return -1
}

private fun headingScaleForLevel(level: Int): Float = when (level) {
    1 -> 1.6f
    2 -> 1.4f
    3 -> 1.25f
    4 -> 1.15f
    5 -> 1.05f
    else -> 1f
}

private class HiddenSyntaxSpan : ReplacementSpan() {
    override fun getSize(
        paint: Paint,
        text: CharSequence,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int = 0

    override fun draw(
        canvas: android.graphics.Canvas,
        text: CharSequence,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) = Unit
}

private class TaskMarkerSpan(
    private val isChecked: Boolean
) : ReplacementSpan() {
    private val markerText = if (isChecked) "☑ " else "☐ "

    override fun getSize(
        paint: Paint,
        text: CharSequence,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int = paint.measureText(markerText).toInt()

    override fun draw(
        canvas: android.graphics.Canvas,
        text: CharSequence,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        val previousColor = paint.color
        paint.color = Color.parseColor("#37474F")
        canvas.drawText(markerText, x, y.toFloat(), paint)
        paint.color = previousColor
    }
}

