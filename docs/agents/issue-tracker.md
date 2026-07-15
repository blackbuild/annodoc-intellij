# Issue tracker: GitHub

Issues and PRDs live in GitHub Issues for the repository inferred from `git remote -v`. Use `gh` for reads and writes.

Read an issue with its body, comments, and labels before acting on it. Preview proposed issue bodies and decomposition with the maintainer before creating or materially editing issues unless the user explicitly asks for unattended publication.

The repository currently has GitHub's default issue labels plus dependency-update labels, but no `needs-triage`, `needs-info`, `ready-for-agent`, or `ready-for-human` labels. Skills must not silently substitute semantically different labels. Publish without an agent-workflow label or ask the maintainer whether to create/configure the missing vocabulary.

Use native sub-issues and blocking relationships when available. Otherwise, preserve relationships in explicit `Parent` and `Blocked by` sections. Do not close or mutate a parent issue merely because child slices were created.
