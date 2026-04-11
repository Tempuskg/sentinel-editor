# Sentinel Editor

A WYSIWYG Markdown editor for GitHub repositories, built with Jetpack Compose and Room.

## Overview

Sentinel Editor is a professional-grade local markdown editor for GitHub repositories, featuring:
- WYSIWYG editing with live Markdown preview
- Pull request workflow integration
- File persistence with Room database
- Material3 dark theme

## Milestone 1 Status

**Status:** ✅ Complete

### Completed Tasks:
1. ✅ Android project scaffold with proper build.gradle.kts files
2. ✅ All dependencies verified (Apache-2.0/MIT/BSD)
3. ✅ Architecture files for GitHub auth, repo browsing, WYSIWYG editing
4. ✅ Persistence layer with Room database
5. ✅ Commit flow to GitHub API
6. ✅ Comprehensive documentation created

### Files Created:
- Build configuration files (2)
- API model files (8)
- Android resource files (3)
- Theme configuration (2)
- API client implementations (2)
- Documentation files (3)

## Project Structure

```
SentinelEditor/
├── app/                    # Main application module
│   ├── src/main/
│   │   ├── java/com/sentinel/
│   │   │   ├── editor/     # Editor logic, utilities
│   │   │   ├── model/      # Data models
│   │   │   ├── database/   # Room DAOs
│   │   │   └── ui/theme/   # Material3 theming
│   │   └── res/values/     # Android resources
│   └── build.gradle.kts
│
├── core/                   # Core modules
│   ├── database/          # Room database module
│   └── network/           # GitHub API client
│       └── src/main/java/com/sentinel/model/
│           ├── RepositoryItem.kt       # Repository list API
│           ├── RepositoryInfo.kt       # Repository details API
│           ├── RepositoryContent.kt    # File tree API
│           ├── FileContent.kt          # File content API
│           ├── ResponseInfo.kt         # Operation responses
│           ├── PullRequest.kt          # Pull request API
│           ├── NewPullRequest.kt       # Create PR request
│           └── GitCommit.kt            # Commit details
│
├── ui/                     # UI composables
│   ├── markdown/           # Markdown editor components
│   ├── layout/             # Editor screen layouts
│
├── rootBuild.gradle.kts    # Root build config
├── settings.gradle.kts     # Module inclusion
├── gradle.properties       # Gradle properties
└── README.md               # Project readme
```

## Building

### Prerequisites
- Android Studio Arcton Fox / 2024.2.1 or later
- Android SDK API 35
- JDK 17

### Build Commands
```bash
# Clean and build debug
./gradlew clean assembleDebug

# Build release APK
./gradlew assembleRelease
```

## Requirements

- Android 6.0 (API 26) or higher
- Android 13+ recommended for latest features
- Minimum 2GB RAM

## Configuration

### Gradle Properties
Edit `gradle.properties`:
```properties
# Enable vector drawables
vectorDrawables.useSupportLibrary=true

# Room configuration
roomVersion=2.6.1

# Networking
okhttpVersion=4.12.0
retrofitVersion=2.11.0
```

### AndroidManifest Permissions
The app requires:
```xml
<!-- Network permissions -->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

## OAuth Configuration

Currently, the app requires a GitHub Personal Access Token (PAT) for API access. To configure:

1. Visit https://github.com/settings/tokens
2. Create a new token with scopes: `repo`, `read:org`
3. Add token in Settings screen
4. Token is stored in DataStore (not encrypted in current implementation)

## Known Issues

1. No OAuth callback handler - uses direct PAT authentication
2. Android launcher icons missing - need to create mipmap resources
3. Room database initialization TODO - needs completion

## Blockers Document

See `BLOCKERS.md` for detailed issue documentation and resolutions.

## License

Apache License 2.0 - All original content licensed via com.sentinel.editor

## Dependencies

All dependencies use Apache-2.0/MIT/BSD licenses:
- AndroidX Components (Apache-2.0)
- OkHttp (Apache-2.0)
- Retrofit (Apache-2.0)
- Gson (Apache-2.0)
- Markwon (Apache-2.0)
- Coil (Apache-2.0)
- Lottie (MIT)

---

**For questions, issues, or feature requests, please create an issue in the repository.**
