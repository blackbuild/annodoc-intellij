# Establish Blackbuild ownership of the plugin identity

**AnnoDoc Support for IntelliJ IDEA** is the official IntelliJ IDEA integration for AnnoDocimal and is owned and published by Blackbuild. Its permanent identity must therefore use Blackbuild-controlled ownership and naming rather than Klum DSL or a personal namespace; KlumAST remains the flagship consumer without defining the plugin's product identity. The shorter product and Marketplace display name is **AnnoDoc Support**, emphasizing the annotation contract consumed by the plugin rather than implying an AnnoDocimal runtime dependency.

The permanent IntelliJ Marketplace plugin ID is `com.blackbuild.annodoc`. This uses Blackbuild's controlled namespace while preserving the AnnoDoc-focused product boundary and leaving room for other Blackbuild AnnoDoc integrations. It deliberately omits `intellij` because JetBrains Plugin Verifier rejects platform template words in new plugin IDs.

Handwritten Java packages and the Gradle project group use the more specific `com.blackbuild.annodoc.intellij` namespace. The provisional `org.annodoc.intellij` and `com.github.pauxus.klumideaplugin` namespaces are replaced before publication because neither represents the product's ownership and no released compatibility surface requires preserving them.

The permanent repository is `blackbuild/annodoc-intellij`, with `annodoc-intellij` as the Gradle root project and distribution artifact name. Moving away from `klum-dsl/klum-idea-plugin` makes repository ownership match the official product boundary and removes the misleading implication that the plugin provides KlumAST-specific IDE behavior.

The identity migration must produce a release-ready renamed codebase before the first Marketplace publication. Signing credentials, Marketplace configuration, release versioning, and publication are handled separately so the permanent identity can be verified without credential-dependent release operations.
