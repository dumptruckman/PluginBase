package pluginbase.properties;

import org.jetbrains.annotations.NotNull;

/**
 * A property observer.
 * <p/>
 * Any class wishing to be notified when a {@link ValueProperty} has changes in a {@link Properties}
 * object should implement this class and add it as an observer with {@link Properties#addObserver(Observer)}.
 */
public interface Observer {

    /**
     * This method is called by any properties object this observer is added to after a change to a
     * ValueProperty occurs.
     *
     * @param properties The properties object where the change occurs.
     * @param property The property that has changed.
     */
    void update(@NotNull final Properties properties, @NotNull final ValueProperty property);
}
