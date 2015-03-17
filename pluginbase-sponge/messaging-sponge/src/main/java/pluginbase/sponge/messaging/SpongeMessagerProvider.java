package pluginbase.sponge.messaging;

import pluginbase.messages.MessageProvider;
import pluginbase.messages.messaging.Messager;
import pluginbase.messages.messaging.MessagerProvider;

public class SpongeMessagerProvider implements MessagerProvider {

    @Override
    public Messager createMessager(MessageProvider messageProvider) {
        return new SpongeMessager(messageProvider);
    }
}
