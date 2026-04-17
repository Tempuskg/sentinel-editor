package com.sentinel.editor.ui

import android.text.method.LinkMovementMethod
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.sentinel.ui.markdown.MarkwonMarkdownRenderer

private enum class MarkdownEditorMode {
    Edit,
    Preview,
    Split
}

@Composable
fun RichMarkdownEditor(
    content: String,
    onContentChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var editorValue by remember {
        mutableStateOf(TextFieldValue(content))
    }
    var editorMode by remember { mutableStateOf(MarkdownEditorMode.Split) }

    LaunchedEffect(content) {
        if (content != editorValue.text) {
            editorValue = editorValue.copy(
                text = content,
                selection = TextRange(content.length)
            )
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        EditorModeSelector(
            editorMode = editorMode,
            onModeChange = { editorMode = it }
        )

        MarkdownToolbar(
            onBold = { editorValue = editorValue.wrapSelection("**", "**", "bold") },
            onItalic = { editorValue = editorValue.wrapSelection("*", "*", "italic") },
            onHeading = { editorValue = editorValue.prefixSelection("# ", "Heading") },
            onCode = { editorValue = editorValue.wrapSelection("`", "`", "code") },
            onList = { editorValue = editorValue.prefixSelection("- ", "List item") },
            onQuote = { editorValue = editorValue.prefixSelection("> ", "Quote") },
            onLink = { editorValue = editorValue.wrapSelection("[", "](https://)", "label") }
        )

        HorizontalDivider(modifier = Modifier.padding(top = 12.dp, bottom = 12.dp))

        when (editorMode) {
            MarkdownEditorMode.Edit -> {
                EditorPane(
                    value = editorValue,
                    onValueChange = {
                        editorValue = it
                        onContentChange(it.text)
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            MarkdownEditorMode.Preview -> {
                MarkdownPreviewPane(
                    markdown = editorValue.text,
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
                            onValueChange = {
                                editorValue = it
                                onContentChange(it.text)
                            },
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
            selected = editorMode == MarkdownEditorMode.Edit,
            onClick = { onModeChange(MarkdownEditorMode.Edit) },
            label = { Text("Edit") }
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
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        textStyle = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace)
    )
}

@Composable
private fun MarkdownPreviewPane(
    markdown: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .padding(16.dp)
    ) {
        AndroidView(
            factory = { viewContext ->
                TextView(viewContext).apply {
                    movementMethod = LinkMovementMethod.getInstance()
                    setTextIsSelectable(true)
                }
            },
            update = { textView ->
                textView.text = MarkwonMarkdownRenderer.render(context, markdown)
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