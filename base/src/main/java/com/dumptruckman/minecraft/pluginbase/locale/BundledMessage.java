/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.locale;

public class BundledMessage {

    private Message message;
    private Object[] args;

    public BundledMessage(Message message, Object...args) {
        this.message = message;
        this.args = args;
    }

    public Message getMessage() {
        return message;
    }

    public Object[] getArgs() {
        return args;
    }
}
