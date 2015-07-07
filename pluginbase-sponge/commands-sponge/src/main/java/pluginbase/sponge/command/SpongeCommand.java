package pluginbase.sponge.command;

import com.google.common.base.Optional;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import pluginbase.command.Command;
import pluginbase.command.CommandProvider;
import pluginbase.messages.Message;
import pluginbase.minecraft.BasePlayer;
import pluginbase.permission.Perm;
import pluginbase.sponge.minecraft.SpongeTools;

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

    // FIXME BAD THINGS WERE DONE TO GET THINGS FLOWING AGAIN, UNTESTED CODE! - SupaHam
    @NotNull
    @Override
    public Optional<CommandResult> process(@NotNull CommandSource commandSource, @NotNull String arguments) throws CommandException {
        List<String> args = Arrays.asList(arguments.split("\\s"));
        BasePlayer basePlayer = SpongeTools.wrapSender(commandSource);
        try {
            return Optional.of(provider.getCommandHandler().locateAndRunCommand(basePlayer, args.toArray(new String[args.size()])) 
                    ? CommandResult.success() : CommandResult.empty());
        } catch (pluginbase.command.CommandException e) {
            e.sendException(provider.getMessager(), basePlayer);
        }
        return Optional.of(CommandResult.empty());
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
    public Optional<Text> getShortDescription(@NotNull CommandSource commandSource) {
        return Optional.<Text>of(Texts.of(desc));
    }

    @NotNull
    @Override
    public Optional<Text> getHelp(@NotNull CommandSource commandSource) {
        Message helpMessage = command.getHelp();
        if (helpMessage != null) {
            String localizedHelp = provider.getMessager().getLocalizedMessage(helpMessage);
            return Optional.<Text>of(Texts.of(localizedHelp));
        }
        return Optional.absent();
    }

    @NotNull
    @Override
    public Text getUsage(@NotNull CommandSource commandSource) {
        return Texts.of(usage);
    }

    @NotNull
    @Override
    public List<String> getSuggestions(@NotNull CommandSource commandSource, @NotNull String s) throws CommandException {
        return provider.getCommandHandler().tabComplete(SpongeTools.wrapSender(commandSource), s.split("\\s"));
    }
}
