package com.dumptruckman.minecraft.pluginbase.properties;

import com.dumptruckman.minecraft.pluginbase.plugin.BukkitPlugin;
import org.bukkit.configuration.ConfigurationSection;

class NestedYamlProperties extends AbstractYamlProperties implements NestedProperties {

    private final String name;
    private final ConfigurationSection parentSection;
    private final ConfigurationSection thisSection;
    private final String fullName;

    NestedYamlProperties(final BukkitPlugin plugin, final CommentedYamlConfiguration config,
                         final AbstractYamlProperties parent, final String name,
                         final Class... configClasses) {
        super(plugin, config, configClasses);
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

    protected ConfigurationSection getConfig() {
        return thisSection;
    }

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
    protected void serializeAll(ConfigurationSection newConfig) {
        super.serializeAll(newConfig);
        parentSection.set(this.name, getConfig());

    }

    @Override
    protected void setDefaults() {
        super.setDefaults();
        parentSection.set(this.name, getConfig());
    }
}
