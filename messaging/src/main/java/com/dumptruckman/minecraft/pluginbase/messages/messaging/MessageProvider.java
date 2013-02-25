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
package com.dumptruckman.minecraft.pluginbase.messages.messaging;

import com.dumptruckman.minecraft.pluginbase.messages.Message;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * Contains methods for retrieving messages from a persistent set of localized messages.
 */
public interface MessageProvider {

    /** The default locale. */
    @NotNull
    Locale DEFAULT_LOCALE = Locale.ENGLISH;
    /** The default message/language file */
    @NotNull
    String DEFAULT_LANGUAGE_FILE_NAME = "english.txt";

    /**
     * Returns a message (as {@link String}) for the specified key (as {@link com.dumptruckman.minecraft.pluginbase.messages.Messages}).
     *
     * @param key  The key
     * @param args Args for String.format()
     * @return The message
     */
    @NotNull
    String getMessage(@NotNull Message key, Object... args);

    /**
     * Returns the Locale this MessageProvider is currently using.
     *
     * @return The locale this MessageProvider is currently using.
     */
    @NotNull
    Locale getLocale();

    /**
     * Sets the locale for this MessageProvider.
     *
     * @param locale The new {@link java.util.Locale}.
     */
    void setLocale(@NotNull final Locale locale);

    /**
     * Loads the given language file which will be used for localized messages with this message provider.
     * <p/>
     * If the file does not exist it will be created and populated with the default values.
     *
     * @param languageFileName the name of the file to use.
     */
    void loadLanguageFile(@NotNull final String languageFileName);

    /**
     * Removes any messages in the currently loaded language file that does not exist in set of registered messages.
     * <p/>
     * Registered messages would be messages created using {@link Message#Message(String, String)}.
     */
    void pruneLanguageFile();
}

