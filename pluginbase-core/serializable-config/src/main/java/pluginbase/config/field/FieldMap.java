package pluginbase.config.field;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public class FieldMap implements Iterable<Field> {

    @Nullable
    Map<String, Field> fieldMap;

    FieldMap(@Nullable Map<String, Field> fieldMap) {
        this.fieldMap = fieldMap;
    }

    public boolean hasField(@NotNull String fieldName) {
        return fieldMap != null && fieldMap.containsKey(fieldName.toLowerCase());
    }

    @Nullable
    public Field getField(@NotNull String fieldName) {
        return fieldMap == null ? null : fieldMap.get(fieldName.toLowerCase());
    }

    public int size() {
        return fieldMap == null ? 0 : fieldMap.size();
    }

    @Override
    public Iterator<Field> iterator() {
        return new FieldIterator(fieldMap == null ? null : fieldMap.values().iterator());
    }

    private static class FieldIterator implements Iterator<Field> {

        @Nullable
        private Iterator<Field> fieldIterator;

        FieldIterator(@Nullable Iterator<Field> fieldIterator) {
            this.fieldIterator = fieldIterator;
        }

        @Override
        public boolean hasNext() {
            return fieldIterator != null && fieldIterator.hasNext();
        }

        @Override
        public Field next() {
            if (fieldIterator == null) {
                throw new NoSuchElementException();
            }
            return fieldIterator.next();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
