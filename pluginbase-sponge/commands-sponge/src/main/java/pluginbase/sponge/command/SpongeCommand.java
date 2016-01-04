package pluginbase.sponge.command;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import pluginbase.command.Command;
import pluginbase.command.CommandProvider;
import pluginbase.messages.Message;
import pluginbase.minecraft.BasePlayer;
import pluginbase.permission.Perm;
import pluginbase.sponge.minecraft.SpongeTools;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    // FIXME BAD THINGS WERE DONE TO GET THINGS FLOWING AGAIN, UNTESTED CODE! - SupaHam
    @NotNull
    @Override
    public CommandResult process(@NotNull CommandSource commandSource, @NotNull String arguments) throws CommandException {
        List<String> args = Arrays.asList(arguments.split("\\s"));
        BasePlayer basePlayer = SpongeTools.wrapSender(commandSource);
        try {
            return provider.getCommandHandler().locateAndRunCommand(basePlayer, args.toArray(new String[args.size()]))
                    ? CommandResult.success() : CommandResult.empty();
        } catch (pluginbase.command.CommandException e) {
            e.sendException(provider.getMessager(), basePlayer);
        }
        return CommandResult.empty();
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
    public Optional<? extends Text> getShortDescription(@NotNull CommandSource commandSource) {
        return Optional.<Text>of(Text.of(desc));
    }

    @NotNull
    @Override
    public Optional<? extends Text> getHelp(@NotNull CommandSource commandSource) {
        Message helpMessage = command.getHelp();
        if (helpMessage != null) {
            String localizedHelp = provider.getMessager().getLocalizedMessage(helpMessage);
            return Optional.<Text>of(Text.of(localizedHelp));
        }
        return Optional.empty();
    }

    @NotNull
    @Override
    public Text getUsage(@NotNull CommandSource commandSource) {
        return Text.of(usage);
    }

    @NotNull
    @Override
    public List<String> getSuggestions(@NotNull CommandSource commandSource, @NotNull String s) throws CommandException {
        return provider.getCommandHandler().tabComplete(SpongeTools.wrapSender(commandSource), s.split("\\s"));
    }
}
