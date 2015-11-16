package pluginbase.plugin.command.builtin;

import org.jetbrains.annotations.NotNull;
import pluginbase.logging.PluginLogger;
import pluginbase.messages.Message;
import pluginbase.messages.messaging.Messager;
import pluginbase.minecraft.BasePlayer;
import pluginbase.plugin.PluginBase;
import pluginbase.util.webpaste.BitlyURLShortener;
import pluginbase.util.webpaste.PasteFailedException;
import pluginbase.util.webpaste.PasteService;
import pluginbase.util.webpaste.PasteServiceFactory;
import pluginbase.util.webpaste.PasteServiceType;
import pluginbase.util.webpaste.URLShortener;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;

class PasteServiceTask implements Runnable {

    private static final URLShortener SHORTENER = new BitlyURLShortener();

    private final PluginBase commandProvider;
    private final Set<Character> flags;
    private final List<String> versionInfoDump;
    private final BasePlayer sender;
    private final Message afterPasteMessage;

    PasteServiceTask(PluginBase commandProvider, Set<Character> flags, List<String> versionInfoDump, BasePlayer sender, Message afterPasteMessage) {
        this.commandProvider = commandProvider;
        this.flags = flags;
        this.versionInfoDump = versionInfoDump;
        this.sender = sender;
        this.afterPasteMessage = afterPasteMessage;
    }

    private PluginBase getCommandProvider() {
        return commandProvider;
    }

    private Messager getMessager() {
        return getCommandProvider().getMessager();
    }

    @Override
    public void run() {
        for (Character flag : flags) {
            final String pasteUrl;
            if (flag.equals('p')) {
                pasteUrl = postToService(PasteServiceType.PASTIE, true, versionInfoDump, getCommandProvider().getLog());
            } else if (flag.equals('b')) {
                pasteUrl = postToService(PasteServiceType.PASTEBIN, true, versionInfoDump, getCommandProvider().getLog());
            } else if (flag.equals('h')) {
                pasteUrl = postToService(PasteServiceType.HASTEBIN, true, versionInfoDump, getCommandProvider().getLog());
            } else {
                continue;
            }
            getCommandProvider().getServerInterface().runTask(new Runnable() {
                @Override
                public void run() {
                    getMessager().message(sender, afterPasteMessage, pasteUrl);
                }
            });
        }
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
