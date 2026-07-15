package org.annodoc.intellij.documentation;

import com.intellij.lang.java.JavaDocumentationProvider;
import com.intellij.platform.backend.documentation.DocumentationTarget;
import com.intellij.platform.backend.documentation.PsiDocumentationTargetProvider;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiCompiledElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifierListOwner;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class AnnoDocDocumentationTargetProvider implements PsiDocumentationTargetProvider {
    @Override
    public @Nullable DocumentationTarget documentationTarget(PsiElement element, @Nullable PsiElement originalElement) {
        if (!(element instanceof PsiModifierListOwner owner)
                || (!(element instanceof PsiClass)
                && !(element instanceof PsiMethod)
                && !(element instanceof PsiField))
                || !(element instanceof PsiCompiledElement)) {
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
