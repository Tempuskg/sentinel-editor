# Wiki — Knowledge Base Schema

You maintain a persistent, compounding wiki — a structured directory of interlinked
markdown pages that sits between you and raw sources. The wiki is your long-term
memory. `MEMORY.md` is a compact executive summary distilled from the wiki and
loaded at session start; the full wiki is read on-demand during sessions.

## Paths

| Layer | Path | Ownership |
|---|---|---|
| Wiki pages | `/sandbox/.openclaw-data/wiki/` | You write; user reads |
| Raw sources | `/sandbox/.openclaw-data/wiki-raw/` | Immutable after ingest |

## Directory Layout

```
wiki/
├── index.md          # Content catalog — every page with link and one-line summary
├── log.md            # Chronological append-only activity log
├── overview.md       # Living high-level synthesis
├── entities/         # People, agents, systems, projects
├── concepts/         # Themes, patterns, techniques
├── sources/          # One summary page per ingested source
└── analyses/         # Filed query results, comparisons, investigations

wiki-raw/
├── conversations/    # Saved transcripts and turn reports
├── documents/        # Uploaded articles, papers, notes
├── web/              # Fetched web content
├── observations/     # Sub-agent outputs and cross-agent learnings
└── artifacts/        # Code, configs, system state snapshots
```

## Page Format

Every wiki page uses YAML frontmatter followed by a markdown body:

```markdown
---
title: Page Title
category: entity | concept | source | analysis
created: YYYY-MM-DD
updated: YYYY-MM-DD
sources: [source-slug-1, source-slug-2]
tags: [tag1, tag2]
---

# Page Title

Body text with relative links: [Display Text](../category/slug.md)

## See Also
- [Related Page](../concepts/related.md)
```

## Special Files

**index.md** — Content-oriented catalog organized by category (entities, concepts,
sources, analyses). Each entry: relative link, one-line summary, source count.
Read this first when answering any non-trivial query.

**log.md** — Append-only chronological record. Each entry starts with
`## [YYYY-MM-DD] verb | Title` so it is parseable with grep:
`grep "^\#\# \[" /sandbox/.openclaw-data/wiki/log.md | tail -5`

**overview.md** — Your evolving understanding of the knowledge base as a whole.
Update after significant ingests or when the big picture shifts.

## Operations

### Ingest

When the user provides a new source or asks you to process recent history:

1. Save the raw source to `wiki-raw/{type}/{slug}.md` (immutable after save).
2. Discuss key takeaways with the user.
3. Write a source summary to `wiki/sources/{slug}.md`.
4. Create or update entity pages in `wiki/entities/`.
5. Create or update concept pages in `wiki/concepts/`.
6. Update cross-references on all touched pages.
7. Update `wiki/index.md` with new or changed entries.
8. Append to `wiki/log.md`: `## [YYYY-MM-DD] ingest | Source Title`
9. If the insight is significant, update `wiki/overview.md` and `MEMORY.md`.

### Query

When the user asks a question against accumulated knowledge:

1. Read `wiki/index.md` to locate relevant pages.
2. If needed, search: `grep -rl "term" /sandbox/.openclaw-data/wiki/`
3. Read the relevant wiki pages.
4. Synthesize an answer with citations to wiki page paths.
5. If the answer is substantive, file it as `wiki/analyses/{slug}.md`.
6. Update `wiki/index.md` and append to `wiki/log.md`.

### Lint

Periodic health check (user-triggered or when you notice gaps):

1. Scan for contradictions between pages.
2. Find orphan pages with no inbound links.
3. Identify concepts mentioned but lacking their own page.
4. Check for stale claims superseded by newer sources.
5. Look for missing cross-references.
6. Suggest new sources to investigate or questions to explore.
7. Append report to `wiki/log.md`: `## [YYYY-MM-DD] lint | Health Check`

## Rules

- You own the wiki exclusively. Sub-agents do not read or write it.
- Raw sources are immutable — never modify files in `wiki-raw/` after saving.
- Keep `MEMORY.md` under 4,000 characters as an executive summary of the wiki.
- Use relative links between wiki pages (e.g., `../entities/metatron.md`).
- Slugs are lowercase, hyphenated: `my-topic-name.md`.
- When creating a page, always add it to `index.md` in the same operation.
- When updating a page, bump the `updated` frontmatter field.
