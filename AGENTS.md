## Agent skills

This is an early-stage IntelliJ Platform plugin. Product intent is still being established: do not infer requirements from IntelliJ template leftovers, placeholder README text, or sample Kotlin classes. Prefer repository evidence for facts and maintainer confirmation for product decisions.

### Project shape

The plugin targets IntelliJ IDEA 2024.3 or newer, runs on Java 21, and depends on the bundled Groovy plugin. Read `docs/agents/intellij-platform.md` before changing extension registration, PSI/documentation behavior, threading, or platform compatibility.

### Domain docs

Use a single-context layout: `CONTEXT.md` at the repository root and ADRs under `docs/adr/`. Create them lazily as decisions emerge. See `docs/agents/domain.md`.

### Testing

Prefer IntelliJ fixture tests through observable platform seams. Run the narrowest relevant test during development and `./gradlew check` before final handoff when practical. See `docs/agents/testing.md`.

### Issues and pull requests

Issues and PRDs live in GitHub Issues. The repository does not yet have agent-workflow labels; do not assume `ready-for-agent` or related labels exist. See `docs/agents/issue-tracker.md`.

For user-visible changes, keep `CHANGELOG.md`, the README plugin description, `plugin.xml`, and compatibility metadata consistent. See `docs/agents/pull-requests.md`.
