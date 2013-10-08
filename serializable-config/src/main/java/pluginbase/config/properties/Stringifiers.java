package pluginbase.config.properties;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public enum Stringifiers {
    ;

    private static final Map<Class<? extends Stringifier>, Stringifier> STRINGIFIER_MAP = new HashMap<Class<? extends Stringifier>, Stringifier>();

    public static <S extends Stringifier> Stringifier getStringifier(Class<S> stringifierClass) {
        if (STRINGIFIER_MAP.containsKey(stringifierClass)) {
            return STRINGIFIER_MAP.get(stringifierClass);
        }
        try {
            Constructor<S> constructor = stringifierClass.getDeclaredConstructor();
            boolean accessible = constructor.isAccessible();
            if (!accessible) {
                constructor.setAccessible(true);
            }
            S serializer = constructor.newInstance();
            registerStringifierInstance(stringifierClass, serializer);
            if (!accessible) {
                constructor.setAccessible(false);
            }
            return serializer;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static Stringifier getDefaultStringifier() {
        return getStringifier(DefaultStringifier.class);
    }

    public static void setDefaultStringifier(Stringifier stringifier) {
        STRINGIFIER_MAP.put(DefaultStringifier.class, stringifier);
    }

    public static <T extends Stringifier> void registerStringifierInstance(Class<T> stringifierClass, T stringifier) {
        STRINGIFIER_MAP.put(stringifierClass, stringifier);
    }
}
