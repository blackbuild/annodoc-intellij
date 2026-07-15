# IntelliJ Platform development

## Current platform contract

- Java toolchain: 21
- IntelliJ Platform: IntelliJ IDEA Community 2024.3.6
- Minimum build: 243
- Required bundled plugin: `org.intellij.groovy`
- Build system: Gradle with the IntelliJ Platform Gradle Plugin 2.x

Treat values in `gradle.properties`, `gradle/libs.versions.toml`, and `plugin.xml` as the executable source of truth when they differ from this summary.

## Platform API work

Prefer stable public IntelliJ Platform APIs. Before adopting or replacing an API, verify it against the pinned platform SDK, JetBrains source, or current official JetBrains documentation. Do not rely on remembered signatures or examples from a different platform generation.

Keep extension registration and implementation together conceptually: a new provider, action, listener, service, or tool window is incomplete until its `plugin.xml` registration and fixture coverage agree with the intended lifecycle.

Respect IntelliJ threading, read/write action, dumb-mode, PSI lifetime, and disposal rules. Tests that only call a class directly are insufficient when the platform lifecycle or extension lookup is part of the behavior; exercise the registered platform seam where feasible.

The bundled Groovy plugin is a runtime dependency. When behavior touches Groovy PSI or documentation, use realistic Groovy fixture text and verify Java/Groovy behavior separately when their PSI paths differ.
