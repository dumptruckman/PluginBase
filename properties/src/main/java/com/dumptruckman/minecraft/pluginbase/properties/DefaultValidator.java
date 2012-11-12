/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.properties;

import com.dumptruckman.minecraft.pluginbase.messaging.Message;
import com.dumptruckman.minecraft.pluginbase.messaging.Messages;

class DefaultValidator implements PropertyValidator<Object> {

    public boolean isValid(Object obj) {
        return true;
    }

    @Override
    public Message getInvalidMessage() {
        return Messages.BLANK;
    }
}
