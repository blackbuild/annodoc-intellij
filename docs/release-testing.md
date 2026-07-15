# 0.1.0-alpha.1 release-candidate validation

## Release controls

`pluginVersion = 0.1.0-alpha.1` maps to the JetBrains Marketplace `alpha` channel through the IntelliJ Platform Gradle Plugin publishing configuration. The build workflow prepares a GitHub draft release only after its build, test, Qodana, and verifier jobs succeed on `main`; it preserves existing drafts and releases rather than deleting them.

Marketplace publication is not triggered by creating, publishing, or promoting a GitHub release. A maintainer must first publish the GitHub release as a prerelease, then manually dispatch the `Release` workflow with its exact tag and the literal confirmation `publish`. The workflow checks both conditions, checks out the tag, verifies that the Gradle version equals the tag and uses the alpha suffix, then uses only GitHub repository secrets for signing and publication.

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

Automated verification cannot install into a user-managed IDEA instance. Before approving Marketplace publication, install `build/distributions/annodoc-intellij-0.1.0-alpha.1.zip` from disk in both IntelliJ IDEA 2025.3 and 2026.1, then record the following observations:

1. In a plain Java project using the compiled AnnoDoc fixture without attached sources, invoke Quick Documentation on a documented type, method, field, and constructor; all should show the annotation-carried documentation.
2. In a Groovy project using the KlumAST fixture, invoke Quick Documentation on a generated DSL API; its AnnoDoc documentation should appear without making Groovy a plugin runtime dependency.
3. Invoke Quick Documentation where ordinary source Javadoc is available; native documentation must win.
4. Invoke Quick Documentation on an unannotated or malformed/blank AnnoDoc declaration; no error or fabricated documentation should appear.

If signing secrets are configured in the execution environment, run `./gradlew signPlugin verifyPluginSignature` and inspect only its success status and signed archive. Do not print or write the credential values. Otherwise, signing remains a maintainer-gated validation step.
