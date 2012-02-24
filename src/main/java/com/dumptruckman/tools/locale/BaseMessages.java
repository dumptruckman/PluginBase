package com.dumptruckman.tools.locale;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

/**
 * An enum containing all messages/strings used by PluginBase.
 */
public class BaseMessages {
    // BEGIN CHECKSTYLE-SUPPRESSION: Javadoc
    
    // Generic Messages
    public final static Message GENERIC_ERROR = new Message("[Error]");
    public final static Message GENERIC_SUCCESS = new Message("[Success]");
    public final static Message GENERIC_INFO = new Message("[Info]");
    public final static Message GENERIC_HELP = new Message("[Help]");
    public final static Message GENERIC_OFF = new Message("OFF");

    // Reload Command
    public final static Message RELOAD_COMPLETE = new Message("&b===[ Reload Complete! ]===");

    // DebugCommand
    public final static Message INVALID_DEBUG =
            new Message("&fInvalid debug level.  Please use number 0-3.  &b(3 being many many messages!)");
    public final static Message DEBUG_SET = new Message("Debug mode is %1");

    // BEGIN CHECKSTYLE-SUPPRESSION: Javadoc

    static {
        registerMessages(BaseMessages.class);
    }

    static final Set<Message> messages = new HashSet<Message>();

    public static void registerMessages(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            field.setAccessible(true);
            try {
                if (Message.class.isInstance(field.get(null))) {
                    try {
                        messages.add((Message) field.get(null));
                    } catch(IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IllegalArgumentException ignore) {
            } catch (IllegalAccessException ignore) {
            } catch (NullPointerException ignore) { }
        }
    }
}

