package pluginbase.util;

import pluginbase.bukkit.AbstractBukkitPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class MockPlugin extends AbstractBukkitPlugin {

    @Override
    public void onPluginLoad() {
        MockMessages.init();
        //HelpCommand.addStaticPrefixedKey("");
    }

    @Override
    public void onPluginEnable() {
        //getCommandHandler().registerCommand(new MockQueuedCommand(this));
    }

    @NotNull
    @Override
    public String getCommandPrefix() {
        return "pb";
    }

    @Nullable
    @Override
    public List<String> dumpVersionInfo() {
        List<String> versionInfo = new LinkedList<String>();
        versionInfo.add("Test");
        return versionInfo;
    }

    @NotNull
    @Override
    protected MockConfig getDefaultSettings() {
        return new MockConfig(this);
    }

    @NotNull
    @Override
    public MockConfig getSettings() {
        return (MockConfig) super.getSettings();
    }

    @Override
    protected boolean useDatabase() {
        return true;
    }
}
