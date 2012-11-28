/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.entity;

import com.dumptruckman.minecraft.pluginbase.messaging.MessageReceiver;
import com.dumptruckman.minecraft.pluginbase.permission.Perm;
import com.dumptruckman.minecraft.pluginbase.permission.Permissible;

public abstract class BasePlayer implements MessageReceiver, Permissible {

    public abstract String getName();

    public abstract boolean hasPermission(String perm);

    public abstract void sendMessage(String message);

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof BasePlayer)) {
            return false;
        }
        final BasePlayer other2 = (BasePlayer) other;
        return other2.getName().equals(getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    public void hasPerm(Perm perm) {
        perm.hasPermission(this);
    }

    public void hasPerm(Perm perm, String specific) {
        perm.hasPermission(this, specific);
    }

    public void checkPermission(String permission) {
        if (!hasPermission(permission)) {
            //throw new WorldEditPermissionException();
        }
    }

    public boolean isPlayer() {
        return true;
    }
}
