package com.dumptruckman.minecraft.pluginbase.util.commandhandler;

public class CommandKey {
    private Integer minArgs = null;
    private Integer maxArgs = null;
    private String key = "";
    private Command cmd;

    public CommandKey(String key, Command cmd) {
        this.key = key;
        this.cmd = cmd;
    }

    public CommandKey(String key, Command cmd, int minArgs, int maxArgs) {
        this(key, cmd);
        this.minArgs = minArgs;
        this.maxArgs = maxArgs;
    }

    public String getKey() {
        return this.key;
    }

    public boolean hasValidNumberOfArgs(int args) {
        if (minArgs == null) {
            minArgs = this.cmd.getMinArgs();
        }
        if (maxArgs == null) {
            maxArgs = this.cmd.getMaxArgs();
        }
        // Min args == -1 case covered here.
        if (minArgs <= args && maxArgs >= args) {
            return true;
        }
        if (minArgs <= args && maxArgs == -1) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "[" + this.key + "(" + this.minArgs + ", " + this.maxArgs + ")" + "]";
    }
}