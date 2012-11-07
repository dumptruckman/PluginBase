package com.dumptruckman.minecraft.pluginbase.util;

import com.dumptruckman.minecraft.pluginbase.config.BaseConfig;
import com.dumptruckman.minecraft.pluginbase.plugin.BukkitPlugin;
import com.dumptruckman.minecraft.pluginbase.properties.AbstractYamlProperties;
import com.dumptruckman.minecraft.pluginbase.properties.ListProperty;
import com.dumptruckman.minecraft.pluginbase.properties.MappedProperty;
import com.dumptruckman.minecraft.pluginbase.properties.NullProperty;
import com.dumptruckman.minecraft.pluginbase.properties.PropertyFactory;
import com.dumptruckman.minecraft.pluginbase.properties.SimpleProperty;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class MockConfig extends AbstractYamlProperties implements BaseConfig {
    
    public static final SimpleProperty<Boolean> TEST = PropertyFactory.newProperty(Boolean.class, "test", true)
            .comment("# ===[ PluginBase Test ]===").build();
    public static final NullProperty SETTINGS = PropertyFactory.newNullProperty("settings")
            .comment("# ===[ PluginBase Settings ]===").build();
    
    public static final MappedProperty<Integer> SPECIFIC_TEST = PropertyFactory.newMappedProperty(Integer.class, "specific_test")
            .build();

    public static final ListProperty<Integer> LIST_TEST = PropertyFactory.newListProperty(Integer.class, "list_test", new LinkedList<Integer>())
            .build();
    
    public MockConfig(BukkitPlugin plugin, boolean doComments, File configFile, Class<? extends MockConfig> configClass) throws IOException {
        super(plugin, doComments, true, configFile, configClass);
    }

    @Override
    protected String getHeader() {
        return "# ===[ PluginBase Config ]===";
    }
}
