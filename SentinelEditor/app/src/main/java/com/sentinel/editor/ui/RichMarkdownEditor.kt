package com.sentinel.editor.ui

import android.text.method.LinkMovementMethod
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import io.noties.markwon.Markwon
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.ext.tasklist.TaskListPlugin
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.linkify.LinkifyPlugin
import kotlinx.coroutines.delay

private enum class MarkdownEditorMode {
    Source,
    WYSIWYG,
    Preview,
    Split
}

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
    val clampedInitialCursorPosition = initialCursorPosition.coerceIn(0, content.length)
    var editorValue by remember(documentKey) {
        mutableStateOf(
            TextFieldValue(
                text = content,
                selection = TextRange(clampedInitialCursorPosition)
            )
        )
    }
    var editorMode by remember { mutableStateOf(MarkdownEditorMode.WYSIWYG) }
    var currentScrollOffset by remember(documentKey) { mutableStateOf(initialScrollOffset) }

    fun applyEditorValue(updatedValue: TextFieldValue) {
        editorValue = updatedValue
        onContentChange(updatedValue.text)
        onEditorPositionChange(updatedValue.selection.max, currentScrollOffset)
    }

    // Debounced scroll-position reporting to ViewModel
    LaunchedEffect(currentScrollOffset) {
        delay(500)
        onEditorPositionChange(editorValue.selection.max, currentScrollOffset)
    }

    LaunchedEffect(documentKey, content, clampedInitialCursorPosition) {
        if (content != editorValue.text) {
            editorValue = TextFieldValue(
                text = content,
                selection = TextRange(clampedInitialCursorPosition)
            )
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        EditorModeSelector(
            editorMode = editorMode,
            onModeChange = { newMode -> editorMode = newMode }
        )

        // Show the markdown command toolbar only when the user is editing source.
        if (editorMode == MarkdownEditorMode.Source || editorMode == MarkdownEditorMode.Split) {
            MarkdownToolbar(
                onBold = {
                    applyEditorValue(editorValue.wrapSelection("**", "**", "bold"))
                },
                onItalic = {
                    applyEditorValue(editorValue.wrapSelection("*", "*", "italic"))
                },
                onHeading = {
                    applyEditorValue(editorValue.prefixSelection("# ", "Heading"))
                },
                onCode = {
                    applyEditorValue(editorValue.wrapSelection("`", "`", "code"))
                },
                onList = {
                    applyEditorValue(editorValue.prefixSelection("- ", "List item"))
                },
                onQuote = {
                    applyEditorValue(editorValue.prefixSelection("> ", "Quote"))
                },
                onLink = {
                    applyEditorValue(editorValue.wrapSelection("[", "](https://)", "label"))
                }
            )
        }

        HorizontalDivider(modifier = Modifier.padding(top = 12.dp, bottom = 12.dp))

        when (editorMode) {
            MarkdownEditorMode.Source -> {
                EditorPane(
                    value = editorValue,
                    onValueChange = ::applyEditorValue,
                    requestInitialFocus = clampedInitialCursorPosition > 0,
                    modifier = Modifier.fillMaxSize()
                )
            }

            MarkdownEditorMode.WYSIWYG -> {
                WysiwygPane(
                    content = editorValue.text,
                    documentKey = documentKey,
                    initialScrollOffset = initialScrollOffset,
                    onScrollChange = { scrollY ->
                        currentScrollOffset = scrollY
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            MarkdownEditorMode.Preview -> {
                MarkdownPreviewPane(
                    markdown = editorValue.text,
                    documentKey = documentKey,
                    initialScrollOffset = initialScrollOffset,
                    onScrollChange = { scrollY ->
                        currentScrollOffset = scrollY
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            MarkdownEditorMode.Split -> {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        modifier = Modifier.weight(1f),
                        tonalElevation = 1.dp,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        EditorPane(
                            value = editorValue,
                            onValueChange = ::applyEditorValue,
                            requestInitialFocus = clampedInitialCursorPosition > 0,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Surface(
                        modifier = Modifier.weight(1f),
                        tonalElevation = 1.dp,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        MarkdownPreviewPane(
                            markdown = editorValue.text,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EditorModeSelector(
    editorMode: MarkdownEditorMode,
    onModeChange: (MarkdownEditorMode) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = editorMode == MarkdownEditorMode.Source,
            onClick = { onModeChange(MarkdownEditorMode.Source) },
            label = { Text("Source") }
        )
        FilterChip(
            selected = editorMode == MarkdownEditorMode.WYSIWYG,
            onClick = { onModeChange(MarkdownEditorMode.WYSIWYG) },
            label = { Text("WYSIWYG") }
        )
        FilterChip(
            selected = editorMode == MarkdownEditorMode.Preview,
            onClick = { onModeChange(MarkdownEditorMode.Preview) },
            label = { Text("Preview") }
        )
        FilterChip(
            selected = editorMode == MarkdownEditorMode.Split,
            onClick = { onModeChange(MarkdownEditorMode.Split) },
            label = { Text("Split") }
        )
    }
}

@Composable
private fun MarkdownToolbar(
    onBold: () -> Unit,
    onItalic: () -> Unit,
    onHeading: () -> Unit,
    onCode: () -> Unit,
    onList: () -> Unit,
    onQuote: () -> Unit,
    onLink: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AssistChip(onClick = onBold, label = { Text("Bold") })
        AssistChip(onClick = onItalic, label = { Text("Italic") })
        AssistChip(onClick = onHeading, label = { Text("H1") })
        AssistChip(onClick = onCode, label = { Text("Code") })
        AssistChip(onClick = onList, label = { Text("List") })
        AssistChip(onClick = onQuote, label = { Text("Quote") })
        AssistChip(onClick = onLink, label = { Text("Link") })
    }
}

@Composable
private fun EditorPane(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    requestInitialFocus: Boolean = false,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.focusRequester(focusRequester),
        textStyle = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace)
    )

    if (requestInitialFocus) {
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}

@Composable
private fun WysiwygPane(
    content: String,
    documentKey: String = "",
    initialScrollOffset: Int = 0,
    onScrollChange: ((Int) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    MarkdownPreviewPane(
        markdown = content,
        documentKey = documentKey,
        initialScrollOffset = initialScrollOffset,
        onScrollChange = onScrollChange,
        modifier = modifier
    )
}

@Composable
private fun MarkdownPreviewPane(
    markdown: String,
    documentKey: String = "",
    initialScrollOffset: Int = 0,
    onScrollChange: ((Int) -> Unit)? = null,
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

    var hasAppliedInitialScroll by remember(documentKey) { mutableStateOf(false) }

    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .padding(16.dp)
    ) {
        AndroidView(
            factory = { viewContext ->
                ScrollView(viewContext).apply {
                    isFillViewport = true
                    val textView = TextView(viewContext).apply {
                        movementMethod = LinkMovementMethod.getInstance()
                        setTextIsSelectable(true)
                    }
                    addView(
                        textView,
                        ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                    )
                    tag = textView
                }
            },
            update = { scrollView ->
                val textView = scrollView.tag as? TextView ?: return@AndroidView
                markwon.setMarkdown(textView, markdown)

                scrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
                    onScrollChange?.invoke(scrollY)
                }

                if (!hasAppliedInitialScroll && initialScrollOffset > 0) {
                    hasAppliedInitialScroll = true
                    scrollView.post {
                        val maxScroll = ((scrollView.getChildAt(0)?.height ?: 0) - scrollView.height).coerceAtLeast(0)
                        scrollView.scrollTo(0, initialScrollOffset.coerceAtMost(maxScroll))
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

private fun TextFieldValue.wrapSelection(
    prefix: String,
    suffix: String,
    placeholder: String
): TextFieldValue {
    val start = selection.min
    val end = selection.max
    val chosenText = text.substring(start, end).ifEmpty { placeholder }
    val replacement = prefix + chosenText + suffix
    val updatedText = text.replaceRange(start, end, replacement)

    return copy(
        text = updatedText,
        selection = if (start == end) {
            TextRange(start + prefix.length, start + prefix.length + chosenText.length)
        } else {
            TextRange(start + replacement.length)
        }
    )
}

private fun TextFieldValue.prefixSelection(
    prefix: String,
    placeholder: String
): TextFieldValue {
    val start = selection.min
    val end = selection.max
    val chosenText = text.substring(start, end)
    val replacement = if (chosenText.isEmpty()) {
        prefix + placeholder
    } else {
        chosenText
            .split('\n')
            .joinToString("\n") { line -> if (line.isBlank()) line else prefix + line }
    }
    val updatedText = text.replaceRange(start, end, replacement)

    return copy(
        text = updatedText,
        selection = if (chosenText.isEmpty()) {
            TextRange(start + prefix.length, start + prefix.length + placeholder.length)
        } else {
            TextRange(start + replacement.length)
        }
    )
}

// --- EditText helpers for WYSIWYG toolbar actions ---

