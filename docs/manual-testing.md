# Manual testing a plugin ZIP

Use the generated Java project to smoke-test a finished plugin ZIP in each supported IntelliJ IDEA line. Every run produces a separate clean project, so IDEA 2025.3 and 2026.1 cannot rewrite each other's project metadata.

## Build the ZIP and demo project

Run this from the repository root, replacing the IDEA line for the installation being tested:

```shell
./gradlew buildPlugin prepareManualTestProject -PmanualTestIdeaVersion=2025.3
```

Supported values are `2025.3` and `2026.1`. The command creates:

- the installable plugin under `build/distributions/annodoc-intellij-<version>.zip`; and
- the clean demo project under `build/manual-test/idea-<IDEA-line>`.

`prepareManualTestProject` is a Gradle `Sync` task. Rerunning it restores the selected project to its template state and removes IDEA-generated or stale files from that output directory.

The build performs two explicit steps:

1. It compiles `manual-test/library/src/main/java` into `lib/annodoc-demo-library.jar`. That source tree declares the minimal `com.blackbuild.annodocimal.annotations.AnnoDoc` contract verbatim and applies it directly; it has no AnnoDocimal or KlumAST dependency and uses no annotation processor or AST transformation.
2. It copies `manual-test/project-template` and links the compiled JAR as a module library with empty source and Javadoc roots.

The root `check` task also compiles the template consumer against only this JAR, so fixture drift fails automated verification.

## Open and test the project

For each supported IDEA installation:

1. Open **Settings | Plugins**, choose **Install Plugin from Disk**, select the ZIP from `build/distributions`, and restart IDEA when prompted.
2. Choose **File | Open** and select the matching `build/manual-test/idea-<IDEA-line>` directory.
3. Select a Java 21 SDK if IDEA cannot inherit one automatically, then wait for indexing to finish.
4. Open `src/demo/ManualSmokeTest.java`. Put the caret on a referenced compiled declaration and invoke **View | Quick Documentation**.
5. Work through the checklist in the generated project's `README.md`.

Do not attach the library sources or generated Javadoc: the absence of both is what exercises the plugin's fallback. To repeat a run from a known state, close that project and rerun the generation command for the same IDEA line.

The demo deliberately remains a plain Java project. Real KlumAST-generated API compatibility stays in the automated `KlumAstQuickDocumentationTest` fixture and does not add Groovy, KlumAST, or AnnoDocimal setup to this manual smoke test.
