# AnnoDoc IDE Support

This project makes documentation carried by annotations available through IntelliJ IDEA's normal documentation experience. AnnoDoc is its first supported format, plain Java projects are first-class consumers, and KlumAST is its motivating first consumer rather than a required dependency.

## Language

**AnnoDocimal**:
The [library and build-tool ecosystem](https://github.com/blackbuild/anno-docimal) that preserves source Javadoc in AnnoDoc annotations so documentation remains available from compiled classes. It is the primary producer of the metadata consumed by this project.

**KlumAST**:
The [Groovy AST-transformation framework](https://github.com/klum-dsl/klum-ast) that generates documented DSL APIs and is the initial, flagship use case for AnnoDoc support. It motivates the project but is not a required dependency.

**AnnoDoc**:
Documentation generated into a runtime-visible `@AnnoDoc` annotation on a JVM declaration. AnnoDocimal derives it from source Javadoc, while code-generation libraries may generate it directly; it is not intended for manual authoring.
_Avoid_: Klum documentation

**AnnoDoc support**:
Zero-configuration, transparent IDE fallback that exposes AnnoDoc from compiled types, methods, fields, and constructors through IntelliJ IDEA's standard Quick Documentation experience when ordinary source documentation is unavailable, without a separate action or tool window. Readable documentation is the baseline; rendering equivalent to ordinary Javadoc is the goal.
_Avoid_: Klum-specific IDE support

**Annotation-carried documentation**:
Generated documentation stored in annotation values so it survives in compiled classes rather than depending on attached sources. AnnoDoc is the first supported format; other annotation formats may be supported later.

**Documentation mapping**:
The association between an annotation's fully qualified name and the annotation member containing documentation. The initial built-in mapping is `com.blackbuild.annodocimal.annotations.AnnoDoc` to `value`; mappings are not user-configurable in the first release.

**Structured annotation documentation**:
A planned future AnnoDocimal representation that may distribute paragraphs, parameters, return values, exceptions, and tags across nested annotations and carry explicit format metadata. Its schema and names remain undecided in [AnnoDocimal issue #30](https://github.com/blackbuild/anno-docimal/issues/30), so the current plugin must leave room for a different extractor without implementing that format prematurely.

**Klum-specific feature**:
IDE behavior that requires knowledge of KlumAST beyond the general meaning of `@AnnoDoc`. Such behavior is outside the current project scope and may belong in a separate future plugin.
