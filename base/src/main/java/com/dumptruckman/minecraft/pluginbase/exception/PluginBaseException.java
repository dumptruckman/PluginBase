package com.dumptruckman.minecraft.pluginbase.exception;

public abstract class PluginBaseException extends Exception {

    protected PluginBaseException() { }

    protected PluginBaseException(String msg) {
        super(msg);
    }
}
