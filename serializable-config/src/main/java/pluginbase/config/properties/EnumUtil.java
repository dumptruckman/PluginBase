package pluginbase.config.properties;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public enum EnumUtil {
    ;

    /**
     * Finds an enum value by name with case insensitive matching.
     * <p/>
     * <em>WARNING:</em> it is possible that the return value will not be the expected value due to the fact that an
     * enum can have values with the same name in different cases.
     *
     * @param enumClass the enum class.
     * @param name the name to look for.
     * @return the first matching enum value or null if none match.
     */
    @Nullable
    public static Enum matchEnum(@NotNull Class<? extends Enum> enumClass, @NotNull String name) {
        EnumSet<?> enumValues = EnumSet.allOf(enumClass);
        for (Enum e : enumValues) {
            if (e.name().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }
}
