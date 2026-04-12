---
title: Room Database
category: concept
created: 2026-04-12
updated: 2026-04-12
sources: [sentinel-editor-md, milestone-3-report]
tags: [android, database, persistence, room]
---

# Room Database

Room is the persistence layer for Sentinel Editor, providing local storage for offline editing with auto-save.

## Schema

Room 2.6.1 with KSP annotation processing.

### Entities

| Entity | Purpose |
|--------|---------|
| `GitHubAuth` | Auth token storage with expiration tracking |
| `MarkdownFile` | File content with cursor/scroll position |
| `Repository` | Cached repository list |

### DAOs

- `AuthDao` — authentication token CRUD
- `FileDao` — file content and metadata CRUD

### Key Files

- `SentinelDatabase.kt` — abstract database class
- `DatabaseInitializer.kt` — initialization logic
- `Converters.kt` — type converters
- `Migrations.kt` — future migration support

## Features

- Offline editing with auto-save
- Cursor position persistence
- Scroll position persistence
- Theme preference storage
- Sync progress tracking

## See Also

- [Sentinel Editor](../entities/sentinel-editor.md)
- [Jetpack Compose](jetpack-compose.md)
