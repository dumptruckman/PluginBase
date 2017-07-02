/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.plugin.command.builtin;

import pluginbase.command.CommandContext;
import pluginbase.command.CommandInfo;
import pluginbase.messages.Message;
import pluginbase.messages.Messages;
import pluginbase.messages.Theme;
import pluginbase.minecraft.BasePlayer;
import pluginbase.permission.Perm;
import pluginbase.permission.PermFactory;
import pluginbase.plugin.PluginBase;
import pluginbase.util.webpaste.BitlyURLShortener;
import pluginbase.util.webpaste.URLShortener;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Produces version information related to this plugin.
 * <p/>
 * Specific information for the plugin can be displayed and is determined by {@link pluginbase.plugin.PluginBase#dumpVersionInfo()}.
 * Also has the option to output to pastebin or pastie.
 */
@CommandInfo(
        primaryAlias = "version",
        desc = "Prints useful version information to the console.",
        flags = "pbh"
)
public class VersionCommand extends BuiltInCommand {

    public final static Message VERSION_HELP = Messages.createMessage("cmd.version.help",
            Theme.HELP + "Displays version and other helpful information about the plugin."
                    + "\n" + Theme.HELP + "Flags:"
                    + "\n" + Theme.CMD_FLAG + "  -p " + Theme.HELP + "will output an http://pastie.org url containing the information."
                    + "\n" + Theme.CMD_FLAG + "  -b " + Theme.HELP + "will output an http://pastebin.com url containing the information.");
    public final static Message VERSION_PLAYER = Messages.createMessage("cmd.version.player",
            Theme.INFO.toString() + Theme.IMPORTANT2 + "Version info dumped to console. Please check your server logs.");
    public final static Message VERSION_PLUGIN_VERSION = Messages.createMessage("cmd.version.info.plugin_version", "%s Version: %s");
    public final static Message VERSION_SERVER_NAME = Messages.createMessage("cmd.version.info.server_name", "Server Name: %s");
    public final static Message VERSION_SERVER_VERSION = Messages.createMessage("cmd.version.info.server_version", "Server Version: %s");
    public final static Message VERSION_LANG_FILE = Messages.createMessage("cmd.version.info.lang_file", "Language file: %s");
    public final static Message VERSION_DEBUG_MODE = Messages.createMessage("cmd.version.info.debug_mode", "Debug Mode: %s");
    public final static Message VERSION_INFO_DUMPED = Messages.createMessage("cmd.version.dumped", Theme.INFO.toString() + Theme.IMPORTANT + "Version info dumped here: " + Theme.VALUE + Theme.IMPORTANT3 + "%s");

    private static final URLShortener SHORTENER = new BitlyURLShortener();

    private final Perm perm;

    protected VersionCommand(@NotNull final PluginBase plugin) {
        super(plugin);
        perm = PermFactory.newPerm(plugin.getPluginClass(), "cmd.version").usePluginName().commandPermission()
                .desc("Sends version information to the console.").build();
    }

    /** {@inheritDoc} */
    @Override
    public Perm getPerm() {
        return perm;
    }

    /** {@inheritDoc} */
    @Override
    public Message getHelp() {
        return VERSION_HELP;
    }

    /** {@inheritDoc} */
    @Override
    public boolean runCommand(@NotNull final BasePlayer sender, @NotNull final CommandContext context) {
        // Check if the command was sent from a Player.
        if (sender.isPlayer()) {
            getMessager().message(sender, VERSION_PLAYER);
        }

        final List<String> versionInfoDump = getCommandProvider().dumpVersionInfo();

        // send info to sender
        for (String line : versionInfoDump) {
            getMessager().message(sender, line);
        }

        final Set<Character> flags = new LinkedHashSet<Character>(context.getFlags());
        if (!flags.isEmpty()) {
            getCommandProvider().getServerInterface().runTaskAsynchronously(new PasteServiceTask(getCommandProvider(), flags, versionInfoDump, sender, VERSION_INFO_DUMPED));
        }
        return true;
    }
}
