import com.google.common.base.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.api.entity.player.Player;
import pluginbase.minecraft.BasePlayer;
import pluginbase.plugin.ServerInterface;
import pluginbase.sponge.minecraft.SpongeTools;

import java.io.File;

class SpongeServerInterface implements ServerInterface {

    @NotNull
    @Override
    public String getName() {
        return SpongeTools.getGame().getPlatform().name();
    }

    @NotNull
    @Override
    public String getVersion() {
        return SpongeTools.getGame().getApiVersion();
    }

    @NotNull
    @Override
    public File getWorldContainer() {
        return new File(".");
    }

    @NotNull
    @Override
    public File getServerFolder() {
        return new File(".");
    }

    @Nullable
    @Override
    public BasePlayer getPlayer(String name) {
        Optional<Player> opPlayer = SpongeTools.getServer().getPlayer(name);
        if (!opPlayer.isPresent()) {
            return null;
        }
        return SpongeTools.wrapPlayer(opPlayer.get());
    }

    @Override
    public int runTask(@NotNull Runnable runnable) {
        return 0;
    }

    @Override
    public int runTaskAsynchronously(@NotNull Runnable runnable) {
        return 0;
    }

    @Override
    public int runTaskLater(@NotNull Runnable runnable, long delay) {
        return 0;
    }

    @Override
    public int runTaskLaterAsynchronously(@NotNull Runnable runnable, long delay) {
        return 0;
    }

    @Override
    public int runTaskTimer(@NotNull Runnable runnable, long delay, long period) {
        return 0;
    }

    @Override
    public int runTaskTimerAsynchronously(@NotNull Runnable runnable, long delay, long period) {
        return 0;
    }
}
