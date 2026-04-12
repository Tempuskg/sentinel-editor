---
title: The Crucible
category: concept
created: 2026-04-12
updated: 2026-04-12
sources: [memory-md, agents-md]
tags: [infrastructure, sandbox, multi-agent]
---

# The Crucible

The-crucible is a NemoClaw sandbox environment with shared GPU-backed local inference that hosts the multi-agent workflow.

## Architecture

- Shared sandbox with multiple named agents
- GPU-backed local inference via `ollama-local` provider
- Active model: `qwen3.5:9b-64k`
- Metatron is the orchestrator; sub-agents handle specialized tasks

## Agent Hierarchy

| Agent | Role |
|-------|------|
| [Metatron](../entities/metatron.md) | Orchestrator & delegator |
| [Jophiel](../entities/jophiel.md) | Lead UI/UX architect |
| [Uriel](../entities/uriel.md) | Human-critical assistant / developer |
| [Gabriel](../entities/gabriel.md) | Auditor & validator |

## Key Properties

- Sub-agents do not read or write the wiki — only Metatron owns it
- `MEMORY.md` is only loaded in main sessions for security
- Daily memory files in `memory/YYYY-MM-DD.md` provide session continuity

## See Also

- [Multi-Agent Workflow](multi-agent-workflow.md)
