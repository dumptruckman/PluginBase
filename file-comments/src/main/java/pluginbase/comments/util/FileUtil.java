/* Copyright (c) 2013 dumptruckman
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.comments.util;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

public class FileUtil {

    private static final int BUFFER_SIZE = 1024;

    /**
     * Pass a file and it will return it's contents as a string.
     *
     * @param file File to read.
     * @return Contents of file.  String will be empty in case of any errors.
     */
    public static String getFileContentsAsString(@NotNull final File file) throws IOException {
        if (!file.exists())
            throw new IOException("File " + file + " does not exist");
        if (!file.canRead())
            throw new IOException("Cannot read file " + file);
        if (file.isDirectory())
            throw new IOException("File " + file + " is directory");

        Writer writer = new StringWriter();
        InputStream is = null;
        Reader reader = null;
        try {
            is = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            int numberOfCharsRead;
            char[] buffer = new char[BUFFER_SIZE];
            while ((numberOfCharsRead = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, numberOfCharsRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignore) {
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignore) {
                }
            }
        }
        return writer.toString();
    }

    public static void writeStringToFile(@NotNull String sourceString, @NotNull final File destinationFile) throws IOException {
        OutputStreamWriter out = null;
        try {
            sourceString = sourceString.replaceAll("\n", System.getProperty("line.separator"));

            out = new OutputStreamWriter(new FileOutputStream(destinationFile), "UTF-8");
            out.write(sourceString);
            out.close();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ignore) {
                }
            }
        }
    }
}
