package com.dumptruckman.minecraft.pluginbase.util;

import com.dumptruckman.minecraft.pluginbase.config.BaseConfig;
import com.dumptruckman.minecraft.pluginbase.plugin.BukkitPlugin;
import com.dumptruckman.minecraft.pluginbase.properties.AbstractYamlProperties;
import com.dumptruckman.minecraft.pluginbase.properties.ListProperty;
import com.dumptruckman.minecraft.pluginbase.properties.MappedProperty;
import com.dumptruckman.minecraft.pluginbase.properties.PropertyBuilder;
import com.dumptruckman.minecraft.pluginbase.properties.SimpleProperty;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class MockConfig extends AbstractYamlProperties implements BaseConfig {
    
    public static final SimpleProperty<Boolean> TEST = new PropertyBuilder<Boolean>(Boolean.class, "test").def(true)
            .comment("# ===[ PluginBase Test ]===").build();
    public static final SimpleProperty<Null> SETTINGS = new PropertyBuilder<Null>(Null.class, "settings")
            .comment("# ===[ PluginBase Settings ]===").build();
    
    public static final MappedProperty<Integer> SPECIFIC_TEST = new PropertyBuilder<Integer>(Integer.class, "specific_test")
            .buildMap();

    public static final ListProperty<Integer> LIST_TEST = new PropertyBuilder<Integer>(Integer.class, "list_test")
            .buildList(LinkedList.class);
    
    public MockConfig(BukkitPlugin plugin, boolean doComments, File configFile, Class<? extends MockConfig> configClass) throws IOException {
        super(plugin, doComments, true, configFile, configClass);
    }

    @Override
    protected String getHeader() {
        return "# ===[ PluginBase Config ]===";
    }
}
