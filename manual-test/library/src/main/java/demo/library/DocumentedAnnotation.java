package demo.library;

import com.blackbuild.annodocimal.annotations.AnnoDoc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@AnnoDoc("A documented annotation type used by the manual-test consumer.")
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR})
public @interface DocumentedAnnotation {
    @AnnoDoc("The annotation's generic-purpose text value.\n@return the configured text")
    String value();
}
