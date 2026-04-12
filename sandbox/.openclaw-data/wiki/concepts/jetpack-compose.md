---
title: Jetpack Compose
category: concept
created: 2026-04-12
updated: 2026-04-12
sources: [readme-md, sentinel-editor-md]
tags: [android, ui, kotlin, compose]
---

# Jetpack Compose

Jetpack Compose is the modern Android UI toolkit used by Sentinel Editor. The project uses Compose BOM 2024.12.01.

## Usage in Sentinel Editor

- **Main editor composable:** `ComposeMarkdownEditor.kt`
- **Split pane layout:** `EditorLayout.kt` (70/30 editor-preview default)
- **Material Design 3:** Full dark theme with `Theme.kt` and `Color.kt`
- **Navigation:** Navigation Component 2.8.4 with deep linking support

## Key Components

| Component | File | Purpose |
|-----------|------|---------|
| Editor | `ComposeMarkdownEditor.kt` | Main WYSIWYG editing surface |
| Preview | `MarkwonMarkdownRenderer.kt` | Live markdown preview |
| Layout | `EditorLayout.kt` | Split pane with file explorer |
| Toolbar | `EditorToolbar.kt` | Formatting controls |
| File Tree | `FileExplorer.kt` | Repository file browser |

## See Also

- [Sentinel Editor](../entities/sentinel-editor.md)
- [WYSIWYG Editing](wysiwyg-editing.md)
- [Room Database](room-database.md)
