# Plan: Wire GitHub File Browsing UI Flow

## TL;DR

The app shows "No file selected" because the entire UI→API→data pipeline is disconnected. Models, API interfaces, and DAOs all exist but nothing is wired together — no ViewModels, no Retrofit instance, no real navigation routes. This plan adds the minimum viable flow: **Token Entry → Repo List → File Browser → Editor** in small, isolated steps a qwen3.5:9b model can execute sequentially.

## Architecture Decisions

- **No Hilt/DI:** Manual dependency wiring via `SentinelApplication` companion — keeps it simple for a small model
- **Single shared ViewModel:** One `MainViewModel` holds all state (token, repos, files, selected file) — avoids complex scoped VM patterns
- **Retrofit built once** in Application class, passed to ViewModel
- **No Repository pattern layer:** ViewModel calls API service directly — avoid abstraction overhead
- **Navigation:** 4 routes in existing `NavigatedGraph.kt` — token, repos, files, editor

## Steps

### Phase 1: Retrofit Wiring (1 file new, 1 file edit)

**Step 1.** Create `RetrofitProvider.kt` — a simple object that builds `OkHttpClient` + `Retrofit` + `GitHubApiService` given a bearer token.
- **File:** `app/src/main/java/com/sentinel/editor/utils/RetrofitProvider.kt` (NEW)
- **Pattern:** Object with `fun create(token: String): GitHubApiService` — builds OkHttpClient with auth interceptor, Retrofit with Gson, returns service
- **Reference:** Existing `GitHubApiService.kt` at `core/network/src/main/java/com/sentinel/service/GitHubApiService.kt` for the interface
- **Reference:** `app/build.gradle.kts` already has Retrofit 2.11.0, OkHttp 4.12.0, Gson
- **Base URL:** `https://api.github.com/`

### Phase 2: ViewModel (1 file new)

**Step 2.** Create `MainViewModel.kt` — single ViewModel holding all screen state.
- **File:** `app/src/main/java/com/sentinel/editor/MainViewModel.kt` (NEW)
- **State fields (StateFlow):**
  - `token: String` — GitHub PAT
  - `isLoading: Boolean`
  - `error: String?`
  - `repositories: List<RepositoryItem>` — from `com.sentinel.model.RepositoryItem` (app model)
  - `selectedRepo: RepositoryItem?`
  - `files: List<RepositoryContent>` — from `com.sentinel.model.RepositoryContent` (app model)
  - `currentPath: String` — current directory path for navigation
  - `selectedFileContent: String?` — decoded file content for editor
  - `selectedFileName: String?`
- **Functions:**
  - `setToken(token: String)` — stores token, builds API service via RetrofitProvider
  - `loadRepositories()` — calls `apiService.listRepositories()`, updates state
  - `selectRepository(repo: RepositoryItem)` — stores selected, calls `loadFiles("")`
  - `loadFiles(path: String)` — calls `apiService.listRepositoryContents(owner, repo, path)`, updates state
  - `openFile(item: RepositoryContent)` — if dir, calls `loadFiles(item.path)`; if file, calls `apiService.getFileContent(...)`, base64 decodes, updates selectedFileContent
  - `goBack()` — navigates up one directory level (parent of currentPath)
- **Error handling:** Wrap API calls in try/catch, set `error` state
- **Note for executor:** Use `viewModelScope.launch` for coroutine calls. Use `android.util.Base64` to decode file content. The RepositoryItem model in `app/src/main/java/com/sentinel/model/RepositoryItem.kt` has `owner` as an `Owner` object — check the actual field structure and use `repo.owner.login` or similar.

### Phase 3: UI Screens (3 files edited, 1 file new)

**Step 3.** Create `TokenEntryScreen.kt` — simple screen with TextField for PAT and "Connect" button.
- **File:** `app/src/main/java/com/sentinel/editor/ui/TokenEntryScreen.kt` (NEW)
- **Composable:** `TokenEntryScreen(onTokenSubmitted: (String) -> Unit)`
- **UI:** Column with app title, TextField (password visual transformation), Button("Connect")
- **Reference:** String resources already exist: `R.string.app_name`, `R.string.save_button`

**Step 4.** Edit `FileExplorer.kt` — replace stub with working file list.
- **File:** `app/src/main/java/com/sentinel/editor/ui/FileExplorer.kt` (EDIT)
- **Composable:** `FileExplorerScreen(files: List<RepositoryContent>, currentPath: String, onItemClick: (RepositoryContent) -> Unit, onBackClick: () -> Unit)`
- **UI:** Column with:
  - Top bar showing `currentPath` with back button (if path != "")
  - LazyColumn of items showing icon (folder/file), name, click handler
  - Use `Icon` with `Icons.Default.Folder` or `Icons.Default.Description` based on `item.type == "dir"`
- **Reference:** `RepositoryContent` model has `name`, `path`, `type`, `size` fields

**Step 5.** Edit `EditorLayout.kt` — replace stub with actual editor display.
- **File:** `app/src/main/java/com/sentinel/editor/ui/EditorLayout.kt` (EDIT)
- **Change:** Replace `Text(file?.content ?: "No file selected")` with:
  - Top bar showing file name
  - Scrollable `SelectionContainer` + `Text` showing the file content with monospace font
  - Keep it read-only for this phase — editing comes later
- **Accept parameters:** `fileName: String?, content: String?, onBack: () -> Unit`

### Phase 4: Repository Picker (1 file new)

**Step 6.** Create `RepositoryListScreen.kt` — screen showing user's repos.
- **File:** `app/src/main/java/com/sentinel/editor/ui/RepositoryListScreen.kt` (NEW)
- **Composable:** `RepositoryListScreen(repos: List<RepositoryItem>, isLoading: Boolean, onRepoClick: (RepositoryItem) -> Unit)`
- **UI:** 
  - If loading: `CircularProgressIndicator`
  - LazyColumn of `Card` items showing repo name, description, language, stars
  - Click navigates to file explorer
- **Reference:** Existing `ui/markdown/RepositoryItem.kt` composable (only shows name) — this replaces it with a richer card

### Phase 5: Navigation Wiring (2 files edited)

**Step 7.** Edit `NavigatedGraph.kt` — replace single "home" route with 4-screen flow.
- **File:** `app/src/main/java/com/sentinel/editor/navigation/NavigatedGraph.kt` (EDIT)
- **Routes:**
  - `"token"` → `TokenEntryScreen` (start destination)
  - `"repos"` → `RepositoryListScreen`
  - `"files"` → `FileExplorerScreen`
  - `"editor"` → `EditorLayout`
- **ViewModel:** Create `MainViewModel` via `viewModel()` at NavHost level, pass to all screens
- **Navigation:** After token submit → navigate to "repos"; after repo click → navigate to "files"; after file click in VM → navigate to "editor"
- **Reference:** App already has `navigation-compose:2.8.4` dependency

**Step 8.** Edit `MainActivity.kt` — make sure it hosts NavigatedGraph properly.
- **File:** `app/src/main/java/com/sentinel/editor/MainActivity.kt` (EDIT) 
- **Change:** `setContent { SentinelEditorTheme { NavigatedGraph() } }` — ensure the theme wraps the nav graph
- **Reference:** Theme at `app/src/main/java/com/sentinel/ui/theme/Theme.kt`
- **Note:** There are TWO MainActivity files — one at `app/src/main/java/com/sentinel/MainActivity.kt` and one at `app/src/main/java/com/sentinel/editor/MainActivity.kt`. The AndroidManifest launcher activity is the one that matters — check which is declared as launcher and edit that one.

### Phase 6: Verification

**Step 9.** Build and verify.
- Run `./gradlew assembleDebug` from `SentinelEditor/`
- Fix any compilation errors
- If build succeeds, install on device/emulator: `adb install -r app/build/outputs/apk/debug/app-debug.apk`

**Step 10.** Manual test flow.
- Launch app → see token entry screen
- Enter a GitHub PAT → tap Connect → see repository list
- Tap a repository → see file list
- Navigate directories → tap a file → see content displayed

## Relevant Files

### To Create (4 new files)
- `app/src/main/java/com/sentinel/editor/utils/RetrofitProvider.kt` — Retrofit + OkHttp builder
- `app/src/main/java/com/sentinel/editor/MainViewModel.kt` — Central state management
- `app/src/main/java/com/sentinel/editor/ui/TokenEntryScreen.kt` — PAT entry screen
- `app/src/main/java/com/sentinel/editor/ui/RepositoryListScreen.kt` — Repo picker screen

### To Edit (4 existing files)
- `app/src/main/java/com/sentinel/editor/ui/FileExplorer.kt` — Replace stub with file list
- `app/src/main/java/com/sentinel/editor/ui/EditorLayout.kt` — Replace "No file selected" with content viewer
- `app/src/main/java/com/sentinel/editor/navigation/NavigatedGraph.kt` — Add 4-screen nav flow
- `app/src/main/java/com/sentinel/editor/MainActivity.kt` (or `com/sentinel/MainActivity.kt`) — Wire theme + nav graph

### Reference (read-only, for patterns)
- `core/network/src/main/java/com/sentinel/service/GitHubApiService.kt` — API endpoint definitions
- `app/src/main/java/com/sentinel/model/RepositoryItem.kt` — Repo model fields
- `app/src/main/java/com/sentinel/model/RepositoryContent.kt` — File/dir model fields
- `app/src/main/java/com/sentinel/model/FileContent.kt` — File content model (has base64 `content` field)
- `app/src/main/java/com/sentinel/ui/theme/Theme.kt` — Theme composable name
- `app/src/main/res/values/strings.xml` — Available string resources

## Verification

1. `./gradlew assembleDebug` succeeds with zero errors
2. App launches to token entry screen (not "No file selected")
3. Entering a valid PAT shows repository list
4. Tapping a repo shows its file tree
5. Navigating into directories works (and back button works)
6. Tapping a markdown file shows its content in the editor view

## Decisions

- **Read-only viewer first:** Editor shows file content but doesn't support editing yet — editing/save/commit is a separate follow-up
- **No offline caching:** Files fetched from API every time — Room persistence deferred
- **No Hilt:** Manual DI to keep step count low and avoid Gradle plugin complexity
- **Single ViewModel:** Simpler than scoped ViewModels, fine for this flow
- **PAT-only auth:** No OAuth flow — matches current app state (PAT is already the documented approach)
- **Excluded:** PR management, branch switching, settings screen, commit flow, markdown rendering — all deferred

## Executor Notes (for qwen3.5:9b)

- Execute steps **sequentially** — each depends on prior steps
- Steps 3-6 (UI screens) can be done in any order, but must all complete before Step 7 (navigation wiring)
- Each step is a single file create or edit — keep changes focused
- After Step 7, run the build (Step 9) to catch issues early
- If build fails, read the error, fix the specific file, retry
- **Watch for duplicate model classes** — the app has models in both `app/src/main/java/com/sentinel/model/` AND `core/network/src/main/java/com/sentinel/model/`. Use the app-level models (they're what the app module can import directly)
- **Import the correct RepositoryContent** — use `com.sentinel.model.RepositoryContent` from the app module
