---
name: handoff
description: Compact the current Klum IDEA plugin conversation into a temporary handoff document for another agent or a fresh task. Use when the user asks to continue work in another session or context is becoming unwieldy.
---

Write a concise handoff document in the operating system's temporary directory, not in the repository.

Include the objective, current state, confirmed decisions, unresolved decisions, verification already run, relevant dirty-worktree caveats, and the exact next action. Add a suggested-skills section. Reference existing issues, ADRs, plans, commits, diffs, and repository files instead of duplicating them.

Redact credentials, tokens, personal data, and unrelated repository details. If the user describes the next session's focus, tailor the handoff to that focus.
