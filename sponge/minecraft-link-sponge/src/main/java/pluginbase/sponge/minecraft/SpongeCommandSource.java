package pluginbase.sponge.minecraft;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.util.command.CommandSource;

public class SpongeCommandSource extends AbstractSpongeCommandSource<CommandSource> {

    public SpongeCommandSource(@NotNull CommandSource source) {
        super(source);
    }

    @Override
    public boolean isPlayer() {
        return false;
    }
}
