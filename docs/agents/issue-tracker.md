# Issue tracker: GitHub

Issues and PRDs live in GitHub Issues for the repository inferred from `git remote -v`. Use `gh` for reads and writes.

Read an issue with its body, comments, and labels before acting on it. Preview proposed issue bodies and decomposition with the maintainer before creating or materially editing issues unless the user explicitly asks for unattended publication.

The repository uses GitHub's default and dependency-update labels plus the workflow vocabulary defined in `docs/agents/triage-labels.md`. Apply the label matching the issue's actual triage state; skills must not silently substitute semantically different labels.

Use native sub-issues and blocking relationships when available. Otherwise, preserve relationships in explicit `Parent` and `Blocked by` sections. Do not close or mutate a parent issue merely because child slices were created.
