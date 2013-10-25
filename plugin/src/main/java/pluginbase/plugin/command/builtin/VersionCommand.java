/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.plugin.command.builtin;

import pluginbase.command.CommandContext;
import pluginbase.command.CommandInfo;
import pluginbase.logging.PluginLogger;
import pluginbase.messages.Message;
import pluginbase.messages.Theme;
import pluginbase.minecraft.BasePlayer;
import pluginbase.permission.Perm;
import pluginbase.permission.PermFactory;
import pluginbase.plugin.PluginBase;
import pluginbase.util.webpaste.BitlyURLShortener;
import pluginbase.util.webpaste.PasteFailedException;
import pluginbase.util.webpaste.PasteService;
import pluginbase.util.webpaste.PasteServiceFactory;
import pluginbase.util.webpaste.PasteServiceType;
import pluginbase.util.webpaste.URLShortener;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

/**
 * Produces version information related to this plugin.
 * <p/>
 * Specific information for the plugin can be displayed and is determined by {@link pluginbase.plugin.PluginBase#dumpVersionInfo()}.
 * Also has the option to output to pastebin or pastie.
 */
@CommandInfo(
        primaryAlias = "version",
        desc = "Prints useful version information to the console.",
        flags = "pb"
)
public class VersionCommand extends BuiltInCommand {

    /** Permission for version command. */
    public static final Perm PERMISSION = PermFactory.newPerm(PluginBase.class, "cmd.version").usePluginName().commandPermission()
            .desc("Sends version information to the console.").build();

    public final static Message VERSION_HELP = Message.createMessage("cmd.version.help",
            Theme.HELP + "Displays version and other helpful information about the plugin."
                    + "\n" + Theme.HELP + "Flags:"
                    + "\n" + Theme.CMD_FLAG + "  -p " + Theme.HELP + "will output an http://pastie.org url containing the information."
                    + "\n" + Theme.CMD_FLAG + "  -b " + Theme.HELP + "will output an http://pastebin.com url containing the information.");
    public final static Message VERSION_PLAYER = Message.createMessage("cmd.version.player",
            Theme.INFO.toString() + Theme.IMPORTANT2 + "Version info dumped to console. Please check your server logs.");
    public final static Message VERSION_PLUGIN_VERSION = Message.createMessage("cmd.version.info.plugin_version", "%s Version: %s");
    public final static Message VERSION_SERVER_NAME = Message.createMessage("cmd.version.info.server_name", "Server Name: %s");
    public final static Message VERSION_SERVER_VERSION = Message.createMessage("cmd.version.info.server_version", "Server Version: %s");
    public final static Message VERSION_LANG_FILE = Message.createMessage("cmd.version.info.lang_file", "Language file: %s");
    public final static Message VERSION_DEBUG_MODE = Message.createMessage("cmd.version.info.debug_mode", "Debug Mode: %s");
    public final static Message VERSION_INFO_DUMPED = Message.createMessage("cmd.version.dumped", Theme.INFO.toString() + Theme.IMPORTANT + "Version info dumped here: " + Theme.VALUE + Theme.IMPORTANT3 + "%s");

    private static final URLShortener SHORTENER = new BitlyURLShortener();

    protected VersionCommand(@NotNull final PluginBase plugin) {
        super(plugin);
    }

    /** {@inheritDoc} */
    @Override
    public Perm getPerm() {
        return PERMISSION;
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

        final List<String> versionInfoDump = getPlugin().dumpVersionInfo();

        // send info to sender
        for (String line : versionInfoDump) {
            getMessager().message(sender, line);
        }

        final Set<Character> flags = new LinkedHashSet<Character>(context.getFlags());
        if (!flags.isEmpty()) {
            getPlugin().getServerInterface().runTaskAsynchronously(getPlugin(), new Runnable() {
                @Override
                public void run() {
                    for (Character flag : flags) {
                        final String pasteUrl;
                        if (flag.equals('p')) {
                            pasteUrl = postToService(PasteServiceType.PASTIE, true, versionInfoDump, getPlugin().getLog());
                        } else if (flag.equals('b')) {
                            pasteUrl = postToService(PasteServiceType.PASTEBIN, true, versionInfoDump, getPlugin().getLog());
                        } else {
                            continue;
                        }
                        getPlugin().getServerInterface().runTask(getPlugin(), new Runnable() {
                            @Override
                            public void run() {
                                getMessager().message(sender, VERSION_INFO_DUMPED, pasteUrl);
                            }
                        });
                    }
                }
            });
        }
        return true;
    }

    /**
     * Send the current contents of this.pasteBinBuffer to a web service.
     *
     * @param type      Service type to send to
     * @param isPrivate Should the paste be marked as private.
     * @return URL of visible paste
     */
    private static String postToService(PasteServiceType type, boolean isPrivate, List<String> pasteData, @NotNull final PluginLogger logger) {
        StringBuilder buffer = new StringBuilder();
        for (String data : pasteData) {
            if (!buffer.toString().isEmpty()) {
                buffer.append('\n');
            }
            buffer.append(data);
        }
        PasteService ps = PasteServiceFactory.getService(type, isPrivate);
        try {
            return SHORTENER.shorten(ps.postData(ps.encodeData(buffer.toString()), ps.getPostURL()));
        } catch (PasteFailedException e) {
            logger.log(Level.WARNING, "Error pasting version information: ", e);
            return "Error posting to service";
        }
    }
}
