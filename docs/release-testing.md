# 0.1.0-alpha.1 release-candidate validation

The maintainer procedure for drafting, approving, signing, and publishing a release is in [Releasing AnnoDoc Support](releasing.md). This document records the validation evidence for the `0.1.0-alpha.1` candidate.

## Dependency triage on 2026-07-15

Applicable updates adopted for this candidate:

- Qodana Gradle plugin, GitHub Action, and JVM Community linter image: `2026.1.3`, with Java 21 retained.
- `actions/checkout@v7`, `actions/setup-java@v5`, `gradle/actions/setup-gradle@v6`, and `actions/upload-artifact@v7`. These replace the Node.js 20-era action lines that generated runtime deprecation notices on GitHub-hosted runners. The obsolete disk-space action was removed from the build workflow rather than retaining an unsupported runtime solely for template cleanup.

Obsolete Dependabot proposals are not applicable because the Java-only rebuild no longer applies their dependencies:

- `org.jetbrains.kotlin.jvm` (PR #15)
- `org.jetbrains.kotlinx.kover` (PR #12)

The old changelog and IntelliJ Platform Gradle Plugin proposals (PRs #21 and #20) were superseded by versions already present on the corrected main branch. The remaining current action and Qodana proposals (PRs #24–#28 and #3) were triaged by their patches and incorporated locally; no dependency pull request was merged or changed.

## Documentation-target API risk

The plugin uses `PsiDocumentationTargetProvider` and `DocumentationTarget` because JetBrains documents the Documentation Target API as the replacement for the deprecated Documentation Provider API. In the pinned IntelliJ IDEA 2025.3.6 SDK, the provider API is marked `ApiStatus.OverrideOnly` and its extension-point field is `ApiStatus.Internal`; the JetBrains extension-point catalog labels the documentation-target path experimental. The older provider API is not a stable replacement and cannot provide the same fallback integration without using deprecated APIs.

`verifyPlugin` reports no compatibility errors for IDEA 2025.3, 2026.1, or the configured 2026.2 EAP. It reports six experimental references for 2025.3 (the documentation target presentation builders and `Pointer.delegatingPointer`) and two for 2026.1 (the pointer interface and its delegating method); the configured EAP reports none. These types are required to implement the target contract, so this is an accepted compatibility risk for the 2025.3/2026.1 release lines. Keep `verifyPlugin` required and re-evaluate the provider when JetBrains promotes a stable documentation fallback seam.

## Manual IDEA smoke test

Automated verification cannot install into a user-managed IDEA instance. Follow [Manual testing a plugin ZIP](manual-testing.md) to generate an isolated demo project, install `build/distributions/annodoc-intellij-0.1.0-alpha.1.zip` from disk in both IntelliJ IDEA 2025.3 and 2026.1, then record the following observations:

1. Quick Documentation on the compiled class, interface, annotation type, record, enum, and nested structures shows their annotation-carried documentation.
2. Quick Documentation on compiled methods, constructors, fields, generic declarations, and interface members shows readable inline markup and block tags.
3. Ordinary source Javadoc wins over a source annotation, and a manually authored source annotation alone is not treated as compiled fallback documentation.
4. Unannotated and blank-AnnoDoc declarations produce no error or fabricated documentation.

The real KlumAST-generated compatibility case remains covered by the automated `KlumAstQuickDocumentationTest`; the manual project intentionally has no Groovy, KlumAST, or AnnoDocimal setup.

If signing secrets are configured in the execution environment, run `./gradlew signPlugin verifyPluginSignature` and inspect only its success status and signed archive. Do not print or write the credential values. Otherwise, signing remains a maintainer-gated validation step.
