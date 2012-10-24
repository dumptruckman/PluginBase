package com.dumptruckman.minecraft.pluginbase.plugin.command;

import java.util.HashMap;
import java.util.Map;

class CommandKey {

    private final String key;
    private final boolean command;
    private Map<String, CommandKey> commandKeys;

    CommandKey(final String key, final boolean command) {
        this.key = key;
        this.command = command;
    }

    CommandKey(CommandKey previousKey) {
        this.key = previousKey.key;
        this.command = true;
        this.commandKeys = previousKey.commandKeys;
    }

    String getName() {
        return key;
    }

    boolean isCommand() {
        return command;
    }

    private void initMap() {
        if (commandKeys == null) {
            commandKeys = new HashMap<String, CommandKey>();
        }
    }

    CommandKey getKey(final String key) {
        initMap();
        return commandKeys.get(key);
    }

    CommandKey newKey(final String key, final boolean command) {
        initMap();
        if (commandKeys.containsKey(key)) {
            if (command) {
                commandKeys.put(key, new CommandKey(commandKeys.get(key)));
            }
            return commandKeys.get(key);
        } else {
            final CommandKey commandKey = new CommandKey(getName() + " " + key, command);
            commandKeys.put(key, commandKey);
            return commandKey;
        }
    }
}
