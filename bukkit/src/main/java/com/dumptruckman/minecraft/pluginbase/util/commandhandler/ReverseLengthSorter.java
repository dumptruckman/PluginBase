/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.util.commandhandler;

import java.util.Comparator;

public class ReverseLengthSorter implements Comparator<CommandKey> {
    public int compare(CommandKey cmdA, CommandKey cmdB) {
        if (cmdA.getKey().length() > cmdB.getKey().length()) {
            return -1;
        } else if (cmdA.getKey().length() < cmdB.getKey().length()) {
            return 1;
        } else {
            return 0;
        }
    }
}