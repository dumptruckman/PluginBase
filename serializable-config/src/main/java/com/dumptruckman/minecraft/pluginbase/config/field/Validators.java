package com.dumptruckman.minecraft.pluginbase.config.field;

import com.dumptruckman.minecraft.pluginbase.config.Validator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

class Validators {

    private static Map<Class<? extends Validator>, Validator> validatorMap = new HashMap<Class<? extends Validator>, Validator>();

    static Validator getValidator(Class<? extends Validator> validatorClass) {
        if (validatorMap.containsKey(validatorClass)) {
            return validatorMap.get(validatorClass);
        }
        try {
            Constructor constructor = validatorClass.getDeclaredConstructor();
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
        }
    }
}
