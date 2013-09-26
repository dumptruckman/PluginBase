package pluginbase.config.field;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public enum Validators {
    ;

    private static Map<Class<? extends Validator>, Validator> validatorMap = new HashMap<Class<? extends Validator>, Validator>();

    public static <T> Validator<T> getValidator(Class<? extends Validator<T>> validatorClass) {
        if (validatorMap.containsKey(validatorClass)) {
            return validatorMap.get(validatorClass);
        }
        Constructor constructor = null;
        boolean accessible = true;
        try {
            constructor = validatorClass.getDeclaredConstructor();
            accessible = constructor.isAccessible();
            if (!accessible) {
                constructor.setAccessible(true);
            }
            Validator validator = (Validator) constructor.newInstance();
            validatorMap.put(validatorClass, validator);
            return validator;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } finally {
            if (constructor != null && !accessible) {
                constructor.setAccessible(accessible);
            }
        }
    }
}
