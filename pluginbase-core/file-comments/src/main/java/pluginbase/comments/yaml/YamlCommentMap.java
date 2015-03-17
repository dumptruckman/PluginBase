/* Copyright (c) 2013 dumptruckman
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.comments.yaml;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class YamlCommentMap {

    static final String COMMENT_LINE_START = "# ";
    static final String LINE_SEPARATOR = System.getProperty("line.separator");

    @NotNull private final String indentLengthString;
    private final char pathSeparatorChar;
    @NotNull private final Map<String, String> commentMap = new ConcurrentHashMap<String, String>();
    @NotNull private final ThreadLocal<String> leadingWhitespace = new ThreadLocal<String>();

    public static YamlCommentMap getYamlCommentMap(final int indentLength, final char pathSeparatorChar) {
        return new YamlCommentMap(indentLength, pathSeparatorChar);
    }

    private YamlCommentMap(final int indentLength, final char pathSeparatorChar) {
        indentLengthString = createIndentString(indentLength);
        this.pathSeparatorChar = pathSeparatorChar;
    }

    private String createIndentString(final int indentLength) {
        StringBuilder indentBuilder = new StringBuilder();
        for (int i = 0; i < indentLength; i++) {
            indentBuilder.append(" ");
        }
        return indentBuilder.toString();
    }

    /**
     * Sets the comments for a path in a yaml file.
     * <p/>
     * The comment can be multiple lines.  An empty string will indicate a blank line.
     *
     * @param path Configuration path to add comment.  Must use the path separator character specified with {@link YamlCommentMap#getYamlCommentMap(int, char)}
     * @param comments Comments to add.  One String per line.
     */
    public void setCommentsForPath(@NotNull final String path, @NotNull final String... comments) {
        leadingWhitespace.set(getLeadingWhitespaceForPathDepth(path));
        String mergedComments = mergeArrayIntoSingleMultiLineIndentedString(comments);

        commentMap.put(path, mergedComments);
    }

    private String getLeadingWhitespaceForPathDepth(final String path) {
        final StringBuilder leadingSpaces = new StringBuilder();
        for (int n = 0; n < path.length(); n++) {
            if (path.charAt(n) == pathSeparatorChar) {
                leadingSpaces.append(indentLengthString);
            }
        }
        return leadingSpaces.toString();
    }

    private String mergeArrayIntoSingleMultiLineIndentedString(final String[] comments) {
        StringBuilder commentsBuilder = new StringBuilder();
        for (final String comment : comments) {
            StringBuilder commentLineBuilder = prepareCommentLine(comment);
            commentsBuilder = appendPreparedCommentLine(commentsBuilder, commentLineBuilder);
        }
        return commentsBuilder.toString();
    }

    private StringBuilder appendPreparedCommentLine(final StringBuilder commentsBuilder, final StringBuilder preparedCommentLine) {
        if (commentsBuilder.length() > 0) {
            commentsBuilder.append(LINE_SEPARATOR);
        }
        commentsBuilder.append(preparedCommentLine);
        return commentsBuilder;
    }

    private StringBuilder prepareCommentLine(final String initialCommentLine) {
        StringBuilder preparedCommentLine = new StringBuilder(initialCommentLine);
        if (preparedCommentLine.length() > 0) {
            preparedCommentLine = prepareNonEmptyLine(preparedCommentLine);
        } else {
            preparedCommentLine.append(" ");
        }
        return preparedCommentLine;
    }

    private StringBuilder prepareNonEmptyLine(StringBuilder commentLine) {
        commentLine = prependCommentTagIfMissing(commentLine);
        commentLine.insert(0, leadingWhitespace.get());
        return commentLine;
    }

    private StringBuilder prependCommentTagIfMissing(final StringBuilder commentLine) {
        if (commentLine.charAt(0) != '#') {
            commentLine.insert(0, COMMENT_LINE_START);
        }
        return commentLine;
    }

    /**
     * Retrieves the comments for a path in a yaml file.
     * <p/>
     * The path must utilize the same path separator character specified with {@link YamlCommentMap#getYamlCommentMap(int, char)}
     * <p/>
     * This will return a single String which will be properly formatted for a yaml file and the given path.
     */
    @NotNull
    public String getCommentsForPath(@NotNull final String path) {
        String commentsForPath = commentMap.get(path);
        if (commentsForPath == null) {
            commentsForPath = "";
        }
        return commentsForPath;
    }

    /**
     * Returns true if any comments have been defined in this comment map.
     */
    public boolean hasComments() {
        return !commentMap.isEmpty();
    }
}
