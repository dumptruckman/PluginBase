/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.properties;

import com.dumptruckman.minecraft.pluginbase.util.Logging;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

class StringStringSerializer<T> implements PropertySerializer<T> {

    private Constructor<T> constructor;

    public StringStringSerializer(Class<T> clazz) {
        try {
            constructor = clazz.getConstructor(String.class);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Class must be String.class!");
        }
    }

    @Override
    public T deserialize(Object obj) {
        try {
            return constructor.newInstance(obj.toString());
        } catch (IllegalAccessException e) {
            Logging.severe("Invalid usage of StringStringSerializer!  Somehow used something other than String.class!");
        } catch (InvocationTargetException e) {
            Logging.severe("new String(String) is throwing an exception:");
            e.printStackTrace();
        } catch (InstantiationException e) {
            Logging.severe("new String(String) is throwing an exception:");
            e.printStackTrace();
        }
        throw new IllegalStateException(this.getClass().getName() + " was used illegally!  Contact dumptruckman!");
    }

    @Override
    public Object serialize(T t) {
        return t;
    }
}
