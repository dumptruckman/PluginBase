package pluginbase.messages.messaging;

import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.jetbrains.annotations.NotNull;
import pluginbase.messages.LocalizablePlugin;
import pluginbase.messages.Messages;

import java.io.File;
import java.util.Locale;

/**
 * A utility class for registering and creating Messager objects.
 */
public class MessagerFactory {

    private MessagerFactory() {
        throw new AssertionError("MessagerFactory cannot be instantiated.");
    }

    private static MessagerProvider messagerProvider = null;

    /**
     * Loads the messages from the given language file into a new Messager.
     *
     * @param localizablePlugin the plugin the messages belong to.
     * @param loader the configuration loader used to load localized messages.
     * @param locale the locale of the localized messages.
     * @return a new Messager loaded with the given messages.
     */
    public static Messager createMessager(@NotNull final LocalizablePlugin localizablePlugin,
                                          @NotNull final ConfigurationLoader loader,
                                          @NotNull final Locale locale) {
        if (messagerProvider == null) {
            throw new IllegalStateException("MessagerProvider must be registered before a Messager can be created.");
        }
        return messagerProvider.createMessager(Messages.loadMessages(localizablePlugin, loader, locale));
    }

    /**
     * Registers a MessagerProvider which is responsible for creating a Messager.
     *
     * @param messagerProvider a MessagerProvider which is responsible for creating a Messager.
     */
    public static void registerMessagerProvider(@NotNull MessagerProvider messagerProvider) {
        MessagerFactory.messagerProvider = messagerProvider;
    }
}
