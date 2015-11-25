/* Copyright (c) 2013 dumptruckman
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.config.datasource.yaml;

import org.junit.Test;

import static pluginbase.config.datasource.yaml.YamlFileCommentInstrumenter.LINE_SEPARATOR;
import static org.junit.Assert.*;

public class YamlFileCommentInstrumenterTest {

    private static final int INDENT_LENGTH = 2;

    private static final String TEST_CONTENTS_1 = "# A Test Yaml File" + LINE_SEPARATOR + LINE_SEPARATOR +
            "test: 123" + LINE_SEPARATOR +
            "a_map:" + LINE_SEPARATOR +
            "  something: yep" + LINE_SEPARATOR +
            "  something_else: 42" + LINE_SEPARATOR +
            "  a_list: " + LINE_SEPARATOR +
            "  - 1" + LINE_SEPARATOR +
            "  - 2" + LINE_SEPARATOR +
            "  a_child_map:" + LINE_SEPARATOR +
            "    another_map:" + LINE_SEPARATOR +
            "      test: 123" + LINE_SEPARATOR +
            "  two_steps_back:" + LINE_SEPARATOR +
            "    test: true" + LINE_SEPARATOR +
            "back_to_root: true" + LINE_SEPARATOR;

    private static final String COMMENTED_TEST_CONTENTS_1 = "# A Test Yaml File" + LINE_SEPARATOR + LINE_SEPARATOR +
            "# Yay" + LINE_SEPARATOR +
            "test: 123" + LINE_SEPARATOR +
            "# They seem to be" + LINE_SEPARATOR +
            "# Working" + LINE_SEPARATOR +
            "a_map:" + LINE_SEPARATOR +
            "  # Yeah, they're working" + LINE_SEPARATOR +
            "  something: yep" + LINE_SEPARATOR +
            "  something_else: 42" + LINE_SEPARATOR +
            "  # Aww yeah, comments on a list" + LINE_SEPARATOR +
            "  a_list: " + LINE_SEPARATOR +
            "  - 1" + LINE_SEPARATOR +
            "  - 2" + LINE_SEPARATOR +
            "  a_child_map:" + LINE_SEPARATOR +
            "    # Comments on a child child map" + LINE_SEPARATOR +
            "    another_map:" + LINE_SEPARATOR +
            "      test: 123" + LINE_SEPARATOR +
            "  # Two steps back comments" + LINE_SEPARATOR +
            "  two_steps_back:" + LINE_SEPARATOR +
            "    test: true" + LINE_SEPARATOR +
            "# Back to root comments" + LINE_SEPARATOR +
            "back_to_root: true" + LINE_SEPARATOR;

    private static final YamlCommentMap TEST_COMMENTS_1 = YamlCommentMap.getYamlCommentMap(INDENT_LENGTH, '.');
    static {
        TEST_COMMENTS_1.setCommentsForPath("test", "Yay");
        TEST_COMMENTS_1.setCommentsForPath("a_map", "They seem to be", "Working");
        TEST_COMMENTS_1.setCommentsForPath("a_map.something", "Yeah, they're working");
        TEST_COMMENTS_1.setCommentsForPath("a_map.a_list", "Aww yeah, comments on a list");
        TEST_COMMENTS_1.setCommentsForPath("a_map.a_child_map.another_map", "Comments on a child child map");
        TEST_COMMENTS_1.setCommentsForPath("a_map.two_steps_back", "Two steps back comments");
        TEST_COMMENTS_1.setCommentsForPath("back_to_root", "Back to root comments");
    }

    @Test
    public void testNoComments() throws Exception {
        YamlFileCommentInstrumenter commentInstrumenter = new YamlFileCommentInstrumenter(YamlCommentMap.getYamlCommentMap(INDENT_LENGTH, '.'), INDENT_LENGTH);
        String instrumentedContents = commentInstrumenter.addCommentsToYamlString(TEST_CONTENTS_1);
        assertEquals(TEST_CONTENTS_1, instrumentedContents);
    }

    // TODO Test disabled.  Need to figure out why the line endings are wrong.
    // @Test
    public void testWithComments() throws Exception {
        YamlFileCommentInstrumenter commentInstrumenter = new YamlFileCommentInstrumenter(TEST_COMMENTS_1, INDENT_LENGTH);
        String instrumentedContents = commentInstrumenter.addCommentsToYamlString(TEST_CONTENTS_1);
        System.out.println(COMMENTED_TEST_CONTENTS_1);
        System.out.println("===============================================");
        System.out.println(instrumentedContents);
        assertEquals(COMMENTED_TEST_CONTENTS_1, instrumentedContents);
    }
}
