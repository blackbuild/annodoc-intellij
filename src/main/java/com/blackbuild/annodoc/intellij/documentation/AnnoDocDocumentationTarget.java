package com.blackbuild.annodoc.intellij.documentation;

import com.intellij.model.Pointer;
import com.intellij.platform.backend.documentation.DocumentationResult;
import com.intellij.platform.backend.documentation.DocumentationTarget;
import com.intellij.platform.backend.presentation.TargetPresentation;
import com.intellij.psi.PsiModifierListOwner;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.SmartPointerManager;
import com.intellij.psi.SmartPsiElementPointer;
import org.jetbrains.annotations.NotNull;

final class AnnoDocDocumentationTarget implements DocumentationTarget {
    private final PsiModifierListOwner element;
    private final String documentation;

    AnnoDocDocumentationTarget(PsiModifierListOwner element, String documentation) {
        this.element = element;
        this.documentation = documentation;
    }

    @Override
    public @NotNull Pointer<? extends DocumentationTarget> createPointer() {
        SmartPsiElementPointer<PsiModifierListOwner> pointer = SmartPointerManager.createPointer(element);
        return Pointer.delegatingPointer(pointer, restored -> {
            String restoredDocumentation = AnnoDocExtractor.extract(restored);
            return restoredDocumentation == null ? null : new AnnoDocDocumentationTarget(restored, restoredDocumentation);
        });
    }

    @Override
    public @NotNull TargetPresentation computePresentation() {
        String name = element instanceof PsiNamedElement namedElement ? namedElement.getName() : null;
        return TargetPresentation.builder(name == null ? "AnnoDoc" : name).presentation();
    }

    @Override
    public DocumentationResult computeDocumentation() {
        return DocumentationResult.documentation(AnnoDocRenderer.render(element, documentation));
    }
}
