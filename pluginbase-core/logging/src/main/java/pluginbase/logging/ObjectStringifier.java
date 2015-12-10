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

            Object value;
            try {
                value = field.get(o);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                continue;
            }

            if (clazz.isAssignableFrom(field.getType()) && value != null) {
                if (showClasses) {
                    buffer.append(clazz).append("{");
                }
                buffer.append(value.toString());
                if (showClasses) {
                    buffer.append(clazz).append("}");
                }
            } else {
                handleValue(buffer, value, showClasses, level);
            }

            if (!accessible) {
                field.setAccessible(false);
            }
        }
    }

    private static void handleValue(@NotNull StringBuilder buffer, @Nullable Object value, boolean showClasses, int level) {
        if (value == null) {
            buffer.append("null");
            return;
        }
        Class clazz = value.getClass();
        if (showClasses) {
            buffer.append(clazz).append("{");
        }
        if (clazz.isArray()) {
            handleArray(buffer, (Object[]) value, showClasses, level);
        } else if (Map.class.isAssignableFrom(clazz)) {
            handleMap(buffer, (Map<?, ?>) value, showClasses, level);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            handleCollection(buffer, (Collection<?>) value, showClasses, level);
        } else if (clazz.isPrimitive()
                || String.class.isAssignableFrom(clazz)
                || Number.class.isAssignableFrom(clazz)
                || Boolean.class.isAssignableFrom(clazz)
                || Character.class.isAssignableFrom(clazz)) {
            buffer.append(value.toString());
        } else {
            buffer.append(showClasses ? clazz.getName() : clazz.getSimpleName()).append("{");
            buildUp(buffer, clazz, value, showClasses, level + 1);
            buffer.append(LINE_BREAK);
            addSpaces(buffer, level);
            buffer.append("}");
        }
        if (showClasses) {
            buffer.append(clazz).append("}");
        }
    }

    private static void handleArray(@NotNull StringBuilder buffer, @NotNull Object[] array, boolean showClasses, int level) {
        if (!showClasses) {
            buffer.append(array.getClass().getSimpleName());
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
            handleValue(buffer, o, showClasses, level + 1);
        }

        buffer.append(LINE_BREAK);
        addSpaces(buffer, level);
        buffer.append("]");
    }

    private static void handleMap(@NotNull StringBuilder buffer, @NotNull Map<?, ?> map, boolean showClasses, int level) {
        if (!showClasses) {
            buffer.append(map.getClass().getSimpleName());
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
            handleValue(buffer, o, showClasses, level + 1);
        }

        buffer.append(LINE_BREAK);
        addSpaces(buffer, level);
        buffer.append("}");
    }

    private static void handleCollection(@NotNull StringBuilder buffer, @NotNull Collection<?> collection, boolean showClasses, int level) {
        if (!showClasses) {
            buffer.append(collection.getClass().getSimpleName());
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
            handleValue(buffer, o, showClasses, level + 1);
        }

        buffer.append(LINE_BREAK);
        addSpaces(buffer, level);
        buffer.append("]");
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
