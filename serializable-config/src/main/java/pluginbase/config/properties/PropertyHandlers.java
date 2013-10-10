package pluginbase.config.properties;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public enum PropertyHandlers {
    ;

    private static final Map<Class<? extends PropertyHandler>, PropertyHandler> HANDLER_MAP = new HashMap<Class<? extends PropertyHandler>, PropertyHandler>();

    public static <T extends PropertyHandler> PropertyHandler getHandler(Class<T> handlerClass) {
        if (HANDLER_MAP.containsKey(handlerClass)) {
            return HANDLER_MAP.get(handlerClass);
        }
        try {
            Constructor<T> constructor = handlerClass.getDeclaredConstructor();
            boolean accessible = constructor.isAccessible();
            if (!accessible) {
                constructor.setAccessible(true);
            }
            T handler = constructor.newInstance();
            registerHandlerInstance(handlerClass, handler);
            if (!accessible) {
                constructor.setAccessible(false);
            }
            return handler;
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

    public static PropertyHandler<Object> getDefaultHandler() {
        return getHandler(DefaultHandler.class);
    }

    public static void setDefaultHandler(PropertyHandler<Object> handler) {
        HANDLER_MAP.put(DefaultHandler.class, handler);
    }

    public static <T extends PropertyHandler> void registerHandlerInstance(Class<T> handlerClass, T handler) {
        HANDLER_MAP.put(handlerClass, handler);
    }
}
