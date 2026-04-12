---
title: GitHub API Integration
category: concept
created: 2026-04-12
updated: 2026-04-12
sources: [readme-md, sentinel-editor-md, milestone-3-report]
tags: [github, api, oauth, networking]
---

# GitHub API Integration

Sentinel Editor integrates with the GitHub API for repository management, file operations, and pull request workflows.

## Authentication

- **Current:** Personal Access Token (PAT) with scopes `repo`, `read:org`
- **Planned:** OAuth 2.0 with PKCE flow
- **Token storage:** Room database with expiration tracking
- **Token refresh:** Automatic refresh before expiration via `GitHubTokenManager`

### OAuth Flow

1. `OAuthCallbackActivity.kt` handles the callback
2. PKCE flow support implemented
3. Token extracted and stored in Room
4. Success/failure UI composables provided

## Networking Stack

| Component | Technology |
|-----------|-----------|
| HTTP Client | OkHttp 4.12.0 |
| REST Client | Retrofit 2.11.0 |
| Serialization | Gson |
| Auth | `GitHubAuthenticator.kt` |
| Rate Limiting | `RateLimiter.kt` (20-min sliding window) |

## API Models

8 API model classes in `core/network/model/`:

- `RepositoryItem` / `RepositoryInfo` — repository listing and details
- `RepositoryContent` / `FileContent` — file tree and content
- `PullRequest` / `NewPullRequest` — PR management
- `GitCommit` — commit metadata
- `ResponseInfo` — API response wrapper
- `BranchRef` / `BranchProtection` — branch models

## Known Issues

- Rate limiter has a map comparison logic bug
- GitHubClient builder implementation incomplete
- OAuth callback handler relies on PAT fallback

## See Also

- [Sentinel Editor](../entities/sentinel-editor.md)
- [Room Database](room-database.md)
