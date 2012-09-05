/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.config;

import com.dumptruckman.minecraft.pluginbase.locale.Message;

/**
 * Used to validate the serialized value of an entry before attempting to deserialize it.
 */
public interface EntryValidator {

    boolean isValid(Object obj);

    Message getInvalidMessage();
}
