package demo;

import com.blackbuild.annodocimal.annotations.AnnoDoc;

public final class NativeDocumentationExamples {
    /** Native source Javadoc must keep precedence over annotation-carried documentation. */
    @AnnoDoc("This annotation text must not replace native source Javadoc.")
    public void nativeDocumentationWins() {
    }

    @AnnoDoc("Source annotations are outside the plugin's compiled-declaration fallback.")
    public void sourceAnnotationOnly() {
    }

    public void exerciseSourceDeclarations() {
        nativeDocumentationWins();
        sourceAnnotationOnly();
    }
}
