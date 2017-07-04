package pluginbase.command;

import org.jetbrains.annotations.NotNull;
import pluginbase.messages.messaging.Messaging;

import java.lang.reflect.Constructor;

enum CommandLoader {
    ;

    /**
     * Constructs a command object from the given Command class.
     * <br>
     * The command class must accept a single parameter which is an object extending both
     * {@link Messaging} and {@link CommandProvider}.
     *
     * @param clazz the command class to instantiate.
     * @return a new instance of the command.
     */
    @NotNull
    public static Command loadCommand(@NotNull CommandProvider commandProvider, @NotNull final Class<? extends Command> clazz) {
        if (clazz.equals(DirectoryCommand.class)) {
            return new DirectoryCommand(commandProvider);
        }
        try {
            for (final Constructor constructor : clazz.getDeclaredConstructors()) {
                if (constructor.getParameterTypes().length == 1
                        && CommandProvider.class.isAssignableFrom(constructor.getParameterTypes()[0])) {
                    constructor.setAccessible(true);
                    try {
                        return (Command) constructor.newInstance(commandProvider);
                    } finally {
                        constructor.setAccessible(false);
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("A command class must have a constructor that takes a single CommandProvider instance only.", e);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        throw new IllegalArgumentException("Class " + clazz + " is missing constructor that takes sole argument which extends Messaging and CommandProvider.");
    }
}
