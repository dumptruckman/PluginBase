package pluginbase.sponge.minecraft;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.util.command.CommandSource;
import pluginbase.messages.ChatColor;

public class SpongeCommandSource extends AbstractSpongeCommandSource<CommandSource> {

    public SpongeCommandSource(@NotNull CommandSource source) {
        super(source);
    }

    @Override
    public boolean isPlayer() {
        return false;
    }

    @Override
    public void sendMessage(@NotNull String message) {
        // TODO remove stripColor when console supports color
        super.sendMessage(ChatColor.stripColor(message));
    }
}
