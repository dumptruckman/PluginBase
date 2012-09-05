/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.config;

import java.util.Map;

public interface MappedConfigEntry<T> extends ConfigEntry<T> {

    Map<String, T> getNewTypeMap();

    Map<String, T> getDefaultMap();
}
