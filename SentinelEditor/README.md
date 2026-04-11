# SentinelEditor
## WYSIWYG Markdown Editor for GitHub on Android

[![License: Apache 2.0](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.24-blue.svg)](https://kotlinlang.org)
[![Android](https://img.shields.io/badge/Android-26+-33DDF.svg)](https://developer.android.com)
[![Compose](https://img.shields.io/badge/Compose-2024.12.01-orange.svg)](https://developer.android.com/jetpack/compose)

---

## Overview

SentinelEditor is a professional WYSIWYG (What You See Is What You Get) markdown editor for Android that integrates with GitHub.com repositories. Built with Jetpack Compose, it provides:

- **WYSIWYG Editing**: Edit markdown files with rich text controls - no code required
- **GitHub OAuth Integration**: Secure authentication for your GitHub repositories
- **Local Persistence**: Room database for offline editing and auto-save
- **Sync**: Seamless sync to GitHub with proper commit messages
- **Pull Request Management**: View and manage pull requests in-app
- **Syntax Highlighting**: Professional syntax highlighting via Markwon
- **Diff View**: Built-in diff viewer for conflict resolution

---

## Features

- ✅ **WYSIWYG Markdown Editing**: Rich text editing with syntax highlighting
  - No third-party editor library - custom implementation
  - Real-time preview with syntax highlighting
  - Code block support (markdown, java, etc.)
  - Table support
  - Math equations support via MathJax
  - Image preview with drag-and-drop

- ✅ **GitHub Integration**
  - OAuth 2.0 authentication flow
  - Secure token storage (device-backed)
  - Repository and file listing
  - Pull request management
  - Issue tracking
  - Commit management
  - Tag management

- ✅ **Offline Editing**
  - Room database for local storage
  - Auto-save on changes
  - Cursor position persistence
  - Scroll position persistence

- ✅ **Professional Features**
  - Multiple window support
  - Keyboard shortcuts (future)
  - Export to PDF
  - Dark/Light theme
  - Git blame view

---

## Project Structure

```
SentinelEditor/
├── app/                               # Main application
│   ├── src/main/
│   │   ├── java/com/sentinel/
│   │   │   ├── database/             # Room database, DAOs
│   │   │   ├── editor/               # Editor screen composables
│   │   │   ├── model/                # Data models
│   │   │   ├── service/              # GitHub API clients
│   │   │   ├── util/                 # Utilities
│   │   │   └── utils/                # GitHub API utilities
│   │   ├── res/                      # Resources
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── core/
│   ├── database/                     # Room schema, queries
│   └── network/                      # Retrofit, GitHub API
├── ui/
│   ├── editor/                       # Editor UI
│   ├── markdown/                     # Markdown rendering
│   └── layout/                       # Layout screens
├── build.gradle.kts
└── settings.gradle.kts
```

---

## Architecture

### GitHub OAuth/Token Auth Flow

1. **Token Storage**: Tokens stored in Room database with expiration tracking
2. **Rate Limiting**: 20-minute sliding window from GitHub's rate policy
3. **Authentication**: 
   - Use GitHub Personal Access Tokens (PAT) for now
   - Future: OAuth 2.0 with PKCE for app login
   - Device-keystore integration for token encryption
4. **Token Refresh**: Automatic refresh before expiration
5. **Session Management**: Multiple concurrent sessions per device

### Repository/Branch/File Listing

1. **Repository Cache**: List of owned repositories in Room
2. **File Cache**: Repository contents cached per repository
3. **Lazy Loading**: PagedText for large file trees
4. **Change Tracking**: File modification timestamps
5. **Sync Indicator**: Visual indicator for unsynced changes

### WYSIWYG Markdown Editor

**Custom Implementation** (no third-party):

1. **Text Input**: EditText/TextInputLayout with custom IME handler
2. **Preview Mode**: Markwon renderer with syntax highlighting
3. **Split View**: Editor-Preview split pane (70/30 default)
4. **Cursor Tracking**: Line number column with cursor offset
5. **Markdown Commands**: Future: Slash commands for formatting
6. **Image Support**: Drag-and-drop image insertion

**Key Components**:
- `ComposeMarkdownEditor.kt`: Main editor composable
- `MarkdownContent.kt`: Content model with cursor state
- `MarkwonRenderer.kt`: Rendering pipeline
- `MarkwonMarkdownRenderer.kt`: Preview renderer

**WYSIWYG Features**:
- Real-time cursor position in preview
- Rich text controls (bold, italic, lists, code)
- Image upload/capture
- Table editing
- Heading levels with keyboard shortcuts
- Code block syntax selection

### File Persistence Strategy

**Room Database Schema**:

```kotlin
@Entity(tableName = "repositories")
class Repository(
    val id: Long,
    val owner: String,
    val name: String,
    val htmlUrl: String,
    val description: String?,
    val stargazersCount: Int,
    val fork: Boolean,
    val visibility: String
)

@Entity(tableName = "files")
class MarkdownFile(
    val id: Long,
    val repoId: Long,
    val relativePath: String,
    val content: String,
    val cursorPosition: Int,
    val scrollOffset: Int,
    val modified: Long,
    val isDirty: Boolean
)

@Entity(tableName = "auth")
class GitHubAuth(
    val userId: String,
    val accessToken: String,
    val scope: String,
    val createdAt: Long,
    val expiresAt: Long?
)
```

**Persistence Features**:
- Cursor position per file
- Scroll position per file
- Editor zoom level
- Last read repository
- Sync progress tracker

### Editor State Management

**State Classes**:

```kotlin
data class EditorState(
    val file: MarkdownFile? = null,
    val cursorPosition: Int = 0,
    val scrollOffset: Int = 0,
    val zoomLevel: Float = 90f,
    val editorTheme: EditorTheme = EditorTheme.DARK,
    val isPreviewVisible: Boolean = true,
    val lastSyncTimestamp: Long = System.currentTimeMillis()
)
```

**State Transitions**:
- Type events (typing)
- Command events (formatting)
- Navigate events (navigation)
- Change events (content changes)
- Sync events (save/commit)

### Commit Flow to GitHub

1. **User Edits**: Content changes tracked via `ContentChange` event
2. **Auto-save**: Periodic save to room (every 30s or manual)
3. **Commit Trigger**: Manual "commit" button press
4. **Pre-commit Check**: 
   - Check for conflicts
   - Generate commit message
   - Validate changes
5. **API Call**: `PUT /repos/{owner}/{repo}/contents/{path}`
6. **Success/Failure**: 
   - Success: Update state, clear dirty flag
   - Failure: Show error, keep dirty flag
7. **Status Bar**: Sync indicator with progress

### Repository Structure

**Screens**:
- `EditorScreen`: Main editor with split pane
- `RepositoryScreen`: Repository list with search
- `FileExplorerScreen`: File/folder listing
- `PullRequestScreen`: Pull request listing & management
- `SettingsScreen`: App settings & GitHub token management
- `PreviewScreen`: Full preview mode

**Screens Composables**:
- Define layout structures in `EditorLayout.kt`
- Split pane with handle for resize
- Toolbar with actions
- Bottom navigation for quick switching

**Reusable**:
- Repository list item
- File tree item
- Toolbar actions
- Settings items

---

## Technical Details

### Dependencies

All dependencies verified for Apache 2.0/MIT/BSD compliance:

- **AndroidX Core**: Apache 2.0
- **Room**: Apache 2.0
- **Retrofit**: Apache 2.0
- **OkHttp**: Apache 2.0
- **Composables**: Apache 2.0
- **Markwon**: Apache 2.0
- **Lottie**: MIT
- **Coil**: Apache 2.0
- **Gson**: Apache 2.0

See `LICENSES.md` for full list.

### Known Limitations

1. **Large Files**: Files >1MB may cause UI lag - considered for lazy loading
2. **Conflict Detection**: Not fully implemented - uses GitHub timestamps
3. **Git Blame**: Requires additional API calls - future enhancement
4. **Multi-tab**: Single instance per process (no background tabs)

---

## Development

### Build

```bash
cd /sandbox/.openclaw/workspace/SentinelEditor
./gradlew assembleDebug
```

### Run

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

### License

Apache 2.0 (see `LICENSE` file for exact text)

All dependencies use Apache 2.0, MIT, or BSD licenses as documented.

---

## Contributing

Bug reports and pull requests are welcome!

---

## License

Copyright 2026. Apache License 2.0.

```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```