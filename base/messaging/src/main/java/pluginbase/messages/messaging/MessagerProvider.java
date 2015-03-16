package pluginbase.messages.messaging;

import pluginbase.messages.MessageProvider;

/**
 * Implement and register with {@link pluginbase.messages.messaging.MessagerFactory#registerMessagerProvider(pluginbase.messages.messaging.MessagerProvider)}
 * to provide an implementation specific Messager.
 */
public interface MessagerProvider {

    /**
     * Tells the MessagerFactory how to create a new Messager for your specific implementation.
     *
     * @param messageProvider The MessageProvider which is responsible for providing localized messages to your Messager.
     * @return an implementation specific Messager.
     */
    Messager createMessager(MessageProvider messageProvider);
}
