/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.config;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Indicates that comments can be added.
 */
public interface CommentedFile {

    /**
     * Adds comments just before the specified path.  The comment can be multiple lines.  An empty string will indicate a blank line.
     *
     * @param path         Configuration path to add comment.
     * @param commentLines Comments to add.  One String per line.
     */
    void addComments(@NotNull final String path, @NotNull final List<String> commentLines);
}
