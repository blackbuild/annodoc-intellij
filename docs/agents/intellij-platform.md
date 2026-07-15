# IntelliJ Platform development

## Intended platform contract

- Java toolchain: 21
- Supported IntelliJ IDEA lines: 2025.3 and 2026.1
- Minimum build: 253
- Required language support: IntelliJ Java/JVM APIs
- Groovy plugin: compatibility fixture only, not a runtime dependency
- Build system: Gradle with the IntelliJ Platform Gradle Plugin 2.x

The current `gradle.properties`, version catalog, and `plugin.xml` still contain abandoned prototype and template settings. Until the cleanup vertical slice aligns them, treat `docs/architecture.md` and this contract as the intended target rather than inferring product decisions from those files.

## Platform API work

Prefer stable public IntelliJ Platform APIs. Before adopting or replacing an API, verify it against the pinned platform SDK, JetBrains source, or current official JetBrains documentation. Do not rely on remembered signatures or examples from a different platform generation.

Keep extension registration and implementation together conceptually: a new provider, action, listener, service, or tool window is incomplete until its `plugin.xml` registration and fixture coverage agree with the intended lifecycle.

Respect IntelliJ threading, read/write action, dumb-mode, PSI lifetime, and disposal rules. Tests that only call a class directly are insufficient when the platform lifecycle or extension lookup is part of the behavior; exercise the registered platform seam where feasible.

Plain Java projects are first-class consumers. Use the Groovy plugin only in compatibility fixtures that prove Quick Documentation from a KlumAST-oriented call site; do not make it a production plugin dependency.
