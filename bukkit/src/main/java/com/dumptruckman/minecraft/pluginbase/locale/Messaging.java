/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.locale;

/**
 * This interface is implemented by classes that use a {@link com.dumptruckman.minecraft.pluginbase.locale.Messager}.
 */
public interface Messaging {

    /**
     * @return The {@link com.dumptruckman.minecraft.pluginbase.locale.Messager} used by the Plugin.
     */
    Messager getMessager();

    /**
     * Sets the {@link com.dumptruckman.minecraft.pluginbase.locale.Messager} used by the Plugin.
     *
     * @param messager The new {@link com.dumptruckman.minecraft.pluginbase.locale.Messager}. Must not be null!
     */
    void setMessager(Messager messager);
}

