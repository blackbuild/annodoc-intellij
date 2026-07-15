package com.blackbuild.annodoc.intellij.documentation;

import com.intellij.lang.java.JavaDocumentationProvider;
import com.intellij.platform.backend.documentation.DocumentationTarget;
import com.intellij.platform.backend.documentation.PsiDocumentationTargetProvider;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiCompiledElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifierListOwner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class AnnoDocDocumentationTargetProvider implements PsiDocumentationTargetProvider {
    @Override
    public @Nullable DocumentationTarget documentationTarget(@NotNull PsiElement element, @Nullable PsiElement originalElement) {
        if (!(element instanceof PsiModifierListOwner owner)
                || !(owner instanceof PsiClass || owner instanceof PsiMethod || owner instanceof PsiField)
                || !(owner instanceof PsiCompiledElement)) {
            return null;
        }

        String documentation = AnnoDocExtractor.extract(owner);
        if (documentation == null) {
            return null;
        }
        List<String> externalJavadocUrls = JavaDocumentationProvider.getExternalJavaDocUrl(element);
        if (externalJavadocUrls != null && !externalJavadocUrls.isEmpty()) {
            return null;
        }

        return new AnnoDocDocumentationTarget(owner, documentation);
    }
}
