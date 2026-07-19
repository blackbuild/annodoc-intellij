package demo.library;

import com.blackbuild.annodocimal.annotations.AnnoDoc;

public final class UndocumentedType {
    public void unannotatedMethod() {
    }

    @AnnoDoc("   ")
    public void blankDocumentation() {
    }
}
