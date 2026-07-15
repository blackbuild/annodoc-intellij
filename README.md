# AnnoDoc Support for IntelliJ IDEA

> **Status: pre-alpha.** The first milestone behavior is implemented and tested with both a compiled Java fixture and a real KlumAST-generated API used from Groovy. Release identity and publication remain pending.

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

The current vertical slice:

- participates only when IntelliJ cannot provide ordinary source documentation;
- inspects compiled types, methods, fields, and constructors on demand;
- recognizes `com.blackbuild.annodocimal.annotations.AnnoDoc#value` without a runtime dependency on AnnoDocimal;
- renders ordinary Javadoc markup and representative block tags in Quick Documentation;
- fails silently back to IntelliJ when annotation content is missing, blank, or unreadable;
- works without project configuration, scanning, indexing, startup activity, or background caches;
- supports IntelliJ IDEA 2025.3 and 2026.1 initially; and
- treats plain Java projects as first-class consumers while verifying a real KlumAST-generated API through a separate Groovy compatibility fixture.

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

The first milestone is a tested, locally installable plugin ZIP. Its fixture suite proves the behavior with both:

1. a small compiled AnnoDocimal fixture without attached sources; and
2. a real KlumAST-generated API integration fixture.

The plugin's permanent identity is `com.blackbuild.annodoc.intellij`, published by Blackbuild from [blackbuild/annodoc-intellij](https://github.com/blackbuild/annodoc-intellij).

## Development

The plugin is implemented in Java 21 and currently builds against IntelliJ IDEA 2025.3.6 using the IntelliJ Platform Gradle Plugin. It has no production dependency on Kotlin, Groovy, AnnoDocimal, or KlumAST; only IntelliJ's bundled Java plugin is required. Groovy 4 and KlumAST 3 are confined to the compatibility fixture build and test environment.

Run the focused IntelliJ fixture suite with:

```shell
./gradlew test
```

Build a locally installable plugin ZIP with:

```shell
./gradlew buildPlugin
```
