# Good and bad IntelliJ plugin tests

## Good tests

Test behavior through the narrowest realistic IntelliJ fixture and a platform-facing seam.

```java
public void testDocumentationContainsAnnotationText() {
    myFixture.configureByText(
        "Example.java",
        "class Example { @AnnoDoc(\"Useful documentation\") void run() {} }"
    );

    PsiMethod method = myFixture.findElementByText("run", PsiMethod.class);
    DocumentationTarget target = provider.documentationTarget(method, null);

    assertNotNull(target);
    assertTrue(render(target).contains("Useful documentation"));
}
```

Good fixture tests:

- describe observable editor or platform behavior;
- use realistic PSI text and public extension interfaces;
- survive internal refactoring;
- use independently known expected values;
- avoid asserting incidental markup or PSI implementation details.

## Bad tests

Avoid tests that prove only private orchestration:

```java
public void testProviderCallsRendererOnce() {
    provider.documentationTarget(method, null);
    verify(renderer).render(method);
}
```

This breaks when internal collaborators are reorganized even if IDE behavior remains correct.

Avoid bypassing registration when registration is part of the contract. Instantiating a provider directly can be appropriate for a small logic seam, but it does not prove that IntelliJ can discover the extension from `plugin.xml`.

Avoid tautological expectations. Use a literal or specification-derived expected result; do not compute the expected HTML with the same renderer under test.
