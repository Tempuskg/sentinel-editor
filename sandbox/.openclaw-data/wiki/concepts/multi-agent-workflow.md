---
title: Multi-Agent Workflow
category: concept
created: 2026-04-12
updated: 2026-04-12
sources: [agents-md, memory-md]
tags: [workflow, agents, coordination]
---

# Multi-Agent Workflow

The multi-agent workflow in the-crucible uses a hierarchical delegation model where Metatron orchestrates specialized sub-agents.

## Pattern

1. Metatron receives requests from Darren
2. Metatron delegates implementation to specialized agents (Uriel for development, Gabriel for auditing, Jophiel for UI/UX)
3. Sub-agents report results back
4. Metatron synthesizes, files to wiki, and responds

## Conventions

- Metatron owns the wiki exclusively
- Sub-agent outputs are stored in `wiki-raw/observations/`
- Cross-agent learnings are ingested into the wiki by Metatron
- Memory is layered: `MEMORY.md` (executive summary) → wiki (full detail) → `wiki-raw/` (immutable sources)

## See Also

- [The Crucible](the-crucible.md)
- [Metatron](../entities/metatron.md)
