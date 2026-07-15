# Releasing AnnoDoc Support

This procedure is for maintainers preparing an IntelliJ Marketplace release. It does not authorize or automate publication: publishing remains an explicit maintainer decision after all release-candidate checks are complete.

## Prepare the candidate

1. Set `pluginVersion` in `gradle.properties` to the intended SemVer version.
2. For a prerelease such as `0.1.0-alpha.1`, confirm the suffix maps to the intended Marketplace channel. The build derives the channel from the text between `-` and the next `.`; this version publishes to `alpha`.
3. Move the completed notes from `Unreleased` to a heading for that exact version in `CHANGELOG.md`.
4. Run `./gradlew check verifyPlugin buildPlugin` and inspect the ZIP as described in [release-testing.md](release-testing.md).
5. Complete and record the manual IDEA smoke test for every supported IDEA line.

## Create and approve the GitHub release

After the Build workflow succeeds on `main`, it creates a draft GitHub release for the version and its changelog notes. It preserves an existing draft or release with the same tag; it never deletes prior releases.

Review the draft’s tag, notes, and attached build evidence. When every acceptance criterion is satisfied, a maintainer must explicitly publish it as a **prerelease**. Publishing the GitHub release alone does not publish to JetBrains Marketplace.

## Publish to JetBrains Marketplace

The `Release` GitHub Actions workflow has no release-event trigger. A maintainer must manually dispatch it with:

- `release_tag`: the published GitHub prerelease tag, exactly matching `pluginVersion`;
- `confirmation`: the literal value `publish`.

The workflow rejects drafts, stable GitHub releases, non-alpha versions, and a tag/version mismatch. It checks out the tag, signs the plugin, publishes it to the Marketplace, and uploads the resulting ZIP to that GitHub release.

Configure these repository secrets before dispatching the workflow:

- `PUBLISH_TOKEN`
- `CERTIFICATE_CHAIN`
- `PRIVATE_KEY`
- `PRIVATE_KEY_PASSWORD`

Never add credentials to repository files, logs, command lines, or issue comments. If the secrets are unavailable, do not dispatch the publishing workflow; perform only the credential-independent checks and leave signing/publication as an explicit remaining maintainer action.
