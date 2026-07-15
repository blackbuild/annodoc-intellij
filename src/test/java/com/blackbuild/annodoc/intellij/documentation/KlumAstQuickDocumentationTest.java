package com.blackbuild.annodoc.intellij.documentation;

import com.blackbuild.annodocimal.annotations.AnnoDoc;
import com.intellij.platform.backend.documentation.DocumentationData;
import com.intellij.platform.backend.documentation.DocumentationTarget;
import com.intellij.platform.backend.documentation.PsiDocumentationTargetProvider;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiCompiledElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.testFramework.LightProjectDescriptor;
import com.intellij.testFramework.PsiTestUtil;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;

public final class KlumAstQuickDocumentationTest extends LightJavaCodeInsightFixtureTestCase {
    @Override
    protected @NotNull LightProjectDescriptor getProjectDescriptor() {
        return JAVA_21;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        addLibrary("real-klum-ast-fixture", Path.of(System.getProperty("annodoc.klumAstFixtureJar")));
        addLibrary("annodocimal-annotations", libraryJarContaining(AnnoDoc.class));
    }

    public void testQuickDocumentationReadsAnnoDocFromRealKlumAstGeneratedMethodAtGroovyCallSite() {
        myFixture.configureByText(
                "Usage.groovy",
                """
                import fixture.DocumentedModel._RW as DocumentedModelBuilder

                DocumentedModelBuilder builder
                builder.it<caret>em {}
                """
        );

        assertTrue(quickDocumentationAtCaret().contains(
                "Creates a new 'item' and adds it to the 'items' collection."
        ));
    }

    public void testQuickDocumentationReadsAnnoDocInsideKlumAstFactoryClosure() {
        myFixture.configureByText(
                "Usage.groovy",
                """
                import fixture.DocumentedModel

                DocumentedModel.Create.With {
                    it<caret>em {}
                }
                """
        );

        assertTrue(quickDocumentationAtCaret().contains(
                "Creates a new 'item' and adds it to the 'items' collection."
        ));
    }

    private String quickDocumentationAtCaret() {
        PsiElement originalElement = myFixture.getFile().findElementAt(myFixture.getCaretOffset());
        PsiMethod targetElement = assertInstanceOf(myFixture.getElementAtCaret(), PsiMethod.class);
        assertInstanceOf(targetElement, PsiCompiledElement.class);
        PsiClass containingClass = targetElement.getContainingClass();
        assertNotNull(containingClass);
        assertEquals("fixture.DocumentedModel._RW", containingClass.getQualifiedName());

        DocumentationTarget target = PsiDocumentationTargetProvider.EP_NAME.getExtensionList().stream()
                .map(provider -> provider.documentationTarget(targetElement, originalElement))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);

        assertNotNull("The registered provider must claim the real KlumAST-generated method", target);
        DocumentationData documentation = assertInstanceOf(target.computeDocumentation(), DocumentationData.class);
        return documentation.getHtml();
    }

    private void addLibrary(String name, Path jar) {
        PsiTestUtil.addLibrary(getModule(), name, jar.getParent().toString(), jar.getFileName().toString());
    }

    private static Path libraryJarContaining(Class<?> type) throws Exception {
        URL classResource = type.getResource(type.getSimpleName() + ".class");
        if (classResource == null || !"jar".equals(classResource.getProtocol())) {
            throw new IllegalStateException("Cannot locate test artifact containing " + type.getName());
        }
        String resourceUrl = classResource.toExternalForm();
        int entrySeparator = resourceUrl.indexOf("!/");
        return Path.of(URI.create(resourceUrl.substring("jar:".length(), entrySeparator)));
    }
}
