package demo;

import demo.library.DocumentedAnnotation;
import demo.library.DocumentedModel;
import demo.library.DocumentedRecord;
import demo.library.DocumentedRepository;
import demo.library.UndocumentedType;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@DocumentedAnnotation("consumer")
public final class ManualSmokeTest {
    private final DocumentedRepository<String> repository;

    public ManualSmokeTest(DocumentedRepository<String> repository) {
        this.repository = repository;
    }

    public void exerciseCompiledDeclarations() {
        DocumentedModel<String> model = new DocumentedModel<>("value");
        String field = model.documentedField;
        String value = model.value();
        List<Integer> mapped = repository.map(List.of(value), String::length);
        Optional<String> found = repository.findById("id");
        Map<String, Integer> indexed = model.indexBy(mapped.getFirst());
        int pageSize = DocumentedRepository.DEFAULT_PAGE_SIZE;

        DocumentedModel.Nested<Integer> nested = new DocumentedModel.Nested<>(mapped.getFirst());
        DocumentedModel<String>.Inner inner = model.new Inner();
        DocumentedRecord<String, Integer> record = new DocumentedRecord<>(field, nested.value());
        DocumentedModel.State state = DocumentedModel.State.READY;

        System.out.println(found.orElse(record.key()) + inner.describe() + state + indexed + pageSize);
    }

    public void exerciseFallbackCases(UndocumentedType undocumented) {
        undocumented.unannotatedMethod();
        undocumented.blankDocumentation();
    }
}
