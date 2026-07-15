---
name: implement
description: Implement an approved Klum IDEA plugin issue, PRD, or vertical slice with IntelliJ Platform fixture coverage and repository-appropriate verification. Use when the user asks to implement already-decided work rather than explore or grill the design.
---

# Implement Approved Work

1. Read the complete issue or PRD, `AGENTS.md`, `docs/agents/commits.md`, relevant entries in `CONTEXT.md`, applicable ADRs, and the other agent docs referenced by `AGENTS.md`.
2. State the accepted behavior, out-of-scope behavior, and the public IntelliJ Platform seam to test. Resolve material ambiguity before coding; do not quietly expand the design. Before the first commit, create a new branch dedicated to the issue from the agreed base, or confirm that the current branch was newly created for this issue.
3. Use the `tdd` skill where a reliable fixture seam exists. Work in thin vertical slices: one failing behavioral test, minimal implementation, then the next slice. Commit each completed reasoning slice according to `docs/agents/commits.md`; keep the failing test and the change that makes it pass in the same green commit.
4. Use the narrowest relevant test during iteration. Follow `docs/agents/testing.md` for final verification and `docs/agents/intellij-platform.md` when platform APIs or plugin registration are involved.
5. Update `CHANGELOG.md`, the README plugin description, and plugin metadata only when the accepted behavior makes them relevant.
6. Review the completed diff against the issue-branch base with the `code-review` skill or an equivalent two-axis Standards/Spec review. Commit any fixes, then review and improve the complete commit sequence as required by `docs/agents/commits.md`. Re-run final verification after rewriting history.

On a dedicated issue branch, commit and amend completed reasoning steps without asking. On an existing, shared, or unrelated branch, obtain permission before committing. Do not push, create a pull request, close an issue, or alter issue state unless the user explicitly asks for that action.
