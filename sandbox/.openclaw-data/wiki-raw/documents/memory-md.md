# Raw: MEMORY.md

Captured: 2026-04-12
Source: /MEMORY.md (workspace root)
Type: Executive summary / bootstrap context

---

# MEMORY.md

Executive summary for Metatron. Use this as the bootstrap layer for long-term context; use the wiki for detail.

## Core Context

- Metatron is the main agent in the-crucible and coordinates the sandbox's multi-agent workflow.
- Darren prefers directness, clarity, and competent action over filler.
- The deep knowledge base lives in `/sandbox/.openclaw-data/wiki/`; `WIKI.md` defines how to maintain it.

## Sandbox State

- the-crucible is a NemoClaw sandbox with shared GPU-backed local inference.
- Current local provider route is `ollama-local`; active model was recently switched to `qwen3.5:9b-64k`.
- `MEMORY.md` is only the executive summary. For historical detail, entities, analyses, and raw sources, start at `wiki/index.md`.

## Known Agents

- Metatron: orchestrator and delegator.
- jophiel: lead UI/UX architect.
- uriel: human-critical assistant.
- gabriel: auditor and validator.

## Wiki Rules

- Read `WIKI.md` and `wiki/index.md` before answering questions that depend on accumulated knowledge.
- Add durable insights back into the wiki, not just into ephemeral chat responses.
- Treat `wiki-raw/` as immutable after ingest.
- Keep this file compact and curated; the wiki holds the full detail.

## Recovery Note

- After sandbox restore, verify `SOUL.md`, `AGENTS.md`, `MEMORY.md`, and `WIKI.md` still reflect the wiki workflow even if the wiki pages themselves survived.
