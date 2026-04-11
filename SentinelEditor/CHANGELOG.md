# Sentinel Editor - Changelog

All notable changes to Sentinel Editor will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [1.0.0] - 2026-04-11

### Added

**Initial Release**

- **Core Features**
  - WYSIWYG Markdown editor with custom implementation
  - GitHub OAuth 2.0 integration
  - Local persistence with Room database
  - Pull request management
  - File preview with split pane UI
  - Dark theme (primary theme)
  - Repository management
  - Branch navigation

- **Editor**
  - Cursor tracking with sync scroll
  - Syntax highlighting
  - Link support with previews
  - Inline code highlighting
  - Table support
  - Image support ready

- **Persistence**
  - Room database for offline editing
  - Cursor position persistence
  - Scroll position persistence
  - Theme preference storage
  - Sync progress tracking

- **GitHub Integration**
  - Repository listing with pagination
  - File listing and browsing
  - Content fetching
  - Commit viewing
  - Pull request management
  - Branch navigation
  - Token refresh with PKCE
  - Rate limiting to prevent API abuse

- **UI/UX**
  - Material Design 3 styling
  - Dark theme support
  - Split pane editor UI
  - Repository file tree
  - Pull request list
  - Sync indicator

- **Developer Features**
  - Version 2.6.1 Room with KSP
  - Navigation component
  - Compose BOM 2024.12.01
  - ProGuard optimization rules

- **Security**
  - Token encryption with Android KeyStore
  - PKCE OAuth flow
  - Rate limiting
  - Privacy policy configuration

---

### Changed

- None

---

### Deprecated

- None

---

### Removed

- None

---

### Fixed

- None

---

### Security

- Initial implementation of token storage with Android Keystore
- Rate limiting for GitHub API calls
- PKCE OAuth flow for app login

---

## [0.0.x] - Development Versions

All development versions prior to 1.0.0.

---

## Milestone 3: Release-Ready Fixes [2026-04-11]

### Completed

- ✅ Gradle wrapper generated (root/gradlew)
- ✅ Privacy policy URL configured
- ✅ Production signing keystore directory created
- ✅ Resource XML files created (backup_rules.xml, data_extraction_rules.xml)
- ✅ Room database initialization code complete
- ✅ All database DAOs implemented
- ✅ Complete UI composables implemented
- ✅ GitHub API client with all models
- ✅ OAuth callback activity with PKCE
- ✅ Split-pane editor UI implemented
- ✅ Navigation graph with deep linking
- ✅ Screenshots directory structure created
- ✅ Initial release changelog generated
- ✅ App version code set to 1
- ✅ All TODOs completed

### Build Files Created

- `root/settings.gradle.kts`
- `root/rootBuild.gradle.kts`
- `root/gradle.properties`
- `app/build.gradle.kts`
- `core/database/build.gradle.kts`
- `core/network/build.gradle.kts`

---

## [0.1.0] - Development

### Added

- Android project scaffold
- Basic build configuration
- Dependency declarations
- Project structure

---

## License

Apache 2.0 via com.sentinel.editor

All dependencies verified for Apache-2.0/MIT/BSD license compliance.
