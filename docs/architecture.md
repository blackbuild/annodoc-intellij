# Architecture

## Purpose

The plugin exposes documentation carried in compiled-class annotations through IntelliJ IDEA's standard Quick Documentation experience. It is a fallback for declarations whose ordinary source documentation is unavailable, not a replacement for IntelliJ's native Javadoc or Groovydoc handling.

The runtime path must be zero-configuration and on-demand. Projects without supported documentation annotations incur no scanning, indexing, startup work, or background caching.

## Architectural drivers

### AnnoDocimal

[AnnoDocimal](https://github.com/blackbuild/anno-docimal) preserves source Javadoc in the runtime-visible `com.blackbuild.annodocimal.annotations.AnnoDoc` annotation and supports libraries that generate documented declarations during compilation. It is the primary metadata producer for the first version, but the plugin must not depend on AnnoDocimal at runtime.

The initial annotation contract is:

```text
annotation: com.blackbuild.annodocimal.annotations.AnnoDoc
member:     value
content:    raw Javadoc text
targets:    type, method, field, constructor
```

[AnnoDocimal issue #30](https://github.com/blackbuild/anno-docimal/issues/30) proposes a future structured representation with nested annotations for paragraphs, parameters, return values, exceptions, and tags, plus explicit format metadata. Names and schema are not settled. The plugin therefore separates extraction from rendering, but does not implement or predict the V2 model.

### KlumAST

[KlumAST](https://github.com/klum-dsl/klum-ast) is a Groovy AST-transformation framework that generates documented DSL factories and Builders. It is the flagship initial use case and must have a real integration fixture.

KlumAST's DSL-G work supplies IntelliJ completion through IDE-only generated source mirrors. This plugin solves a distinct concern: recovering AnnoDoc documentation from compiled declarations when usable source documentation is absent. No KlumAST-specific behavior or runtime dependency belongs in the first version.

## Runtime flow

1. IntelliJ requests documentation for a resolved declaration.
2. Native source Javadoc or Groovydoc retains precedence.
3. If native documentation is unavailable, the plugin considers only the resolved compiled declaration.
4. A documentation extractor checks a built-in documentation mapping and reads its configured annotation member.
5. A renderer converts the extracted raw Javadoc into IntelliJ documentation content.
6. IntelliJ presents the result in its normal Quick Documentation UI.

If the declaration is unsupported, the annotation or member is absent, or its value is blank, non-constant, or unreadable, the plugin yields without presenting an error. Debug logging is acceptable; user-visible failures are not.

## Internal seams

The first implementation should keep three responsibilities distinct:

- **IntelliJ presentation** integrates with Quick Documentation and decides whether fallback documentation may participate.
- **Documentation extraction** recognizes compiled-class annotation structures and produces documentation content. The V1 extractor uses one fixed annotation/member mapping; a future structured AnnoDoc format can add a different extractor.
- **Documentation rendering** turns extracted raw Javadoc into IntelliJ's documentation representation. Readable output is the baseline; parity with normal Javadoc rendering is the goal.

The V1 documentation mapping contains only an annotation FQN and member name. Do not add public configuration, a format taxonomy, or a generic structured-document model until a concrete second format requires them.

## Scope boundaries

- Support compiled types, methods, fields, and constructors.
- Do not read manually authored source annotations as a substitute for source Javadoc.
- Do not claim annotated declarations before IntelliJ's native documentation path has had priority.
- Do not introduce project scans, custom indexes, startup activities, or background caches.
- Do not require the Groovy plugin. Plain Java projects are first-class consumers.
- Verify Groovy call sites and real KlumAST-generated APIs as compatibility coverage.
- Do not bundle or depend on AnnoDocimal or KlumAST at plugin runtime.
- Do not expose user settings or a public extension point in the first version.

## Implementation baseline

- Language and toolchain: Java 21 only.
- Supported product: IntelliJ IDEA.
- Initial platform lines: 2025.3 and 2026.1.
- Distribution milestone: tested, locally installable plugin ZIP.
- Marketplace publication, signing, final plugin identity, repository renaming, and broader JetBrains product claims are deferred.

The current Kotlin template sources, sample tool window, startup activity, random-number service, and abandoned Java documentation target are disposable. Retain build and CI infrastructure only where it still serves the new vertical slice.

## Verification strategy

The core suite needs a small library compiled with AnnoDocimal and consumed without attached sources. It must cover:

- type, method, field, and constructor documentation;
- native documentation precedence;
- absent annotations;
- missing, blank, and unreadable mapped values;
- readable raw Javadoc rendering, followed by representative inline and block-tag rendering; and
- no mandatory Groovy or KlumAST dependency.

A separate integration fixture must exercise Quick Documentation on a real KlumAST-generated API from a Groovy-oriented use case. Both fixture levels are required before the first milestone is complete.

## Deferred possibilities

- structured AnnoDocimal annotations described by issue #30;
- additional annotation/member mappings;
- user-visible mapping configuration or a public extractor extension point;
- a per-project disable switch if profiling demonstrates a need;
- verified compatibility with other JetBrains IDEs; and
- a separate future plugin for genuinely KlumAST-specific IDE features.
