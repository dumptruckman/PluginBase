/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.command.builtin;

import com.dumptruckman.minecraft.pluginbase.command.CommandInfo;
import com.dumptruckman.minecraft.pluginbase.config.BaseConfig;
import com.dumptruckman.minecraft.pluginbase.logging.Logging;
import com.dumptruckman.minecraft.pluginbase.messaging.Message;
import com.dumptruckman.minecraft.pluginbase.minecraft.BasePlayer;
import com.dumptruckman.minecraft.pluginbase.permission.Perm;
import com.dumptruckman.minecraft.pluginbase.plugin.PluginBase;
import com.dumptruckman.minecraft.pluginbase.util.webpaste.BitlyURLShortener;
import com.dumptruckman.minecraft.pluginbase.util.webpaste.PasteFailedException;
import com.dumptruckman.minecraft.pluginbase.util.webpaste.PasteService;
import com.dumptruckman.minecraft.pluginbase.util.webpaste.PasteServiceFactory;
import com.dumptruckman.minecraft.pluginbase.util.webpaste.PasteServiceType;
import com.dumptruckman.minecraft.pluginbase.util.webpaste.URLShortener;
import com.sk89q.minecraft.util.commands.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

/**
 * Enables debug-information.
 */
@CommandInfo(
        primaryAlias = "version",
        desc = "Prints useful version information to the console.",
        flags = "pb",
        usage = "[-p|-b]"
)
public class VersionCommand extends BaseBuiltInCommand {

    private static final URLShortener SHORTENER = new BitlyURLShortener();

    private static final List<String> staticKeys = new ArrayList<String>();

    public static void addStaticAlias(@NotNull final String key) {
        staticKeys.add(key);
    }

    protected VersionCommand(@NotNull final PluginBase plugin) {
        super(plugin);
    }

    @Override
    public List<String> getStaticAliases() {
        return staticKeys;
    }

    @Override
    public Perm getPerm() {
        return CommandPerms.COMMAND_VERSION;
    }

    @Override
    public Message getHelp() {
        return CommandMessages.VERSION_HELP;
    }

    @Override
    public boolean runCommand(@NotNull final BasePlayer sender, @NotNull final CommandContext context) {
        // Check if the command was sent from a Player.
        if (sender.isPlayer()) {
            getMessager().message(sender, CommandMessages.VERSION_PLAYER);
        }

        final List<String> buffer = new LinkedList<String>();
        buffer.add(getMessager().getMessage(CommandMessages.VERSION_PLUGIN_VERSION, getPlugin().getPluginInfo().getName(), getPlugin().getPluginInfo().getVersion()));
        buffer.add(getMessager().getMessage(CommandMessages.VERSION_SERVER_NAME, getPlugin().getServerInterface().getName()));
        buffer.add(getMessager().getMessage(CommandMessages.VERSION_SERVER_VERSION, getPlugin().getServerInterface().getVersion()));
        buffer.add(getMessager().getMessage(CommandMessages.VERSION_LANG_FILE, getPlugin().config().get(BaseConfig.LANGUAGE_FILE)));
        buffer.add(getMessager().getMessage(CommandMessages.VERSION_DEBUG_MODE, getPlugin().config().get(BaseConfig.DEBUG_MODE)));

        List<String> versionInfoDump = getPlugin().dumpVersionInfo();
        if (versionInfoDump != null) {
            buffer.addAll(versionInfoDump);
        }

        // log to console
        for (String line : buffer) {
            Logging.info(line);
        }

        final Set<Character> flags = new LinkedHashSet<Character>(context.getFlags());
        if (!flags.isEmpty()) {
            getPlugin().getServerInterface().runTaskAsynchronously(getPlugin(), new Runnable() {
                @Override
                public void run() {
                    for (Character flag : flags) {
                        final String pasteUrl;
                        if (flag.equals('p')) {
                            pasteUrl = postToService(PasteServiceType.PASTIE, true, buffer);
                        } else if (flag.equals('b')) {
                            pasteUrl = postToService(PasteServiceType.PASTEBIN, true, buffer);
                        } else {
                            continue;
                        }
                        getPlugin().getServerInterface().runTask(getPlugin(), new Runnable() {
                            @Override
                            public void run() {
                                getMessager().message(sender, CommandMessages.VERSION_INFO_DUMPED, pasteUrl);
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
    private static String postToService(PasteServiceType type, boolean isPrivate, List<String> pasteData) {
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
            Logging.getLogger().log(Level.WARNING, "Error pasting version information: ", e);
            return "Error posting to service";
        }
    }
}
