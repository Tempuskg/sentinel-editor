---
title: Overview
category: overview
created: 2026-04-12
updated: 2026-04-12
---

# Wiki Overview

This wiki documents the Sentinel Editor project and the multi-agent workflow that builds it.

## The Project

**[Sentinel Editor](entities/sentinel-editor.md)** is a professional WYSIWYG Markdown editor for Android that integrates with GitHub repositories. Built with Jetpack Compose, Room, and Retrofit, it provides rich text editing, GitHub OAuth, offline persistence, and pull request management. The project reached v1.0.0 on 2026-04-11 with all core features implemented.

## The Team

The project is built within **[The Crucible](concepts/the-crucible.md)**, a multi-agent sandbox environment:

- **[Metatron](entities/metatron.md)** orchestrates the workflow and maintains this wiki
- **[Uriel](entities/uriel.md)** handles implementation (built the project scaffold and core architecture)
- **[Gabriel](entities/gabriel.md)** audits deliverables and identifies blockers
- **[Jophiel](entities/jophiel.md)** leads UI/UX decisions
- **[Darren](entities/darren.md)** is the human owner directing the project

## Current State

The project has completed three milestones:
1. **M1 — Project Scaffold** ✅ — Android structure, dependencies, architecture docs, API models
2. **M3 — Release-Ready Fixes** ✅ — Room init, OAuth, navigation, all UI composables, v1.0.0 release

Key technical pillars:
- [Jetpack Compose](concepts/jetpack-compose.md) for the UI layer
- [Room Database](concepts/room-database.md) for offline persistence
- [GitHub API Integration](concepts/github-api-integration.md) for repository workflows
- [Custom WYSIWYG Editor](concepts/wysiwyg-editing.md) with no third-party dependencies

## Open Questions

- What happened to Milestone 2? (Not documented in available sources)
- OAuth still falls back to PAT — when will full OAuth 2.0 flow be primary?
- Rate limiter logic bug status — has it been fixed in M3?
- Launcher icons — were mipmap resources ever created?

## Knowledge Gaps

- No source material on Jophiel's specific contributions
- No detailed architecture decision records (ADRs)
- No test coverage documentation
- No user feedback or usage analytics

---

*This overview is updated after significant ingests or when the big picture shifts.*
