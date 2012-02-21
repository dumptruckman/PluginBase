package com.dumptruckman.tools.locale;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An enum containing all messages/strings used by SimpleCircuits.
 */
public enum Message {
    // BEGIN CHECKSTYLE-SUPPRESSION: Javadoc
    TEST_STRING("a test-string from the enum"),

    // Generic Messages
    GENERIC_ERROR("[Error]"),
    GENERIC_SUCCESS("[Success]"),
    GENERIC_INFO("[Info]"),
    GENERIC_HELP("[Help]"),
    GENERIC_OFF("OFF"),

    // Reload Command
    RELOAD_COMPLETE("&b===[ Reload Complete! ]==="),

    // DebugCommand
    INVALID_DEBUG("&fInvalid debug level.  Please use number 0-3.  &b(3 being many many messages!)"),
    DEBUG_SET("Debug mode is %1");

    // BEGIN CHECKSTYLE-SUPPRESSION: Javadoc

    private final List<String> def;

    Message(String def, String... extra) {
        this.def = new ArrayList<String>();
        this.def.add(def);
        this.def.addAll(Arrays.asList(extra));
    }

    /**
     * @return This {@link Message}'s default-message
     */
    public List<String> getDefault() {
        return def;
    }

}

