---
title: Sentinel Editor
category: entity
created: 2026-04-12
updated: 2026-04-12
sources: [sentinel-editor-md, readme-md, changelog-md, milestone-1-summary, milestone-3-report]
tags: [project, android, markdown, github, jetpack-compose]
---

# Sentinel Editor

A professional WYSIWYG Markdown editor for Android that integrates with GitHub repositories. Built with Jetpack Compose and Room.

## Overview

Sentinel Editor provides rich text editing for markdown files with full GitHub integration — authentication, repository browsing, commit management, and pull request workflows.

## Key Features

- **WYSIWYG Editing:** Custom implementation (no third-party editor library), real-time preview with syntax highlighting
- **GitHub Integration:** OAuth 2.0 with PKCE, repository/file listing, PR management, commit viewing
- **Offline Editing:** Room database for local storage, auto-save, cursor/scroll position persistence
- **Material Design 3:** Dark theme primary, split pane editor UI

## Tech Stack

| Component | Technology | License |
|-----------|-----------|---------|
| UI | Jetpack Compose (BOM 2024.12.01) | Apache-2.0 |
| Database | Room 2.6.1 with KSP | Apache-2.0 |
| Networking | Retrofit 2.11.0 + OkHttp 4.12.0 | Apache-2.0 |
| Markdown | Markwon + CommonMark | Apache-2.0 / BSD-3 |
| Images | Coil | Apache-2.0 |
| Animation | Lottie | MIT |
| Min SDK | API 26 (Android 6.0) | — |
| Target SDK | API 35 | — |

## Project Structure

```
SentinelEditor/
├── app/              # Main application module
├── core/
│   ├── database/     # Room database module
│   └── network/      # GitHub API client (Retrofit)
├── ui/
│   ├── markdown/     # Markdown editor components
│   └── layout/       # Editor screen layouts
└── build configs     # Gradle, settings, properties
```

## Milestone History

### Milestone 1 — Project Scaffold ✅
- Android project structure, build files, dependency verification
- 8 API model classes, GitHub API service, Room DAOs
- Architecture docs for auth, browsing, editing, persistence, commit flow
- **Reviewed by:** [Gabriel](gabriel.md)
- **Built by:** [Uriel](uriel.md)

### Milestone 3 — Release-Ready Fixes ✅
- Gradle wrapper, privacy policy, signing keystore template
- Room database initialization complete (DatabaseInitializer, schema, DAOs, migrations)
- All Room entity models complete (12+ models)
- Main UI composables, editor composables, screen layouts
- OAuth callback activity with PKCE
- Navigation graph with deep linking
- GitHub API client with rate limiting and token management

### Version 1.0.0 (2026-04-11)
- Initial release with all core features
- Custom WYSIWYG editor, GitHub OAuth, Room persistence
- Material3 dark theme, split pane UI

## Known Issues

1. OAuth callback handler — uses direct PAT authentication currently
2. Launcher icons — mipmap resources need creation
3. Rate limiter logic — map comparison issue

## Repository

- **GitHub:** `The-Crucibles-Uriel/sentinel-editor`
- **Branch:** `main`
- **License:** Apache License 2.0

## See Also

- [Jetpack Compose](../concepts/jetpack-compose.md)
- [Room Database](../concepts/room-database.md)
- [GitHub API Integration](../concepts/github-api-integration.md)
- [WYSIWYG Editing](../concepts/wysiwyg-editing.md)
- [Uriel](uriel.md) — primary developer
- [Gabriel](gabriel.md) — reviewer
