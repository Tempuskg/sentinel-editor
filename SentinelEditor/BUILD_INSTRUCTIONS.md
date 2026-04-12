# Build Instructions for Sentinel Editor

## Requirements

- **Java JDK 21** (installed)
- **Gradle 8.14.3** (installed)
- **Android SDK** (not installed in current environment - install locally)

## Quick Build

### On your local machine with Android Studio/SDK Setup:

```bash
cd SentinelEditor
gradlew assembleDebug
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

### If Android SDK is not installed:

The current environment doesn't have Android SDK installed. You have 2 options:

#### Option 1: Install Android SDK Locally
```bash
# Install command-line tools
sdkmanager "platform-tools" "platforms;android-35" "build-tools;35.0.0"
export ANDROID_HOME=$HOME/android-sdk
export PATH=$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools:$PATH

# Accept licenses
sdkmanager --licenses

# Now build
gradlew assembleDebug
```

#### Option 2: Build in Android Studio
1. Open project in Android Studio
2. Sync Gradle
3. Build › Build APK (Shift+F10)
4. Install on device/emulator

## Current Status ✅

All code changes are ready:
- ✅ Retrofit wiring for GitHub API
- ✅ MainViewModel for state management
- ✅ TokenEntryScreen for PAT input
- ✅ RepositoryListScreen for repo picker
- ✅ FileExplorerScreen for file tree
- ✅ EditorLayout for markdown viewing
- ✅ Navigation wiring complete

The app implements: **Token Entry → Repos List → File Browser → Editor**

## Testing

1. Launch app
2. Enter GitHub Personal Access Token (PAT)
3. See repository list
4. Click a repository to browse files
5. Click files to view content
6. Navigate directories with back button

## OAuth Alternative

For full OAuth flow (if you want GitHub to handle authentication automatically):

1. Create OAuth App at: https://github.com/settings/applications/new
2. Update `AndroidManifest.xml` with your client ID
3. Enable OAuth flow in code
