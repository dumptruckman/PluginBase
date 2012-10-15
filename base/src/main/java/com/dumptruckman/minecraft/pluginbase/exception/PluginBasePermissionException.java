package com.dumptruckman.minecraft.pluginbase.exception;

import com.dumptruckman.minecraft.pluginbase.permission.Perm;

public class PluginBasePermissionException extends PluginBaseException {

    public PluginBasePermissionException(Perm perm) {
        super("You do not have permission to do that.");
    }
}
