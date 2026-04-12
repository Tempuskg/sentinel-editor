---
title: WYSIWYG Editing
category: concept
created: 2026-04-12
updated: 2026-04-12
sources: [readme-md, sentinel-editor-md]
tags: [editor, markdown, wysiwyg, compose]
---

# WYSIWYG Editing

Sentinel Editor uses a custom WYSIWYG (What You See Is What You Get) markdown editing implementation — no third-party editor library.

## Architecture

- **Input layer:** EditText/TextInputLayout with custom IME handler
- **Rendering:** Markwon renderer with syntax highlighting
- **Layout:** Split pane (70% editor / 30% preview default)
- **State model:** `MarkdownContent.kt` with cursor state

## Features

- Real-time cursor position synced to preview
- Rich text controls: bold, italic, lists, code, headings
- Code block support with language selection
- Table editing
- Image preview with drag-and-drop
- Math equation support via MathJax
- Link support with previews
- Inline code highlighting

## Key Components

| File | Role |
|------|------|
| `ComposeMarkdownEditor.kt` | Main editor composable |
| `MarkdownContent.kt` | Content model with cursor state |
| `MarkwonEditor.kt` | Markwon rendering pipeline |
| `MarkwonMarkdownRenderer.kt` | Preview renderer |
| `EditorToolbar.kt` | Formatting toolbar |
| `MentionRenderer.kt` | @mention handling |

## See Also

- [Jetpack Compose](jetpack-compose.md)
- [Sentinel Editor](../entities/sentinel-editor.md)
