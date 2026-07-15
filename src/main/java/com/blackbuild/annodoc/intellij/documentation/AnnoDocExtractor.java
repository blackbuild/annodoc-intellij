package com.blackbuild.annodoc.intellij.documentation;

import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiModifierListOwner;
import org.jetbrains.annotations.Nullable;

final class AnnoDocExtractor {
    private static final String ANNOTATION_FQN = "com.blackbuild.annodocimal.annotations.AnnoDoc";
    private static final String DOCUMENTATION_MEMBER = "value";

    private AnnoDocExtractor() {
    }

    static @Nullable String extract(PsiModifierListOwner owner) {
        PsiAnnotation annotation = owner.getAnnotation(ANNOTATION_FQN);
        if (annotation == null) {
            return null;
        }

        PsiAnnotationMemberValue memberValue = annotation.findDeclaredAttributeValue(DOCUMENTATION_MEMBER);
        if (memberValue == null) {
            return null;
        }

        Object value = JavaPsiFacade.getInstance(owner.getProject())
                .getConstantEvaluationHelper()
                .computeConstantExpression(memberValue, false);
        if (!(value instanceof String documentation) || documentation.isBlank()) {
            return null;
        }
        return documentation;
    }
}
