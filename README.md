# AnnoDoc Support for IntelliJ IDEA

> **Status: pre-alpha prototype.** This repository does not currently contain a working or installable plugin. Its original implementation was abandoned partway through and is being rebuilt from a clarified product definition.

AnnoDoc Support for IntelliJ IDEA is intended to make documentation preserved in compiled JVM classes available through IntelliJ IDEA's normal Quick Documentation experience.

When ordinary source Javadoc is unavailable, the plugin will read generated documentation metadata from supported annotations and render it as a transparent fallback. There is no separate tool window, action, project setup, or background indexing.

<!-- Plugin description -->
AnnoDoc Support for IntelliJ IDEA displays documentation preserved by [AnnoDocimal](https://github.com/blackbuild/anno-docimal) in compiled JVM classes through IntelliJ IDEA's standard Quick Documentation UI.

It is designed as a zero-configuration fallback when source Javadoc is unavailable. [KlumAST](https://github.com/klum-dsl/klum-ast) is the flagship initial use case, but the plugin itself is independent of KlumAST and does not require the Groovy plugin.
<!-- Plugin description end -->

## Why this exists

[AnnoDocimal](https://github.com/blackbuild/anno-docimal) preserves documentation in the runtime-visible `com.blackbuild.annodocimal.annotations.AnnoDoc` annotation. This allows Javadoc and documentation for generated declarations to survive in compiled class files even when usable sources are not attached.

[KlumAST](https://github.com/klum-dsl/klum-ast) generates documented DSL APIs and is the motivating first consumer. KlumAST's DSL-G work addresses completion through IDE-only generated source mirrors; this plugin addresses the separate case of retrieving documentation directly from compiled declarations.

## Intended behavior

The first version will:

- participate only when IntelliJ cannot provide ordinary source documentation;
- inspect compiled types, methods, fields, and constructors on demand;
- recognize `com.blackbuild.annodocimal.annotations.AnnoDoc#value` without a runtime dependency on AnnoDocimal;
- show readable documentation in Quick Documentation, aiming for normal IntelliJ Javadoc rendering fidelity;
- fail silently back to IntelliJ when annotation content is missing, blank, or unreadable;
- work without project configuration, scanning, indexing, startup activity, or background caches;
- support IntelliJ IDEA 2025.3 and 2026.1 initially; and
- treat plain Java projects as first-class consumers while verifying the KlumAST/Groovy use case separately.

## Initial non-goals

The first version will not provide:

- broader KlumAST-specific IDE behavior;
- annotation-authoring or documentation-generation tools;
- user-configurable annotation mappings;
- a public extension API;
- a custom documentation window or action;
- a per-project enable/disable switch;
- Marketplace publication or signing; or
- a compatibility promise for JetBrains products other than IntelliJ IDEA.

The architecture leaves room for additional annotation formats and AnnoDocimal's proposed structured annotation model without implementing those features prematurely. See [Architecture](docs/architecture.md).

## First milestone

The first milestone is a tested, locally installable plugin ZIP. It must prove the behavior with both:

1. a small compiled AnnoDocimal fixture without attached sources; and
2. a real KlumAST-generated API integration fixture.

The final repository name, plugin ID, Java package, and Marketplace identity remain intentionally undecided until that vertical slice works. The current `klum-idea-plugin` repository name is provisional.

## Development

The project will be rebuilt as a Java 21 IntelliJ Platform plugin. Existing template Kotlin code and the abandoned documentation-provider experiment are not considered part of the intended design.

Build and installation instructions will be added once the first working vertical slice exists.
