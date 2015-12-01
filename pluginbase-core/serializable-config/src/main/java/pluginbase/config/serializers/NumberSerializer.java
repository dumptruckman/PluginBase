package pluginbase.config.serializers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

class NumberSerializer<N extends Number> implements Serializer<N> {

    @Nullable
    @Override
    public Object serialize(@Nullable N object, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
        return object;
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public N deserialize(@Nullable Object serialized, @NotNull Class wantedType, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
        try {
            Method valueOf = wantedType.getMethod("valueOf", String.class);
            return (N) valueOf.invoke(null, String.valueOf(serialized));
        } catch (Exception e) {
            throw new RuntimeException("There was a problem deserializing a primitive number: " + serialized, e);
        }
    }

    static class BigNumberSerializer<N extends Number> implements Serializer<N> {

        @Nullable
        @Override
        public Object serialize(@Nullable N object, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
            return object;
        }

        @Nullable
        @Override
        @SuppressWarnings("unchecked")
        public N deserialize(@Nullable Object serialized, @NotNull Class wantedType, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
            try {
                return (N) InstanceUtil.createInstance(wantedType, new Class[] {String.class}, new Object[] {String.valueOf(serialized)});
            } catch (Exception e) {
                throw new RuntimeException("There was a problem deserializing a big number: " + serialized, e);
            }
        }
    }

    static class AtomicLongSerializer implements Serializer<AtomicLong> {

        @Nullable
        @Override
        public Object serialize(@Nullable AtomicLong object, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
            return object;
        }

        @Nullable
        @Override
        @SuppressWarnings("unchecked")
        public AtomicLong deserialize(@Nullable Object serialized, @NotNull Class wantedType, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
            try {
                return new AtomicLong(Long.valueOf(String.valueOf(serialized)));
            } catch (NumberFormatException e) {
                throw new RuntimeException("There was a problem deserializing an AtomicLong: " + serialized, e);
            }
        }
    }

    static class AtomicIntegerSerializer implements Serializer<AtomicInteger> {

        @Nullable
        @Override
        public Object serialize(@Nullable AtomicInteger object, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
            return object;
        }

        @Nullable
        @Override
        @SuppressWarnings("unchecked")
        public AtomicInteger deserialize(@Nullable Object serialized, @NotNull Class wantedType, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
            try {
                return new AtomicInteger(Integer.valueOf(String.valueOf(serialized)));
            } catch (NumberFormatException e) {
                throw new RuntimeException("There was a problem deserializing an AtomicLong: " + serialized, e);
            }
        }
    }
}
