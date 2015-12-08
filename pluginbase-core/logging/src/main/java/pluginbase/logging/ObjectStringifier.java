package pluginbase.logging;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

public class ObjectStringifier {

    private static final String LINE_BREAK = System.getProperty("line.separator");

    private ObjectStringifier() {
        throw new AssertionError();
    }

    /**
     * Uses reflection to turn an object into a String. The string will contain the names and values of all non-static
     * fields within the object.
     *
     * @param o The object to stringify
     * @param showClasses specifies whether or not to show the complete class name of every value within the object.
     * @return a String representation of the object.
     */
    @NotNull
    public static String toString(@Nullable Object o, boolean showClasses) {
        if (o == null) {
            return "null";
        }

        StringBuilder buffer = new StringBuilder();
        buffer.append(showClasses ? o.getClass().getName() : o.getClass().getSimpleName()).append("{");
        buildUp(buffer, o.getClass(), o, showClasses, 1);
        buffer.append(LINE_BREAK).append("}");
        return buffer.toString();
    }

    /**
     * Uses reflection to turn an object into a String. The string will contain the names and values of all non-static
     * fields within the object.
     *
     * @param o The object to stringify
     * @return a String representation of the object.
     */
    @NotNull
    public static String toString(@Nullable Object o) {
        if (o == null) {
            return "null";
        }

        return toString(o, false);
    }

    private static void buildUp(@NotNull StringBuilder buffer, @NotNull Class clazz, @NotNull Object o, boolean showClasses, int level) {
        boolean first = true;
        for (Field field : clazz.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            if (first) {
                first = false;
            } else {
                buffer.append(",");
            }
            buffer.append(LINE_BREAK);
            addSpaces(buffer, level);

            boolean accessible = field.isAccessible();
            if (!accessible) {
                field.setAccessible(true);
            }

            buffer.append(field.getName()).append("=");

            Object value = null;
            try {
                value = field.get(o);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                continue;
            }
            if (value == null) {
                buffer.append("null");
                continue;
            }

            if (field.getType().isArray()) {
                handleArray(buffer, (Object[]) value, showClasses, level);
            } else if (Map.class.isAssignableFrom(field.getType())) {
                handleMap(buffer, (Map<?, ?>) value, showClasses, level);
            } else if (Collection.class.isAssignableFrom(field.getType())) {
                handleCollection(buffer, (Collection<?>) value, showClasses, level);
            } else {
                buffer.append(value.getClass().getName()).append("{").append(value.toString()).append("}");
            }

            if (!accessible) {
                field.setAccessible(false);
            }
        }
    }

    private static void handleArray(@NotNull StringBuilder buffer, @NotNull Object[] array, boolean showClasses, int level) {
        if (showClasses) {
            buffer.append(array.getClass()).append("{");
        }
        buffer.append("[");

        boolean first = true;
        for (Object o : array) {
            if (first) {
                first = false;
            } else {
                buffer.append(",");
            }
            buffer.append(LINE_BREAK);
            addSpaces(buffer, level + 1);
            if (o == null) {
                buffer.append("null");
            } else if (o.getClass().isArray()) {
                handleArray(buffer, (Object[]) o, showClasses, level + 1);
            } else if (Map.class.isAssignableFrom(o.getClass())) {
                handleMap(buffer, (Map<?, ?>) o, showClasses, level + 1);
            } else if (Collection.class.isAssignableFrom(o.getClass())) {
                handleCollection(buffer, (Collection<?>) o, showClasses, level + 1);
            } else {
                buffer.append(o.getClass().getName()).append("{").append(o.toString()).append("}");
            }
        }

        buffer.append(LINE_BREAK);
        addSpaces(buffer, level);
        buffer.append("]");
        if (showClasses) {
            buffer.append("}");
        }
    }

    private static void handleMap(@NotNull StringBuilder buffer, @NotNull Map<?, ?> map, boolean showClasses, int level) {
        if (showClasses) {
            buffer.append(map.getClass()).append("{");
        }
        buffer.append("{");

        boolean first = true;
        for (Entry entry : map.entrySet()) {
            if (first) {
                first = false;
            } else {
                buffer.append(",");
            }
            buffer.append(LINE_BREAK);
            addSpaces(buffer, level + 1);

            Object key = entry.getKey();
            buffer.append(key).append("=");

            Object o = entry.getValue();

            if (o == null) {
                buffer.append("null");
            } else if (o.getClass().isArray()) {
                handleArray(buffer, (Object[]) o, showClasses, level + 1);
            } else if (Map.class.isAssignableFrom(o.getClass())) {
                handleMap(buffer, (Map<?, ?>) o, showClasses, level + 1);
            } else if (Collection.class.isAssignableFrom(o.getClass())) {
                handleCollection(buffer, (Collection<?>) o, showClasses, level + 1);
            } else {
                buffer.append(o.getClass().getName()).append("{").append(o.toString()).append("}");
            }
        }

        buffer.append(LINE_BREAK);
        addSpaces(buffer, level);
        buffer.append("}");
        if (showClasses) {
            buffer.append("}");
        }
    }

    private static void handleCollection(@NotNull StringBuilder buffer, @NotNull Collection<?> collection, boolean showClasses, int level) {
        if (showClasses) {
            buffer.append(collection.getClass()).append("{");
        }
        buffer.append("[");

        boolean first = true;
        for (Object o : collection) {
            if (first) {
                first = false;
            } else {
                buffer.append(",");
            }
            buffer.append(LINE_BREAK);
            addSpaces(buffer, level + 1);
            if (o == null) {
                buffer.append("null");
            } else if (o.getClass().isArray()) {
                handleArray(buffer, (Object[]) o, showClasses, level + 1);
            } else if (Map.class.isAssignableFrom(o.getClass())) {
                handleMap(buffer, (Map<?, ?>) o, showClasses, level + 1);
            } else if (Collection.class.isAssignableFrom(o.getClass())) {
                handleCollection(buffer, (Collection<?>) o, showClasses, level + 1);
            } else {
                buffer.append(o.getClass().getName()).append("{").append(o.toString()).append("}");
            }
        }

        buffer.append(LINE_BREAK);
        addSpaces(buffer, level);
        buffer.append("]");
        if (showClasses) {
            buffer.append("}");
        }
    }

    private static void addSpaces(@NotNull StringBuilder buffer, int level) {
        for (int i = 0; i < level; i++) {
            buffer.append("  ");
        }
    }

    private static boolean isTypeSimple(@NotNull Class type) {
        return type.isPrimitive()
                || Number.class.isAssignableFrom(type)
                || String.class.isAssignableFrom(type)
                || BigDecimal.class.isAssignableFrom(type)
                || BigInteger.class.isAssignableFrom(type)
                || Enum.class.isAssignableFrom(type);
    }
}
