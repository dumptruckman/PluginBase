/* Copyright (c) 2013 dumptruckman
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.comments.yaml;

import org.junit.Test;

import static org.junit.Assert.*;

public class YamlCommentMapTest {

    @Test
    public void testCommentsForPath() throws Exception {
        YamlCommentMap yamlCommentMap = YamlCommentMap.getYamlCommentMap(2, '.');
        yamlCommentMap.setCommentsForPath("some.path", "test");

        assertEquals("", yamlCommentMap.getCommentsForPath("some"));
        assertEquals("  # test", yamlCommentMap.getCommentsForPath("some.path"));

        yamlCommentMap.setCommentsForPath("some.path");
        assertEquals("", yamlCommentMap.getCommentsForPath("some.path"));

        yamlCommentMap.setCommentsForPath("some", "Test", "", "# Okay", " # Three");
        assertEquals("# Test" + YamlCommentMap.LINE_SEPARATOR + " " + YamlCommentMap.LINE_SEPARATOR + "# Okay" + YamlCommentMap.LINE_SEPARATOR + "#  # Three", yamlCommentMap.getCommentsForPath("some"));

        yamlCommentMap.setCommentsForPath("some.path", "Test", "", "# Okay", " # Three");
        assertEquals("  # Test" + YamlCommentMap.LINE_SEPARATOR + " " + YamlCommentMap.LINE_SEPARATOR + "  # Okay" + YamlCommentMap.LINE_SEPARATOR + "  #  # Three", yamlCommentMap.getCommentsForPath("some.path"));
    }
}
