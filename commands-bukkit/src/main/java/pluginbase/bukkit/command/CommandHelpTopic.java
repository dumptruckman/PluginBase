package pluginbase.bukkit.command;

import pluginbase.messages.Message;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.help.GenericCommandHelpTopic;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class CommandHelpTopic extends GenericCommandHelpTopic {

    @Nullable
    private final Message helpMessage;

    CommandHelpTopic(@NotNull final DynamicPluginCommand command, @Nullable final Message helpMessage) {
        super(command);
        this.helpMessage = helpMessage;
    }

    @Override
    public String getFullText(final CommandSender forWho) {
        final StringBuilder buffer = new StringBuilder();
        buffer.append(super.getFullText(forWho));
        if (helpMessage != null) {
            final DynamicPluginCommand cmd = (DynamicPluginCommand) command;
            buffer.append("\n");
            buffer.append(ChatColor.GOLD);
            buffer.append("Help: ");
            buffer.append(cmd.registeredWith.getMessager().getLocalizedMessage(helpMessage));
        }
        return buffer.toString();
    }
}
