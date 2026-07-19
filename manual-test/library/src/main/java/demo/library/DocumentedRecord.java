package demo.library;

import com.blackbuild.annodocimal.annotations.AnnoDoc;

import java.util.Objects;

@AnnoDoc("A generic compiled record.\n@param <K> the key type\n@param <V> the value type")
public record DocumentedRecord<K, V>(K key, V value) {
    @AnnoDoc("Creates a record with non-null components.\n@param key the record key\n@param value the record value")
    public DocumentedRecord {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
    }
}
