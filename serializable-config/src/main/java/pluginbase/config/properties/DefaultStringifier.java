package pluginbase.config.properties;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DefaultStringifier implements Stringifier {

    private static final Map<Class, Method> VALUE_OF_METHODS = new HashMap<Class, Method>();
    private static final Map<Class, Class> PRIMITIVE_WRAPPER_MAP;

    static {
        Map<Class, Class> map = new HashMap<Class, Class>();
        map.put(int.class, Integer.class);
        map.put(boolean.class, Boolean.class);
        map.put(long.class, Long.class);
        map.put(double.class, Double.class);
        map.put(float.class, Float.class);
        map.put(byte.class, Byte.class);
        map.put(short.class, Short.class);
        PRIMITIVE_WRAPPER_MAP = Collections.unmodifiableMap(map);
    }

    protected Method getValueOfMethod(Class clazz) throws NoSuchMethodException {
        Method method = VALUE_OF_METHODS.get(clazz);
        if (method == null) {
            method = cacheValueOfMethod(clazz);
        }
        return method;
    }

    private static Method cacheValueOfMethod(Class clazz) throws NoSuchMethodException {
        Method method = clazz.getDeclaredMethod("valueOf", String.class);
        if (Modifier.isStatic(method.getModifiers())) {
            method.setAccessible(true);
            VALUE_OF_METHODS.put(clazz, method);
            return method;
        }
        throw new NoSuchMethodException("valueOf(String) method is not static.");
    }

    @NotNull
    @Override
    public String toString(@NotNull Object value) {
        if (value instanceof Enum) {
            return ((Enum) value).name();
        }
        return value.toString();
    }

    @NotNull
    @Override
    public Object valueOf(@NotNull String value, @NotNull Class desiredType) throws IllegalArgumentException {
        if (PRIMITIVE_WRAPPER_MAP.containsKey(desiredType)) {
            desiredType = PRIMITIVE_WRAPPER_MAP.get(desiredType);
        }
        if (desiredType.equals(String.class)) {
            return value;
        }
        if (Enum.class.isAssignableFrom(desiredType)) {
            EnumSet<?> enumValues = EnumSet.allOf(desiredType);
            for (Enum e : enumValues) {
                if (e.name().equalsIgnoreCase(value)) {
                    return e;
                }
            }
        }
        try {
            Method method = getValueOfMethod(desiredType);
            return method.invoke(null, value);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
