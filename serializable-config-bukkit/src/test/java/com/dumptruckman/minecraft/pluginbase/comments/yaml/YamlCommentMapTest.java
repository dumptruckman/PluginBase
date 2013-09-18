package com.dumptruckman.minecraft.pluginbase.comments.yaml;

import org.junit.Test;

public class YamlCommentMapTest {

    @Test
    public void testCommentsForPath() throws Exception {
        YamlCommentMap yamlCommentMap = YamlCommentMap.getYamlCommentMap(2);
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
