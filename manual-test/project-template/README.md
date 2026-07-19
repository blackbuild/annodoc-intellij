# AnnoDoc Support manual smoke test

This generated Java 21 project links `lib/annodoc-demo-library.jar` without sources or Javadoc. The JAR contains a minimal verbatim `AnnoDoc` declaration and precompiled annotated examples; it has no AnnoDocimal, KlumAST, annotation-processor, or Groovy dependency.

Open `src/demo/ManualSmokeTest.java`, place the caret on each referenced declaration below, and invoke **View | Quick Documentation**:

- `DocumentedModel`, `new DocumentedModel<>(...)`, `documentedField`, `value()`, and generic `indexBy(...)` cover a compiled class, constructor, field, ordinary method, and generic method with block tags.
- `DocumentedRepository`, `DEFAULT_PAGE_SIZE`, `findById(...)`, and variance-aware `map(...)` cover a generic interface and its members.
- `DocumentedModel.Nested`, `Inner`, and `State.READY` cover static nested, non-static inner, enum, and enum-constant declarations.
- `DocumentedRecord` and its constructor cover a generic record.
- `DocumentedAnnotation` covers a compiled annotation type.
- `UndocumentedType.unannotatedMethod()` and `blankDocumentation()` must not produce AnnoDoc fallback content or an error.

Then open `src/demo/NativeDocumentationExamples.java`:

- `nativeDocumentationWins()` must display its native source Javadoc, not the annotation value.
- `sourceAnnotationOnly()` must not display its manually authored source annotation as fallback documentation.

This directory is generated. Make fixture changes in the repository's `manual-test` sources, not here.
