package com.dumptruckman.minecraft.pluginbase.bukkit.properties;

import com.dumptruckman.minecraft.pluginbase.logging.PluginLogger;
import com.dumptruckman.minecraft.pluginbase.properties.NestedProperties;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

class NestedFileProperties extends AbstractFileProperties implements NestedProperties {

    private final String name;
    private final ConfigurationSection parentSection;
    private final ConfigurationSection thisSection;
    private final String fullName;

    NestedFileProperties(@NotNull final PluginLogger logger, @NotNull final FileConfiguration config,
                         @NotNull final AbstractFileProperties parent, @NotNull final String name,
                         @NotNull final Class... configClasses) {
        super(logger, config, configClasses);
        this.parentSection = parent.getConfig();
        this.name = name;
        ConfigurationSection section = this.parentSection.getConfigurationSection(this.name);
        if (section == null) {
            section = this.parentSection.createSection(this.name);
        }
        thisSection = section;
        if (parent.getName().isEmpty()) {
            this.fullName = name;
        } else {
            this.fullName = parent.getName() + getConfigOptions().pathSeparator() + name;
        }
    }

    @NotNull
    @Override
    protected ConfigurationSection getConfig() {
        return thisSection;
    }

    @NotNull
    @Override
    protected String getName() {
        return fullName;
    }

    @Override
    protected void deserializeAll() {
        super.deserializeAll();
        parentSection.set(this.name, getConfig());
    }

    @Override
    protected void serializeAll(@NotNull final ConfigurationSection newConfig) {
        super.serializeAll(newConfig);
        parentSection.set(this.name, getConfig());

    }

    @Override
    protected void setDefaults() {
        super.setDefaults();
        parentSection.set(this.name, getConfig());
    }

    @Override
    public void flush() { }

    @Override
    public void reload() { }
}
