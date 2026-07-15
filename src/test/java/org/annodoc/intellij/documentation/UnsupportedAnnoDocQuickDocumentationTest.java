package org.annodoc.intellij.documentation;

import com.intellij.platform.backend.documentation.DocumentationTarget;
import com.intellij.platform.backend.documentation.PsiDocumentationTargetProvider;
import com.intellij.psi.PsiCompiledElement;
import com.intellij.psi.PsiElement;
import com.intellij.testFramework.IdeaTestUtil;
import com.intellij.testFramework.LightProjectDescriptor;
import com.intellij.testFramework.PsiTestUtil;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

public final class UnsupportedAnnoDocQuickDocumentationTest extends LightJavaCodeInsightFixtureTestCase {
    private Path fixtureDirectory;

    @Override
    protected @NotNull LightProjectDescriptor getProjectDescriptor() {
        return JAVA_21;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        fixtureDirectory = Files.createTempDirectory("unsupported-annodoc-fixture");
        Path libraryJar = compileFixtureLibrary(fixtureDirectory);
        PsiTestUtil.addLibrary(
                getModule(),
                "unsupported-annodoc-fixture",
                libraryJar.getParent().toString(),
                libraryJar.getFileName().toString()
        );
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

    public void testQuickDocumentationIgnoresUnsupportedAnnotationMemberTypes() {
        assertAnnoDocFallbackDoesNotClaim(
                "class Usage { fixture.Unsupported<caret>Type value; }",
                "An annotation member with an unsupported type must be ignored"
        );
    }

    public void testQuickDocumentationIgnoresAnnotationsWithoutDeclaredMappedMemberValues() {
        assertAnnoDocFallbackDoesNotClaim(
                "class Usage { fixture.Missing<caret>ValueType value; }",
                "An annotation without a declared mapped value must be ignored"
        );
    }

    private void assertAnnoDocFallbackDoesNotClaim(String source, String message) {
        myFixture.configureByText("Usage.java", source);
        PsiElement originalElement = myFixture.getFile().findElementAt(myFixture.getCaretOffset());
        PsiElement targetElement = myFixture.getElementAtCaret();
        assertInstanceOf(targetElement, PsiCompiledElement.class);

        DocumentationTarget target = PsiDocumentationTargetProvider.EP_NAME.getExtensionList().stream()
                .map(provider -> provider.documentationTarget(targetElement, originalElement))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
        assertNull(message, target);
    }

    private static Path compileFixtureLibrary(Path root) throws Exception {
        Path sources = Files.createDirectories(root.resolve("sources"));
        Path annotationSource = writeSource(sources, "com/blackbuild/annodocimal/annotations/AnnoDoc.java", """
                package com.blackbuild.annodocimal.annotations;
                import java.lang.annotation.ElementType;
                import java.lang.annotation.Retention;
                import java.lang.annotation.RetentionPolicy;
                import java.lang.annotation.Target;
                @Retention(RetentionPolicy.RUNTIME)
                @Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD})
                public @interface AnnoDoc {
                    String[] value() default {};
                    String other() default "";
                }
                """);
        Path unsupportedTypeSource = writeSource(sources, "fixture/UnsupportedType.java", """
                package fixture;
                import com.blackbuild.annodocimal.annotations.AnnoDoc;
                @AnnoDoc({"unsupported"})
                public class UnsupportedType {}
                """);
        Path missingValueTypeSource = writeSource(sources, "fixture/MissingValueType.java", """
                package fixture;
                import com.blackbuild.annodocimal.annotations.AnnoDoc;
                @AnnoDoc(other = "not the configured member")
                public class MissingValueType {}
                """);

        Path classes = Files.createDirectories(root.resolve("classes"));
        IdeaTestUtil.compileFile(annotationSource.toFile(), classes.toFile(), "--release", "21");
        IdeaTestUtil.compileFile(
                unsupportedTypeSource.toFile(), classes.toFile(),
                "--release", "21", "-classpath", classes.toString()
        );
        IdeaTestUtil.compileFile(
                missingValueTypeSource.toFile(), classes.toFile(),
                "--release", "21", "-classpath", classes.toString()
        );

        Path jar = root.resolve("unsupported-annodoc-fixture.jar");
        try (JarOutputStream output = new JarOutputStream(Files.newOutputStream(jar));
             var classFiles = Files.walk(classes)) {
            for (Path classFile : classFiles.filter(Files::isRegularFile).toList()) {
                output.putNextEntry(new JarEntry(classes.relativize(classFile).toString().replace('\\', '/')));
                Files.copy(classFile, output);
                output.closeEntry();
            }
        }
        return jar;
    }

    private static Path writeSource(Path sources, String relativePath, String content) throws IOException {
        Path source = sources.resolve(relativePath);
        Files.createDirectories(source.getParent());
        Files.writeString(source, content);
        return source;
    }
}
