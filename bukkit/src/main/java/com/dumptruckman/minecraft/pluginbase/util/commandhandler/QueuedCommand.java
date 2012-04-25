package com.dumptruckman.minecraft.pluginbase.util.commandhandler;

import com.dumptruckman.minecraft.pluginbase.plugin.BukkitPlugin;
import com.dumptruckman.minecraft.pluginbase.plugin.PluginBase;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.List;

public class QueuedCommand {
    private String name;
    private List<? extends Object> args;
    private Class<?> paramTypes[];
    private CommandSender sender;
    private BukkitPlugin plugin;
    private Calendar timeRequested;
    private String success;
    private String fail;
    private int expiration;
    private boolean alreadyRun;

    public QueuedCommand(String commandName, List<? extends Object> args, Class<?> partypes[], CommandSender sender, Calendar instance, BukkitPlugin plugin, String success, String fail, int expiration) {
        this.plugin = plugin;
        this.name = commandName;
        this.args = args;
        this.sender = sender;
        this.timeRequested = instance;
        this.paramTypes = partypes;
        this.setSuccess(success);
        this.setFail(fail);
        this.expiration = expiration;
    }

    public CommandSender getSender() {
        return this.sender;
    }

    public boolean execute() {
        this.timeRequested.add(Calendar.SECOND, this.expiration);
        if (this.timeRequested.after(Calendar.getInstance())) {
            if(alreadyRun) {
                this.sender.sendMessage("This command has already been run! Please type the original command again if you want to rerun it.");
                return false;
            }
            try {
                this.alreadyRun = true;
                Method method = this.plugin.getClass().getMethod(this.name, this.paramTypes);
                Object[] listAsArray = this.args.toArray(new Object[this.args.size()]);
                Object returnVal = method.invoke(this.plugin, listAsArray);
                if (returnVal instanceof Boolean) {
                    return (Boolean) returnVal;
                } else {
                    return true;
                }
            } catch (Exception e) {
                System.out.print(e.getMessage());
            }
        } else {
            this.sender.sendMessage("This command has expired. Please type the original command again.");
        }
        return false;
    }

    private void setSuccess(String success) {
        this.success = success;
    }

    public String getSuccess() {
        return this.success;
    }

    private void setFail(String fail) {
        this.fail = fail;
    }

    public String getFail() {
        return this.fail;
    }

}
