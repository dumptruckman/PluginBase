/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.messages;

/**
 * A marker interface used to indicate that something will be using localized messages.
 * <p/>
 * This is required to register messages for localization.  See {@link Messages#registerMessages(Localizable, Class)}.
 */
public interface Localizable { }
