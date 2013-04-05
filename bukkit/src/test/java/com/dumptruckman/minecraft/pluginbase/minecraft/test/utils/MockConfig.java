package com.dumptruckman.minecraft.pluginbase.minecraft.test.utils;

import com.dumptruckman.minecraft.pluginbase.config.AbstractFileConfig;
import com.dumptruckman.minecraft.pluginbase.config.BaseConfig;
import com.dumptruckman.minecraft.pluginbase.config.ConfigEntry;
import com.dumptruckman.minecraft.pluginbase.config.EntryBuilder;
import com.dumptruckman.minecraft.pluginbase.config.ListConfigEntry;
import com.dumptruckman.minecraft.pluginbase.config.MappedConfigEntry;
import com.dumptruckman.minecraft.pluginbase.plugin.BukkitPlugin;
import com.dumptruckman.minecraft.pluginbase.util.Null;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class MockConfig extends AbstractFileConfig<MockConfig> implements BaseConfig {
    
    public static final ConfigEntry<Boolean> TEST = new EntryBuilder<Boolean>(Boolean.class, "test").def(true)
            .comment("# ===[ PluginBase Test ]===").build();
    public static final ConfigEntry<Null> SETTINGS = new EntryBuilder<Null>(Null.class, "settings")
            .comment("# ===[ PluginBase Settings ]===").build();
    
    public static final MappedConfigEntry<Integer> SPECIFIC_TEST = new EntryBuilder<Integer>(Integer.class, "specific_test")
            .buildMap();

    public static final ListConfigEntry<Integer> LIST_TEST = new EntryBuilder<Integer>(Integer.class, "list_test")
            .buildList(LinkedList.class);
    
    public MockConfig(BukkitPlugin plugin, boolean doComments, File configFile, Class<? extends MockConfig> configClass) throws IOException {
        super(plugin, doComments, true, configFile, new YamlConfiguration(), configClass);
    }

    @Override
    protected String getHeader() {
        return "# ===[ PluginBase Config ]===";
    }
}
