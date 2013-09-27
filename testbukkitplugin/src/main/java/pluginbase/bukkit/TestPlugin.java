package pluginbase.bukkit;

import org.jetbrains.annotations.NotNull;

public class TestPlugin extends AbstractBukkitPlugin {

    @NotNull
    @Override
    public String getCommandPrefix() {
        return "pb";
    }

    @Override
    protected boolean useDatabase() {
        return false;
    }
}
