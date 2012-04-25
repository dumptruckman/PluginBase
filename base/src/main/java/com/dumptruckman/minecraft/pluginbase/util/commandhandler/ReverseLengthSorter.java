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