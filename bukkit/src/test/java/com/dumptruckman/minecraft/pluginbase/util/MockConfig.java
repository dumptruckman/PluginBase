package com.dumptruckman.minecraft.pluginbase.util;

import com.dumptruckman.minecraft.pluginbase.config.AbstractYamlProperties;
import com.dumptruckman.minecraft.pluginbase.config.BaseConfig;
import com.dumptruckman.minecraft.pluginbase.config.EntryBuilder;
import com.dumptruckman.minecraft.pluginbase.config.ListEntry;
import com.dumptruckman.minecraft.pluginbase.config.MappedEntry;
import com.dumptruckman.minecraft.pluginbase.config.SimpleEntry;
import com.dumptruckman.minecraft.pluginbase.plugin.BukkitPlugin;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class MockConfig extends AbstractYamlProperties implements BaseConfig {
    
    public static final SimpleEntry<Boolean> TEST = new EntryBuilder<Boolean>(Boolean.class, "test").def(true)
            .comment("# ===[ PluginBase Test ]===").build();
    public static final SimpleEntry<Null> SETTINGS = new EntryBuilder<Null>(Null.class, "settings")
            .comment("# ===[ PluginBase Settings ]===").build();
    
    public static final MappedEntry<Integer> SPECIFIC_TEST = new EntryBuilder<Integer>(Integer.class, "specific_test")
            .buildMap();

    public static final ListEntry<Integer> LIST_TEST = new EntryBuilder<Integer>(Integer.class, "list_test")
            .buildList(LinkedList.class);
    
    public MockConfig(BukkitPlugin plugin, boolean doComments, File configFile, Class<? extends MockConfig> configClass) throws IOException {
        super(plugin, doComments, true, configFile, configClass);
    }

    @Override
    protected String getHeader() {
        return "# ===[ PluginBase Config ]===";
    }
}
