package pluginbase.config.serializers;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;

class InstanceUtil {

    private static final Class[] EMPTY_PARAM_TYPE_ARRAY = new Class[0];
    private static final Object[] EMPTY_PARAM_VALUE_ARRAY = new Object[0];
    static final Class[] SIZE_PARAM_TYPE_ARRAY = new Class[] {Integer.class};

    @NotNull
    static <T> T createInstance(@NotNull Class<T> wantedType, @NotNull Class[] parameterTypes, @NotNull Object[] parameterValues) {
        try {
            Constructor<T> constructor = wantedType.getDeclaredConstructor(parameterTypes);
            boolean accessible = constructor.isAccessible();
            if (!accessible) {
                constructor.setAccessible(true);
            }
            try {
                return constructor.newInstance(parameterValues);
            } finally {
                if (!accessible) {
                    constructor.setAccessible(false);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    static <T> T createInstance(@NotNull Class<T> wantedType) {
        return createInstance(wantedType, EMPTY_PARAM_TYPE_ARRAY, EMPTY_PARAM_VALUE_ARRAY);
    }
}
