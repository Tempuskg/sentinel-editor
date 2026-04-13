# Plan: Wire GitHub File Browsing UI Flow (Updated 2026-04-13)

## Current Status Snapshot

The original plan is partially completed and partially stale.

### Already present

- `app/src/main/java/com/sentinel/editor/utils/RetrofitProvider.kt` exists.
- `app/src/main/java/com/sentinel/editor/navigation/NavigationGraph.kt` exists with routes `token`, `repos`, `files`, `editor`.
- `app/src/main/java/com/sentinel/editor/MainActivity.kt` hosts `NavigationGraph`.
- Launcher activity is `com.sentinel.editor.MainActivity` in `AndroidManifest.xml`.

### Still stubbed/broken

- `app/src/main/java/com/sentinel/editor/ui/Screens.kt` contains placeholder composables only.
- `NavigationGraph.kt` has a hardcoded `if (true)` auto-navigation from token to repos.
- `NavigationGraph.kt` shadows the passed `navController` parameter with a new local controller.
- No `MainViewModel` exists to connect token -> repos -> files -> file content.
- File browsing and editor display are not connected to network data.

## Goal

Deliver a working read-only flow:

1. Token entry
2. Repository list
3. Repository file browser (directory navigation)
4. File content viewer

## Architecture Decisions (Reconfirmed)

- Keep manual wiring (no Hilt for this milestone).
- Use one `MainViewModel` for flow state.
- Use app-level models from `app/src/main/java/com/sentinel/model/`.
- Keep read-only behavior first (no edit/save/commit in this milestone).

## Execution Plan

### Phase 1: State and API Wiring

1. Create `app/src/main/java/com/sentinel/editor/MainViewModel.kt`.
2. Store state in a single immutable `UiState` exposed via `StateFlow`.
3. Wire token initialization using `RetrofitProvider(token).create()`.
4. Implement actions:
  - `setToken(token)`
  - `loadRepositories()`
  - `selectRepository(repo)`
  - `loadFiles(path)`
  - `openItem(item)` (dir/file split)
  - `navigateUpDirectory()`
  - `clearError()`
5. Decode file content with `android.util.Base64` when type is file.

### Phase 2: UI Screen Refactor

1. Replace placeholders in `app/src/main/java/com/sentinel/editor/ui/Screens.kt` with real composables.
2. `TokenEntryScreen`:
  - token text field
  - connect button
  - inline error/loading support
3. `RepositoryListScreen`:
  - loading state
  - empty state
  - repo cards/list items
4. `FileExplorerScreen`:
  - current path header
  - up/back action when not at root
  - directory/file rows with icon
5. `EditorLayout`:
  - file name header
  - scrollable, selectable read-only content body

### Phase 3: Navigation Fixes and Wiring

1. Edit `app/src/main/java/com/sentinel/editor/navigation/NavigationGraph.kt`:
  - remove local `rememberNavController()` shadowing
  - use `viewModel()` once at graph scope
  - remove hardcoded `if (true)` navigation
  - navigate based on user actions + state changes
2. Route transitions:
  - token submit success -> `repos`
  - repo click -> `files`
  - file click (for file) -> `editor`
  - editor back -> `files`

### Phase 4: Validation

1. Run `./gradlew assembleDebug` from `SentinelEditor/`.
2. Resolve compile issues.
3. Install debug APK.
4. Manual verify:
  - token screen appears first
  - valid token loads repositories
  - selecting repository loads file list
  - folder navigation works
  - opening file shows decoded content

## Files To Modify

### New

- `app/src/main/java/com/sentinel/editor/MainViewModel.kt`

### Edit

- `app/src/main/java/com/sentinel/editor/ui/Screens.kt`
- `app/src/main/java/com/sentinel/editor/navigation/NavigationGraph.kt`

### Keep as-is (unless blocker found)

- `app/src/main/java/com/sentinel/editor/MainActivity.kt`
- `app/src/main/java/com/sentinel/editor/utils/RetrofitProvider.kt`

## Risks and Notes

- `GitHubApiService.listRepositoryContents` uses `contents/{path}`. Root listing may fail unless endpoint supports empty path handling in this Retrofit declaration.
- There are duplicate model namespaces in the project (`app` and `core/network`); ensure app module imports `com.sentinel.model.*`.
- OAuth metadata in manifest still has placeholders; this plan intentionally keeps PAT-first behavior.

## Definition of Done

1. No placeholder UI remains in file-browsing flow.
2. Navigation is user-driven (no hardcoded route jumps).
3. Repositories and file trees load from GitHub API.
4. File content opens in read-only viewer.
5. `assembleDebug` succeeds.
