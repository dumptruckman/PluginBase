/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.permission;

/**
 * The default settings available for permissions.
 */
public enum PermDefault {
    /** All players have access by default. */
    TRUE,
    /** No players have access by default. */
    FALSE,
    /** Only server ops have access by default. */
    OP,
    /** Only players that are not server ops have access by default. */
    NOT_OP
}
