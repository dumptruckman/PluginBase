package pluginbase.config.util;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A utility class for switched primitive classes with their respective wrapper class.
 */
public enum PrimitivesUtil {
    ;

    /** Contains a mapping of primitive classes to their object forms. */
    protected static final Map<Class, Class> PRIMITIVE_WRAPPER_MAP;

    static {
        Map<Class, Class> map = new HashMap<>();
        map.put(int.class, Integer.class);
        map.put(boolean.class, Boolean.class);
        map.put(long.class, Long.class);
        map.put(double.class, Double.class);
        map.put(float.class, Float.class);
        map.put(byte.class, Byte.class);
        map.put(short.class, Short.class);
        map.put(char.class, Character.class);
        PRIMITIVE_WRAPPER_MAP = Collections.unmodifiableMap(map);
    }

    /**
     * Switches any primitive classes with their respective wrapper class. Returns the input argument directly for
     * non-primitive classes.
     *
     * @param clazz the class to switch, if primitive.
     * @return the primitive wrapper class for a primitive class input or the input argument if not a primitive class.
     */
    @NotNull
    public static Class switchForWrapper(@NotNull Class clazz) {
        if (PRIMITIVE_WRAPPER_MAP.containsKey(clazz)) {
            clazz = PRIMITIVE_WRAPPER_MAP.get(clazz);
        }
        return clazz;
    }
}
