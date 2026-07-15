package com.blackbuild.annodoc.intellij.documentation;

import com.blackbuild.annodocimal.annotations.AnnoDoc;
import com.intellij.platform.backend.documentation.DocumentationData;
import com.intellij.platform.backend.documentation.DocumentationResult;
import com.intellij.platform.backend.documentation.DocumentationTarget;
import com.intellij.platform.backend.documentation.PsiDocumentationTargetProvider;
import com.intellij.lang.java.JavaDocumentationProvider;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.roots.JavadocOrderRootType;
import com.intellij.openapi.roots.LibraryOrderEntry;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.vfs.JarFileSystem;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiCompiledElement;
import com.intellij.psi.PsiNewExpression;
import com.intellij.testFramework.IdeaTestUtil;
import com.intellij.testFramework.LightProjectDescriptor;
import com.intellij.testFramework.PsiTestUtil;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

public final class AnnoDocQuickDocumentationTest extends LightJavaCodeInsightFixtureTestCase {
    private Path fixtureDirectory;
    private Library fixtureLibrary;

    @Override
    protected @NotNull LightProjectDescriptor getProjectDescriptor() {
        return JAVA_21;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        fixtureDirectory = Files.createTempDirectory("annodoc-compiled-fixture");
        Path annoDocimalAnnotationsJar = annoDocimalAnnotationsJar();
        Path libraryJar = compileFixtureLibrary(fixtureDirectory);
        PsiTestUtil.addLibrary(
                getModule(),
                "annodocimal-annotations",
                annoDocimalAnnotationsJar.getParent().toString(),
                annoDocimalAnnotationsJar.getFileName().toString()
        );
        PsiTestUtil.addLibrary(
                getModule(),
                "annodoc-compiled-fixture",
                libraryJar.getParent().toString(),
                libraryJar.getFileName().toString()
        );
        String fixtureLibraryRootUrl = VfsUtil.getUrlForLibraryRoot(libraryJar.toFile());
        fixtureLibrary = Arrays.stream(ModuleRootManager.getInstance(getModule()).getOrderEntries())
                .filter(LibraryOrderEntry.class::isInstance)
                .map(LibraryOrderEntry.class::cast)
                .filter(entry -> Arrays.asList(entry.getRootUrls(OrderRootType.CLASSES))
                        .contains(fixtureLibraryRootUrl))
                .map(LibraryOrderEntry::getLibrary)
                .findFirst()
                .orElseThrow();
    }

    @Override
    protected void tearDown() throws Exception {
        try {
            if (fixtureDirectory != null) {
                try (var paths = Files.walk(fixtureDirectory)) {
                    paths.sorted(Comparator.reverseOrder()).forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException exception) {
                            throw new RuntimeException(exception);
                        }
                    });
                }
            }
        } finally {
            super.tearDown();
        }
    }

    public void testQuickDocumentationFallsBackToAnnoDocOnCompiledTypeWithoutSources() {
        myFixture.configureByText(
                "Usage.java",
                "import fixture.DocumentedType; class Usage { Documented<caret>Type value; }"
        );

        PsiElement originalElement = myFixture.getFile().findElementAt(myFixture.getCaretOffset());
        PsiElement targetElement = myFixture.getElementAtCaret();

        assertInstanceOf(targetElement, PsiClass.class);
        assertInstanceOf(targetElement, PsiCompiledElement.class);

        DocumentationTarget target = PsiDocumentationTargetProvider.EP_NAME.getExtensionList().stream()
                .map(provider -> provider.documentationTarget(targetElement, originalElement))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);

        assertNotNull("The registered Quick Documentation extension must claim the annotated compiled type", target);
        DocumentationResult result = target.computeDocumentation();
        DocumentationData documentation = assertInstanceOf(result, DocumentationData.class);
        assertTrue(documentation.getHtml().contains("Documentation recovered from the compiled annotation."));
    }

    public void testQuickDocumentationFallsBackToAnnoDocOnCompiledMethodWithoutSources() {
        myFixture.configureByText(
                "Usage.java",
                "class Usage { void use(fixture.DocumentedType value) { value.documented<caret>Method(); } }"
        );

        PsiElement originalElement = myFixture.getFile().findElementAt(myFixture.getCaretOffset());
        PsiElement targetElement = myFixture.getElementAtCaret();
        DocumentationTarget target = PsiDocumentationTargetProvider.EP_NAME.getExtensionList().stream()
                .map(provider -> provider.documentationTarget(targetElement, originalElement))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);

        assertNotNull("The registered Quick Documentation extension must claim the annotated compiled method", target);
        DocumentationData documentation = assertInstanceOf(target.computeDocumentation(), DocumentationData.class);
        assertTrue(documentation.getHtml().contains("Compiled method documentation."));
    }

    public void testQuickDocumentationFallsBackToAnnoDocOnCompiledConstructorWithoutSources() {
        myFixture.configureByText(
                "Usage.java",
                "class Usage { fixture.DocumentedType value = new fixture.Documented<caret>Type(); }"
        );

        PsiElement originalElement = myFixture.getFile().findElementAt(myFixture.getCaretOffset());
        PsiNewExpression newExpression = PsiTreeUtil.findChildOfType(myFixture.getFile(), PsiNewExpression.class);
        assertNotNull(newExpression);
        PsiElement targetElement = newExpression.resolveConstructor();
        DocumentationTarget target = PsiDocumentationTargetProvider.EP_NAME.getExtensionList().stream()
                .map(provider -> provider.documentationTarget(targetElement, originalElement))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);

        assertNotNull("The registered Quick Documentation extension must claim the annotated compiled constructor", target);
        DocumentationData documentation = assertInstanceOf(target.computeDocumentation(), DocumentationData.class);
        assertTrue(documentation.getHtml().contains("Compiled constructor documentation."));
    }

    public void testQuickDocumentationFallsBackToAnnoDocOnCompiledFieldWithoutSources() {
        myFixture.configureByText(
                "Usage.java",
                "class Usage { String use(fixture.DocumentedType value) { return value.documented<caret>Field; } }"
        );

        PsiElement originalElement = myFixture.getFile().findElementAt(myFixture.getCaretOffset());
        PsiElement targetElement = myFixture.getElementAtCaret();
        DocumentationTarget target = PsiDocumentationTargetProvider.EP_NAME.getExtensionList().stream()
                .map(provider -> provider.documentationTarget(targetElement, originalElement))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);

        assertNotNull("The registered Quick Documentation extension must claim the annotated compiled field", target);
        DocumentationData documentation = assertInstanceOf(target.computeDocumentation(), DocumentationData.class);
        assertTrue(documentation.getHtml().contains("Compiled field documentation."));
    }

    public void testQuickDocumentationIgnoresCompiledDeclarationsWithoutAnnoDoc() {
        myFixture.configureByText(
                "Usage.java",
                "class Usage { void use(fixture.DocumentedType value) { value.unannotated<caret>Method(); } }"
        );

        PsiElement originalElement = myFixture.getFile().findElementAt(myFixture.getCaretOffset());
        PsiElement targetElement = myFixture.getElementAtCaret();
        DocumentationTarget target = PsiDocumentationTargetProvider.EP_NAME.getExtensionList().stream()
                .map(provider -> provider.documentationTarget(targetElement, originalElement))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);

        assertNull("Unannotated declarations must remain on IntelliJ's native documentation path", target);
    }

    public void testQuickDocumentationIgnoresBlankAnnoDocValues() {
        myFixture.configureByText(
                "Usage.java",
                "class Usage { void use(fixture.DocumentedType value) { value.blank<caret>Documentation(); } }"
        );

        PsiElement originalElement = myFixture.getFile().findElementAt(myFixture.getCaretOffset());
        PsiElement targetElement = myFixture.getElementAtCaret();
        DocumentationTarget target = PsiDocumentationTargetProvider.EP_NAME.getExtensionList().stream()
                .map(provider -> provider.documentationTarget(targetElement, originalElement))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);

        assertNull("Blank annotation values must remain on IntelliJ's native documentation path", target);
    }

    public void testNativeSourceJavadocKeepsPrecedenceOverAnnoDoc() {
        myFixture.configureByText(
                "SourceType.java",
                """
                import com.blackbuild.annodocimal.annotations.AnnoDoc;
                class SourceType {
                    /** Native source documentation. */
                    @AnnoDoc("Annotation documentation must not replace native Javadoc.")
                    void documented() {}
                    void use() { docu<caret>mented(); }
                }
                """
        );

        PsiElement originalElement = myFixture.getFile().findElementAt(myFixture.getCaretOffset());
        PsiElement targetElement = myFixture.getElementAtCaret();
        DocumentationTarget annoDocTarget = PsiDocumentationTargetProvider.EP_NAME.getExtensionList().stream()
                .map(provider -> provider.documentationTarget(targetElement, originalElement))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
        assertNull("Source declarations must not be claimed by the AnnoDoc fallback", annoDocTarget);

        String nativeDocumentation = new JavaDocumentationProvider().generateDoc(targetElement, originalElement);
        assertNotNull(nativeDocumentation);
        assertTrue(nativeDocumentation.contains("Native source documentation."));
        assertFalse(nativeDocumentation.contains("must not replace native Javadoc"));
    }

    public void testExternalJavadocKeepsPrecedenceOverAnnoDocFallback() throws Exception {
        Path javadocJar = fixtureDirectory.resolve("javadoc.jar");
        try (JarOutputStream output = new JarOutputStream(Files.newOutputStream(javadocJar))) {
            output.putNextEntry(new JarEntry("fixture/DocumentedType.html"));
            output.write("<html><body>Native external Javadoc.</body></html>".getBytes());
            output.closeEntry();
        }
        VirtualFile localJavadocJar = LocalFileSystem.getInstance().refreshAndFindFileByNioFile(javadocJar);
        assertNotNull(localJavadocJar);
        VirtualFile javadocRoot = JarFileSystem.getInstance().getJarRootForLocalFile(localJavadocJar);
        assertNotNull(javadocRoot);

        ApplicationManager.getApplication().runWriteAction(() -> {
            Library.ModifiableModel model = fixtureLibrary.getModifiableModel();
            model.addRoot(javadocRoot.getUrl(), JavadocOrderRootType.getInstance());
            model.commit();
        });

        myFixture.configureByText(
                "Usage.java",
                "import fixture.DocumentedType; class Usage { Documented<caret>Type value; }"
        );
        PsiElement originalElement = myFixture.getFile().findElementAt(myFixture.getCaretOffset());
        PsiElement targetElement = myFixture.getElementAtCaret();

        assertNotNull(JavaDocumentationProvider.getExternalJavaDocUrl(targetElement));
        assertFalse(JavaDocumentationProvider.getExternalJavaDocUrl(targetElement).isEmpty());
        DocumentationTarget target = PsiDocumentationTargetProvider.EP_NAME.getExtensionList().stream()
                .map(provider -> provider.documentationTarget(targetElement, originalElement))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
        assertNull("External Javadoc must retain precedence over the AnnoDoc fallback", target);
    }

    public void testQuickDocumentationRendersJavadocMarkupAndBlockTags() {
        myFixture.configureByText(
                "Usage.java",
                "class Usage { String use(fixture.DocumentedType value) { return value.rendered<caret>(\"input\"); } }"
        );

        PsiElement originalElement = myFixture.getFile().findElementAt(myFixture.getCaretOffset());
        PsiElement targetElement = myFixture.getElementAtCaret();
        DocumentationTarget target = PsiDocumentationTargetProvider.EP_NAME.getExtensionList().stream()
                .map(provider -> provider.documentationTarget(targetElement, originalElement))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);

        assertNotNull(target);
        DocumentationData documentation = assertInstanceOf(target.computeDocumentation(), DocumentationData.class);
        String html = documentation.getHtml();
        assertTrue(html.contains("<b>HTML</b>"));
        assertTrue(html.contains("<code>"));
        assertTrue(html.contains("inline"));
        assertTrue(html.contains("the input value"));
        assertTrue(html.contains("the rendered result"));
    }

    public void testQuickDocumentationKeepsMalformedJavadocContentReadable() {
        myFixture.configureByText(
                "Usage.java",
                "class Usage { void use(fixture.DocumentedType value) { value.malformed<caret>Documentation(); } }"
        );

        PsiElement originalElement = myFixture.getFile().findElementAt(myFixture.getCaretOffset());
        PsiElement targetElement = myFixture.getElementAtCaret();
        DocumentationTarget target = PsiDocumentationTargetProvider.EP_NAME.getExtensionList().stream()
                .map(provider -> provider.documentationTarget(targetElement, originalElement))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);

        assertNotNull(target);
        DocumentationData documentation = assertInstanceOf(target.computeDocumentation(), DocumentationData.class);
        assertTrue(documentation.getHtml().contains("Malformed but still readable"));
    }

    private static Path compileFixtureLibrary(Path root) throws Exception {
        Path sources = Files.createDirectories(root.resolve("sources"));
        Path documentedTypeSource = sources.resolve("fixture/DocumentedType.java");
        Files.createDirectories(documentedTypeSource.getParent());

        Files.writeString(documentedTypeSource, """
                package fixture;
                import com.blackbuild.annodocimal.annotations.AnnoDoc;
                @AnnoDoc("Documentation recovered from the compiled annotation.")
                public class DocumentedType {
                    @AnnoDoc("Compiled constructor documentation.")
                    public DocumentedType() {}
                    @AnnoDoc("Compiled method documentation.")
                    public void documentedMethod() {}
                    @AnnoDoc("Compiled field documentation.")
                    public String documentedField;
                    public void unannotatedMethod() {}
                    @AnnoDoc("   ") public void blankDocumentation() {}
                    @AnnoDoc("Renders <b>HTML</b> and {@code inline}.\\n@param input the input value\\n@return the rendered result")
                    public String rendered(String input) { return input; }
                    @AnnoDoc("Malformed but still readable {@code")
                    public void malformedDocumentation() {}
                }
                """);

        Path classes = Files.createDirectories(root.resolve("classes"));
        Path annoDocimalAnnotationsJar = annoDocimalAnnotationsJar();
        IdeaTestUtil.compileFile(
                documentedTypeSource.toFile(),
                classes.toFile(),
                "--release", "21",
                "-classpath", annoDocimalAnnotationsJar.toString()
        );

        Path jar = root.resolve("annodoc-fixture.jar");
        try (JarOutputStream output = new JarOutputStream(Files.newOutputStream(jar));
             var classFiles = Files.walk(classes)) {
            for (Path classFile : classFiles.filter(Files::isRegularFile).toList()) {
                String entryName = classes.relativize(classFile).toString().replace('\\', '/');
                output.putNextEntry(new JarEntry(entryName));
                Files.copy(classFile, output);
                output.closeEntry();
            }
        }
        return jar;
    }

    private static Path annoDocimalAnnotationsJar() throws Exception {
        URL classResource = AnnoDoc.class.getResource("AnnoDoc.class");
        if (classResource == null || !"jar".equals(classResource.getProtocol())) {
            throw new IllegalStateException("Cannot locate the AnnoDocimal annotations test artifact");
        }
        String resourceUrl = classResource.toExternalForm();
        int entrySeparator = resourceUrl.indexOf("!/");
        if (!resourceUrl.startsWith("jar:") || entrySeparator < 0) {
            throw new IllegalStateException("Unexpected AnnoDoc resource URL: " + resourceUrl);
        }
        return Path.of(URI.create(resourceUrl.substring("jar:".length(), entrySeparator)));
    }
}
