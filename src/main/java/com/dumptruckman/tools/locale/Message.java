/**
 * Copyright (c) 2011, The Multiverse Team All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of The Multiverse Team nor the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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

