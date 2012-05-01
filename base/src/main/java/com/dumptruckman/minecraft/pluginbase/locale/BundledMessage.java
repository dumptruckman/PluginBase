package com.dumptruckman.minecraft.pluginbase.locale;

public class BundledMessage {

    private Message message;
    private Object[] args;

    public BundledMessage(Message message, Object...args) {
        this.message = message;
        this.args = args;
    }

    public Message getMessage() {
        return message;
    }

    public Object[] getArgs() {
        return args;
    }
}
