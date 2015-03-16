package pluginbase.sponge.command;

import com.google.common.base.Optional;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandSource;
import pluginbase.command.Command;
import pluginbase.command.CommandProvider;
import pluginbase.messages.Message;
import pluginbase.minecraft.BasePlayer;
import pluginbase.permission.Perm;
import pluginbase.sponge.minecraft.SpongeTools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class SpongeCommand implements CommandCallable {

    private final CommandProvider provider;
    private final Command command;
    private final String usage;
    private final String desc;

    SpongeCommand(@NotNull CommandProvider provider, @NotNull Command command, @NotNull String usage, @NotNull String desc) {
        this.provider = provider;
        this.command = command;
        this.usage = usage;
        this.desc = desc;
    }

    @Override
    public boolean call(@NotNull CommandSource commandSource, @NotNull String arguments, @NotNull List<String> parents) throws CommandException {
        List<String> args = Arrays.asList(arguments.split("\\s"));
        List<String> allArgs = new ArrayList<>(parents.size() + args.size());
        allArgs.addAll(parents);
        allArgs.addAll(args);
        BasePlayer basePlayer = SpongeTools.wrapSender(commandSource);
        try {
            return provider.getCommandHandler().locateAndRunCommand(basePlayer, allArgs.toArray(new String[allArgs.size()]));
        } catch (pluginbase.command.CommandException e) {
            e.sendException(provider.getMessager(), basePlayer);
        }
        return false;
    }

    @Override
    public boolean testPermission(@NotNull CommandSource commandSource) {
        Perm perm = command.getPerm();
        if (perm != null) {
            perm.hasPermission(SpongeTools.wrapSender(commandSource));
        }
        return true;
    }

    @NotNull
    @Override
    public Optional<String> getShortDescription() {
        return Optional.of(desc);
    }

    @NotNull
    @Override
    public Optional<String> getHelp() {
        Message helpMessage = command.getHelp();
        if (helpMessage != null) {
            String localizedHelp = provider.getMessager().getLocalizedMessage(helpMessage);
            return Optional.of(localizedHelp);
        }
        return Optional.absent();
    }

    @NotNull
    @Override
    public String getUsage() {
        return usage;
    }

    @NotNull
    @Override
    public List<String> getSuggestions(@NotNull CommandSource commandSource, @NotNull String s) throws CommandException {
        return provider.getCommandHandler().tabComplete(SpongeTools.wrapSender(commandSource), s.split("\\s"));
    }
}
