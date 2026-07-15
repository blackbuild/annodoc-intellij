# Testing

## Feedback lanes

Use the narrowest reliable lane first:

```shell
./gradlew test --tests com.example.SomeTest.someBehavior
./gradlew test --tests com.example.SomeTest
./gradlew test
./gradlew check
```

Replace the example class and method with the actual fully qualified test selector. Run `./gradlew check` before final handoff when practical; it is the repository-wide verification lane and may include coverage or static analysis beyond `test`.

Use `./gradlew verifyPlugin` when changing plugin compatibility, plugin metadata, extension registration, or dependencies. Use `./gradlew runIde` only when interactive IDE validation is necessary; report manual observations separately from automated evidence.

## Test seams

Prefer `BasePlatformTestCase`, `LightJavaCodeInsightFixtureTestCase`, or the narrowest suitable IntelliJ fixture. Exercise behavior through PSI, extension lookup, documentation targets, actions, or other public platform-facing seams rather than private helpers.

Keep fixtures small but realistic. Assert observable behavior with stable semantics; avoid brittle full-HTML or incidental PSI-implementation assertions when a focused semantic assertion suffices.

Every ignored or conditionally disabled test must state an actionable reason and, when possible, the condition for re-enabling it.
