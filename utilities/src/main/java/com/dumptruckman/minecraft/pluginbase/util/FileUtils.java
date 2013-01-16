/******************************************************************************
 * Multiverse 2 Copyright (c) the Multiverse Team 2011.                       *
 * Multiverse 2 is licensed under the BSD License.                            *
 * For more information please check the README.md file included              *
 * with this project.                                                         *
 ******************************************************************************/

package com.dumptruckman.minecraft.pluginbase.util;

import com.dumptruckman.minecraft.pluginbase.logging.Logging;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

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
                final File[] files = file.listFiles();
                if (files == null) {
                    Logging.finest("Error while retrieving file list for: %s", file);
                    return false;
                }
                for (final File f : files) {
                    result = result && deleteFolder(f);
                }
            }
            if (file.delete()) {
                return result;
            } else {
                Logging.finest("Could not delete file: %s", file);
                return false;
            }
        } else {
            Logging.finest("Oops, this file doesn't exist: %s", file);
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
                final File[] files = file.listFiles();
                if (files == null) {
                    Logging.finest("Error while retrieving file list for: %s", file);
                    return false;
                }
                for (final File f : files) {
                    result = result && deleteFolder(f);
                }
            }
            return result;
        } else {
            return false;
        }
    }

}
