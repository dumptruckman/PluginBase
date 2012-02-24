package com.dumptruckman.tools.config;

public class ConfigEntry {

    private String path;
    private Object def;
    private String[] comments;

    public ConfigEntry(String path, Object def, String... comments) {
        this.path = path;
        this.def = def;
        this.comments = comments;
    }

    /**
     * Retrieves the path for a config option.
     *
     * @return The path for a config option.
     */
    String getPath() {
        return this.path;
    }

    /**
     * Retrieves the default value for a config path.
     *
     * @return The default value for a config path.
     */
    Object getDefault() {
        return this.def;
    }

    /**
     * Retrieves the comment for a config path.
     *
     * @return The comments for a config path.
     */
    String[] getComments() {
        if (this.comments != null) {
            return this.comments;
        }

        String[] emptyComments = new String[1];
        emptyComments[0] = "";
        return emptyComments;
    }
}
