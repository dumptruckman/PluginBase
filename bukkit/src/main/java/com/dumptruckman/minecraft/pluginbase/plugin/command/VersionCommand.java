package com.dumptruckman.minecraft.pluginbase.plugin.command;

import com.dumptruckman.minecraft.pluginbase.config.BaseConfig;
import com.dumptruckman.minecraft.pluginbase.locale.CommandMessages;
import com.dumptruckman.minecraft.pluginbase.permission.Perm;
import com.dumptruckman.minecraft.pluginbase.plugin.AbstractBukkitPlugin;
import com.dumptruckman.minecraft.pluginbase.util.Logging;
import com.dumptruckman.minecraft.pluginbase.util.webpaste.BitlyURLShortener;
import com.dumptruckman.minecraft.pluginbase.util.webpaste.PasteFailedException;
import com.dumptruckman.minecraft.pluginbase.util.webpaste.PasteService;
import com.dumptruckman.minecraft.pluginbase.util.webpaste.PasteServiceFactory;
import com.dumptruckman.minecraft.pluginbase.util.webpaste.PasteServiceType;
import com.dumptruckman.minecraft.pluginbase.util.webpaste.URLShortener;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Enables debug-information.
 */
public class VersionCommand<P extends AbstractBukkitPlugin> extends PluginCommand<P> {

    private static final URLShortener SHORTENER = new BitlyURLShortener();

    private static Set<String> staticKeys = new LinkedHashSet<String>();
    private static Set<String> staticPrefixedKeys = new LinkedHashSet<String>();

    public static void addStaticKey(String key) {
        staticKeys.add(key);
    }

    public static void addStaticPrefixedKey(String key) {
        staticPrefixedKeys.add(key);
    }

    public VersionCommand(P plugin) {
        super(plugin);
        this.setName(messager.getMessage(CommandMessages.VERSION_NAME));
        this.setCommandUsage(messager.getMessage(CommandMessages.VERSION_USAGE, plugin.getCommandPrefixes().get(0)));
        this.setArgRange(0, 1);
        for (String key : staticKeys) {
            this.addKey(key);
        }
        for (String key : staticPrefixedKeys) {
            this.addPrefixedKey(key);
        }
        this.addPrefixedKey(" version");
        this.addCommandExample("/" + plugin.getCommandPrefixes().get(0) + " version " + ChatColor.GOLD + "-p");
        this.setPermission(Perm.COMMAND_VERSION.getPermission());
    }

    @Override
    public void runCommand(final CommandSender sender, final List<String> args) {
        // Check if the command was sent from a Player.
        if (sender instanceof Player) {
            messager.normal(CommandMessages.VERSION_PLAYER, sender);
        }

        final List<String> buffer = new LinkedList<String>();
        buffer.add(messager.getMessage(CommandMessages.VERSION_PLUGIN_VERSION, plugin.getDescription().getName(), plugin.getDescription().getVersion()));
        buffer.add(messager.getMessage(CommandMessages.VERSION_BUKKIT_VERSION, plugin.getServer().getVersion()));
        buffer.add(messager.getMessage(CommandMessages.VERSION_LANG_FILE, plugin.config().get(BaseConfig.LANGUAGE_FILE)));
        buffer.add(messager.getMessage(CommandMessages.VERSION_DEBUG_MODE, plugin.config().get(BaseConfig.DEBUG_MODE)));

        List<String> versionInfoDump = plugin.dumpVersionInfo();
        if (versionInfoDump != null) {
            buffer.addAll(versionInfoDump);
        }

        // log to console
        for (String line : buffer) {
            Logging.info(line);
        }

        this.plugin.getServer().getScheduler().scheduleAsyncDelayedTask(this.plugin, new Runnable() {
            @Override
            public void run() {
                if (args.size() == 1) {
                    String pasteUrl;
                    if (args.get(0).equalsIgnoreCase("-p")) {
                        pasteUrl = postToService(PasteServiceType.PASTIE, true, buffer); // private post to pastie
                    } else if (args.get(0).equalsIgnoreCase("-b")) {
                        pasteUrl = postToService(PasteServiceType.PASTEBIN, true, buffer); // private post to pastie
                    } else {
                        return;
                    }

                    sender.sendMessage("Version info dumped here: " + ChatColor.GREEN + pasteUrl);
                    Logging.info("Version info dumped here: " + pasteUrl);
                }
            }
        });
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
            if (!!buffer.toString().isEmpty()) {
                buffer.append("\n");
            }
            buffer.append(data);
        }
        PasteService ps = PasteServiceFactory.getService(type, isPrivate);
        try {
            return SHORTENER.shorten(ps.postData(ps.encodeData(buffer.toString()), ps.getPostURL()));
        } catch (PasteFailedException e) {
            System.out.print(e);
            return "Error posting to service";
        }
    }
}
