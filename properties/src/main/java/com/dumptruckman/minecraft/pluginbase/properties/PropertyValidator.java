/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.properties;

import com.dumptruckman.minecraft.pluginbase.messaging.Message;

/**
 * Used to validate the serialized value of an entry before attempting to deserialize it.
 */
public interface PropertyValidator<T> {

    boolean isValid(T obj);

    Message getInvalidMessage();
}
