# Pull requests and release-facing documentation

Keep each pull request centered on one approved behavior or tracer-bullet slice. Use closing keywords only when the accepted issue behavior is fully delivered; mention related but deferred work without closing it.

Record user-visible features, fixes, compatibility changes, and deprecations in `CHANGELOG.md`. Update the README plugin-description block when the product description or advertised capability changes. Keep `plugin.xml`, `gradle.properties`, and the version catalog aligned when compatibility, dependencies, identity, or publishing metadata changes.

Before handoff, review changed source against `docs/agents/coding-style.md`; fix unnecessary fully qualified names unless a documented exception applies. Run the checks described in `docs/agents/testing.md` and inspect required CI results when a pull request exists. Report failures and skipped checks explicitly; green compilation alone is not evidence that an IntelliJ extension behaves correctly in the platform lifecycle.
