/* Copyright (c) 2013 dumptruckman
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.comments.yaml;

import com.dumptruckman.minecraft.pluginbase.comments.util.FileUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

/**
 * A utility for adding in comments to a YAML file.
 */
public class YamlFileCommentInstrumenter {

    static final String PATH_SEPARATOR = ".";
    static final char PATH_SEPARATOR_CHAR = PATH_SEPARATOR.charAt(0);
    static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private final YamlCommentMap commentMap;
    private final int indentLength;

    private StringBuilder finalFileContents;
    private StringBuilder lastYamlPath;
    private int lastNodeDepth;

    private String currentLine;

    /**
     * Creates an YamlFileCommentInstrumenter which will allow you to add comments to a YAML file.
     * <p/>
     * See {@link #setCommentsForPath(String, String...)} for linking comments to the nodes in the file yaml structure.
     * <p/>
     * <b>Note: </b> The indentation must be uniform in the file for the instrumentation to work correctly.
     * Behavior is unknown for files with non-uniform indentation.
     *
     * @param indentLength the number of spaces that constitutes a single indentation level.
     */
    public static YamlFileCommentInstrumenter createCommentInstrumenter(final int indentLength) {
        return new YamlFileCommentInstrumenter(YamlCommentMap.getYamlCommentMap(indentLength, PATH_SEPARATOR_CHAR), indentLength);
    }

    YamlFileCommentInstrumenter(@NotNull final YamlCommentMap commentMap, final int indentLength) {
        this.commentMap = commentMap;
        this.indentLength = indentLength;
    }

    /**
     * Sets the comments for a path in a yaml file.
     * <p/>
     * The comment can be multiple lines.  An empty string will indicate a blank line.
     *
     * @param path Configuration path to add comment.  Separate nodes in a path with a '.' (period).
     * @param comments Comments to add.  One String per line.
     */
    public void setCommentsForPath(@NotNull final String path, @NotNull final String... comments) {
        commentMap.setCommentsForPath(path, comments);
    }

    /**
     * Parses the given file (which must be in yaml format) and inserts comments.
     *
     * @throws IOException if the file does not exist, is a directory or cannot be read/written for some reason.
     */
    public void saveCommentsToFile(@NotNull final File yamlFile) throws IOException {
        if (commentMap.hasComments()) {
            String fileAsString = FileUtil.getFileContentsAsString(yamlFile);
            String instrumentedContents = addCommentsToYamlString(fileAsString);
            saveNewContentsToFile(instrumentedContents, yamlFile);
        }
    }

    String addCommentsToYamlString(@NotNull final String yamlString) {
        finalFileContents = new StringBuilder();
        lastYamlPath = new StringBuilder();
        lastNodeDepth = 0;

        final String[] yamlLines = splitStringByLines(yamlString);
        for (String line : yamlLines) {
            String instrumentedLine = instrumentLine(line);
            finalFileContents.append(instrumentedLine);
        }
        return finalFileContents.toString();
    }

    private String[] splitStringByLines(final String stringToSplit) {
        return stringToSplit.split(LINE_SEPARATOR);
    }

    private String instrumentLine(final String lineFromFile) {
        currentLine = lineFromFile;
        StringBuilder replacementForCurrentLine = new StringBuilder(currentLine);
        if (currentLineIsYamlNode()) {
            String comments = getCommentsForCurrentNode();
            if (comments != null && !comments.isEmpty()) {
                replacementForCurrentLine.insert(0, LINE_SEPARATOR);
                replacementForCurrentLine.insert(0, comments);
            }
        }
        replacementForCurrentLine.append(LINE_SEPARATOR);
        return replacementForCurrentLine.toString();
    }

    private boolean currentLineIsYamlNode() {
        String trimmedLine = currentLine.trim();
        return notEmptyOrComment(trimmedLine) &&
                (trimmedLine.contains(": ") || (trimmedLine.length() > 1 && trimmedLine.charAt(trimmedLine.length() - 1) == ':'));
    }

    private boolean notEmptyOrComment(String trimmedLine) {
        return !trimmedLine.isEmpty() && !trimmedLine.startsWith("#");
    }

    private String getCommentsForCurrentNode() {
        String currentNodePath = determineCurrentNodePath();
        return commentMap.getCommentsForPath(currentNodePath);
    }

    private String determineCurrentNodePath() {
        String currentNodeName = getCurrentNodeName();
        if (lastYamlPath.length() == 0) {
            // Only happens on first node in the file
            lastYamlPath.append(currentNodeName);
        } else {
            int currentNodeDepth = getCurrentNodeDepth();
            if (currentNodeDepth > lastNodeDepth) {
                lastYamlPath.append(PATH_SEPARATOR);
            } else if (currentNodeDepth < lastNodeDepth) {
                removeDepthFromPath(lastNodeDepth - currentNodeDepth);
                removeFinalNodeFromPath();
            } else {
                removeFinalNodeFromPath();
            }
            lastYamlPath.append(currentNodeName);
            lastNodeDepth = currentNodeDepth;
        }
        return lastYamlPath.toString();
    }

    private String getCurrentNodeName() {
        String trimmedLine = currentLine.trim();
        int nodeNameLength = getNodeNameLengthIncludingWhiteSpace(trimmedLine);
        return trimmedLine.substring(0, nodeNameLength);
    }

    private int getNodeNameLengthIncludingWhiteSpace(final String trimmedLine) {
        // TODO probably needs better detection of node name end
        int nodeNameLength = trimmedLine.indexOf(": ");
        if (nodeNameLength < 0) {
            nodeNameLength = trimmedLine.length() - 1;
        }
        return nodeNameLength;
    }

    private int getCurrentNodeDepth() {
        int currentWhiteSpaceAmount = getWhiteSpacePrecedingCurrentLine();
        return currentWhiteSpaceAmount / indentLength;
    }

    private int getWhiteSpacePrecedingCurrentLine() {
        int whiteSpace = 0;
        for (int n = 0; n < currentLine.length(); n++) {
            if (currentLine.charAt(n) == ' ') {
                whiteSpace++;
            } else {
                break;
            }
        }
        return whiteSpace;
    }

    private void removeDepthFromPath(int depthToRemove) {
        for (int i = 0; i < depthToRemove; i++) {
            lastYamlPath.delete(lastYamlPath.lastIndexOf(PATH_SEPARATOR), lastYamlPath.length());
        }
    }

    private void removeFinalNodeFromPath() {
        int finalPathSeparatorIndex = lastYamlPath.lastIndexOf(PATH_SEPARATOR);
        if (finalPathSeparatorIndex < 0) {
            lastYamlPath.delete(0, lastYamlPath.length());
        } else {
            lastYamlPath.delete(finalPathSeparatorIndex + 1, lastYamlPath.length());
        }
    }

    private void saveNewContentsToFile(final String newContents, final File file) throws IOException {
        FileUtil.writeStringToFile(newContents, file);
    }
}

