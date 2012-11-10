/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.properties;

class DefaultValidator implements PropertyValidator {

    public boolean isValid(Object obj) {
        return true;
    }

    /*
    @Override
    public Message getInvalidMessage() {
        return Messages.BLANK;
    }
    */
}
