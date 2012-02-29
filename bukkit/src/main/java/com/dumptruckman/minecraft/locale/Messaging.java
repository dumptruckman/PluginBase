package com.dumptruckman.minecraft.locale;

/**
 * This interface is implemented by classes that use a {@link com.dumptruckman.minecraft.locale.Messager}.
 */
public interface Messaging {

    /**
     * @return The {@link com.dumptruckman.minecraft.locale.Messager} used by the Plugin.
     */
    Messager getMessager();

    /**
     * Sets the {@link com.dumptruckman.minecraft.locale.Messager} used by the Plugin.
     *
     * @param messager The new {@link com.dumptruckman.minecraft.locale.Messager}. Must not be null!
     */
    void setMessager(Messager messager);
}

