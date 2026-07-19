package demo.library;

import com.blackbuild.annodocimal.annotations.AnnoDoc;

import java.util.Map;

@AnnoDoc("A {@code generic} <b>compiled class</b> containing fields, methods, and nested declarations.\n@param <T> the text value type")
public class DocumentedModel<T extends CharSequence> {
    @AnnoDoc("A public generic field recovered from the compiled class.")
    public final T documentedField;

    @AnnoDoc("Creates a documented model.\n@param value the model value")
    public DocumentedModel(T value) {
        documentedField = value;
    }

    @AnnoDoc("Returns the model value with its generic type intact.\n@return the model value")
    public T value() {
        return documentedField;
    }

    @AnnoDoc("Builds a one-entry map with method and class type parameters.\n@param number the numeric value\n@param <N> the numeric type\n@return a map from the model value to the number\n@throws IllegalArgumentException when the number is negative")
    public <N extends Number> Map<T, N> indexBy(N number) {
        if (number.doubleValue() < 0) {
            throw new IllegalArgumentException("number must not be negative");
        }
        return Map.of(documentedField, number);
    }

    @AnnoDoc("A documented static nested generic class.\n@param <U> the nested value type")
    public static final class Nested<U> {
        private final U value;

        @AnnoDoc("Creates a nested value.\n@param value the nested value")
        public Nested(U value) {
            this.value = value;
        }

        @AnnoDoc("Returns the nested generic value.\n@return the nested value")
        public U value() {
            return value;
        }
    }

    @AnnoDoc("A documented non-static inner class.")
    public final class Inner {
        @AnnoDoc("Describes the enclosing model value.\n@return a readable description")
        public String describe() {
            return "inner:" + documentedField;
        }
    }

    @AnnoDoc("A documented nested enum representing model state.")
    public enum State {
        @AnnoDoc("The model is ready for use.")
        READY,
        @AnnoDoc("The model has been archived.")
        ARCHIVED
    }
}
