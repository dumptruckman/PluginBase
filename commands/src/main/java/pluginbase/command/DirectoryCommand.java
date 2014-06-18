package pluginbase.command;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.messages.Message;
import pluginbase.messages.Theme;
import pluginbase.messages.messaging.Messaging;
import pluginbase.minecraft.BasePlayer;
import pluginbase.permission.Permission;

class DirectoryCommand<P extends CommandProvider & Messaging> extends Command<P> {

    public DirectoryCommand(@NotNull P plugin) {
        super(plugin);
    }

    @Nullable
    @Override
    public Permission getPermission() {
        return null;
    }

    @Nullable
    @Override
    public Message getHelp() {
        return null;
    }

    @Override
    public boolean runCommand(@NotNull BasePlayer sender, @NotNull CommandContext context) {
        return false;
    }

    public void runCommand(@NotNull BasePlayer sender, @NotNull String label, @NotNull CommandTree commandTree) {
        StringBuilder builder = new StringBuilder();
        int count = 0;
        for (String subCommand : commandTree.getSubCommandSet()) {
            count++;
            if (count > 1) {
                builder.append('\n');
            }
            if (count % 2 == 0) {
                builder.append(Theme.LIST_EVEN);
            } else {
                builder.append(Theme.LIST_ODD);
            }
            builder.append(subCommand);
        }
        getMessager().message(sender, CommandHandler.SUB_COMMAND_LIST, label, builder.toString());
    }
}
