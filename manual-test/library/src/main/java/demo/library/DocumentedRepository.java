package demo.library;

import com.blackbuild.annodocimal.annotations.AnnoDoc;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@AnnoDoc("A generic documented interface for looking up and transforming values.\n@param <T> the stored value type")
public interface DocumentedRepository<T> {
    @AnnoDoc("The default number of values requested from a repository.")
    int DEFAULT_PAGE_SIZE = 25;

    @AnnoDoc("Finds a value by identifier.\n@param id the stable identifier\n@return the matching value, if one exists")
    Optional<T> findById(String id);

    @AnnoDoc("Maps repository values while preserving generic variance.\n@param values the source values\n@param mapper the mapping function\n@param <R> the result type\n@return the mapped values")
    <R> List<R> map(List<T> values, Function<? super T, ? extends R> mapper);
}
