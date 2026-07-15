## Agent skills

This is an early-stage IntelliJ Platform plugin. Product intent is still being established: do not infer requirements from IntelliJ template leftovers, placeholder README text, or sample Kotlin classes. Prefer repository evidence for facts and maintainer confirmation for product decisions.

### Project shape

The project is being rebuilt as a Java 21 plugin for IntelliJ IDEA 2025.3 and 2026.1. It must not require the Groovy plugin at runtime. Read `docs/architecture.md` for the product and design boundaries, and `docs/agents/intellij-platform.md` before changing extension registration, PSI/documentation behavior, threading, or platform compatibility.

### Domain docs

Use a single-context layout: `CONTEXT.md` at the repository root and ADRs under `docs/adr/`. Create them lazily as decisions emerge. See `docs/agents/domain.md`.

### Coding style

Import referenced Java types and use their simple names. Fully qualified names in source are reserved for genuine name conflicts or another documented technical necessity. See `docs/agents/coding-style.md`.

### Testing

Prefer IntelliJ fixture tests through observable platform seams. Run the narrowest relevant test during development and `./gradlew check` before final handoff when practical. See `docs/agents/testing.md`.

### Issue implementation commits

Implement issues on a new, dedicated issue branch using small, reasoned commits. Agents may create commits there without asking. Review and, when necessary, rewrite the local commit sequence before handoff. See `docs/agents/commits.md`.

### Issues and pull requests

Issues and PRDs live in GitHub Issues. The repository does not yet have agent-workflow labels; do not assume `ready-for-agent` or related labels exist. See `docs/agents/issue-tracker.md`.

For user-visible changes, keep `CHANGELOG.md`, the README plugin description, `plugin.xml`, and compatibility metadata consistent. See `docs/agents/pull-requests.md`.
