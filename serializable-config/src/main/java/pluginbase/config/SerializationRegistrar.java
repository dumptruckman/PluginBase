package pluginbase.config;

import pluginbase.config.annotation.SerializableAs;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.logging.Logging;
import pluginbase.logging.PluginLogger;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public enum SerializationRegistrar {
    ;

    private static Set<Class> serializationEligibleClasses = new HashSet<Class>();
    private static Map<String, Class> aliases = new HashMap<String, Class>();

    /**
     * Registers the given class to be eligible for serialization.
     */
    public static void registerClass(@NotNull Class clazz) {
        try {
            clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            Logging.warning("SerializationRegistrar: Class '%s' is missing a 0-arg constructor and may cause deserialization issues.", clazz.getName());
        }
        serializationEligibleClasses.add(clazz);

        registerClassAlias(clazz, getAlias(clazz));
        registerClassAlias(clazz, clazz.getName());
    }

    private static void registerClassAlias(@NotNull Class clazz, @NotNull String alias) {
        aliases.put(alias, clazz);
    }

    /**
     * Removes the given class from serialization eligibility.
     */
    public static void unregisterClass(@NotNull Class clazz) {
        serializationEligibleClasses.remove(clazz);
        while (aliases.values().remove(clazz)) { }
    }

    /**
     * Attempts to get a registered class by its alias.
     *
     * @param alias Alias of the serializable class.
     * @return Registered class, or null if not found.
     */
    @Nullable
    public static Class getClassByAlias(String alias) {
        return aliases.get(alias);
    }

    /**
     * Tells whether the given class has been registered for serialization services.
     *
     * @return {@code true} if the given class is serialization eligible.
     */
    public static boolean isClassRegistered(Class clazz) {
        return serializationEligibleClasses.contains(clazz);
    }

    /**
     * Gets the correct alias for the given class.
     *
     * @return Alias to use for the class.
     */
    @NotNull
    public static String getAlias(@NotNull Class clazz) {
        SerializableAs alias = (SerializableAs) clazz.getAnnotation(SerializableAs.class);

        if ((alias != null) && (alias.value() != null)) {
            return alias.value();
        }

        return clazz.getName();
    }

    public static Set<Class> getRegisteredClasses() {
        return Collections.unmodifiableSet(serializationEligibleClasses);
    }
}
