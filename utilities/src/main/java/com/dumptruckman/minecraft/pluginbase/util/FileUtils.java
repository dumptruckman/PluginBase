/******************************************************************************
 * Multiverse 2 Copyright (c) the Multiverse Team 2011.                       *
 * Multiverse 2 is licensed under the BSD License.                            *
 * For more information please check the README.md file included              *
 * with this project.                                                         *
 ******************************************************************************/

package com.dumptruckman.minecraft.pluginbase.util;

import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * File-utilities.
 */
public class FileUtils {

    protected FileUtils() {
        throw new AssertionError();
    }

    /**
     * Used to delete a folder.
     *
     * @param file The folder to delete.
     * @return true if the folder was successfully deleted.
     */
    public static boolean deleteFolder(@NotNull final File file) {
        if (file.exists()) {
            boolean result = true;
            // If the file exists, and it has more than one file in it.
            if (file.isDirectory()) {
                for (final File f : file.listFiles()) {
                    result = result && deleteFolder(f);
                    if (!FileUtils.deleteFolder(f)) {
                        return false;
                    }
                }
            }
            return result && file.delete();
        } else {
            return false;
        }
    }

    /**
     * Used to delete the contents of a folder, without deleting the folder itself.
     *
     * @param file The folder whose contents to delete.
     * @return true if the contents were successfully deleted
     */
    public static boolean deleteFolderContents(@NotNull final File file) {
        if (file.exists()) {
            boolean result = true;
            // If the file exists, and it has more than one file in it.
            if (file.isDirectory()) {
                for (final File f : file.listFiles()) {
                    result = result && deleteFolder(f);
                }
            }
            return result;
        } else {
            return false;
        }
    }

}
