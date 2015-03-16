package pluginbase.bukkit.messaging;

import pluginbase.messages.MessageProvider;
import pluginbase.messages.messaging.Messager;
import pluginbase.messages.messaging.MessagerProvider;

public class BukkitMessagerProvider implements MessagerProvider {

    @Override
    public Messager createMessager(MessageProvider messageProvider) {
        return new BukkitMessager(messageProvider);
    }
}
