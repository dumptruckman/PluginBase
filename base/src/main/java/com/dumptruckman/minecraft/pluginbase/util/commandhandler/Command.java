package com.dumptruckman.minecraft.pluginbase.util.commandhandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Command {
    protected Plugin plugin;

    private int minimumArgLength;
    private int maximumArgLength;

    private String commandName;
    private String commandUsage;

    private List<CommandKey> commandKeys;
    private List<String> examples;

    private Permission permission;
    private List<Permission> auxPerms;

    public Command(Plugin plugin) {
        this.plugin = plugin;
        this.auxPerms = new ArrayList<Permission>();
        this.commandKeys = new ArrayList<CommandKey>();
        this.examples = new ArrayList<String>();
    }

    public List<String> getKeyStrings() {
        List<String> returnList = new ArrayList<String>();
        for (CommandKey ck : this.commandKeys) {
            returnList.add(ck.getKey());
        }
        return returnList;
    }

    public List<CommandKey> getKeys() {
        return this.commandKeys;
    }

    public abstract void runCommand(CommandSender sender, List<String> args);

    public boolean checkArgLength(List<String> args) {
        return (this.minimumArgLength == -1 || this.minimumArgLength <= args.size())
                && (args.size() <= this.maximumArgLength || this.maximumArgLength == -1);
    }

    private String getArgsString(List<String> args) {
        String returnString = "";
        for (String s : args) {
            returnString += s + " ";
        }
        if (returnString.length() > 0) {
            return returnString.substring(0, returnString.length() - 1);
        }
        return "";
    }

    /**
     * This method is provided as a convenience to add additional permissions recursively to all nodes
     *
     * @param otherPerm The Permission to add.
     */
    public void addAdditonalPermission(Permission otherPerm) {
        if (this.plugin.getServer().getPluginManager().getPermission(otherPerm.getName()) == null) {
            this.plugin.getServer().getPluginManager().addPermission(otherPerm);
            this.addToParentPerms(otherPerm.getName());
        }
        this.auxPerms.add(otherPerm);
    }

    public CommandKey getKey(List<String> parsedArgs) {
        // Combines our args to a space separated string
        String argsString = this.getArgsString(parsedArgs);

        for (CommandKey ck : this.commandKeys) {
            String identifier = ck.getKey().toLowerCase();

            // If we match AND we match the number of args.
            if (argsString.matches(identifier + "(\\s+.*|\\s*)")) {
                return ck;
            }
        }
        return null;
    }

    // mutates!
    public List<String> removeKeyArgs(List<String> args, String key) {
        int identifierLength = key.split(" ").length;
        for (int i = 0; i < identifierLength; i++) {
            // Since we're pulling from the front, always remove the first element
            args.remove(0);
        }
        return args;
    }

    public int getNumKeyArgs(String key) {
        int identifierLength = key.split(" ").length;
        return identifierLength;
    }

    public String getPermissionString() {
        return this.permission.getName();
    }

    public Permission getPermission() {
        return this.permission;
    }

    public void setPermission(String p, String desc, PermissionDefault defaultPerm) {
        this.setPermission(new Permission(p, desc, defaultPerm));
    }

    public void setPermission(Permission perm) {
        this.permission = perm;
        try {
            this.plugin.getServer().getPluginManager().addPermission(this.permission);
            this.addToParentPerms(this.permission.getName());
        } catch (IllegalArgumentException e) {
        }
    }

    private void addToParentPerms(String permString) {
        String permStringChopped = permString.replace(".*", "");

        String[] seperated = permStringChopped.split("\\.");
        String parentPermString = getParentPerm(seperated);
        if (parentPermString == null) {
            addToRootPermission("*", permStringChopped);
            addToRootPermission("*.*", permStringChopped);
            return;
        }
        Permission parentPermission = this.plugin.getServer().getPluginManager().getPermission(parentPermString);
        // Creat parent and grandparents
        if (parentPermission == null) {
            parentPermission = new Permission(parentPermString);
            this.plugin.getServer().getPluginManager().addPermission(parentPermission);

            this.addToParentPerms(parentPermString);
        }
        // Create actual perm.
        Permission actualPermission = this.plugin.getServer().getPluginManager().getPermission(permString);
        // Extra check just to make sure the actual one is added
        if (actualPermission == null) {

            actualPermission = new Permission(permString);
            this.plugin.getServer().getPluginManager().addPermission(actualPermission);
        }
        if (!parentPermission.getChildren().containsKey(permString)) {
            parentPermission.getChildren().put(actualPermission.getName(), true);
            this.plugin.getServer().getPluginManager().recalculatePermissionDefaults(parentPermission);
        }
    }

    private void addToRootPermission(String rootPerm, String permStringChopped) {
        Permission rootPermission = this.plugin.getServer().getPluginManager().getPermission(rootPerm);
        if (rootPermission == null) {
            rootPermission = new Permission(rootPerm);
            this.plugin.getServer().getPluginManager().addPermission(rootPermission);
        }
        rootPermission.getChildren().put(permStringChopped + ".*", true);
        this.plugin.getServer().getPluginManager().recalculatePermissionDefaults(rootPermission);
    }

    /**
     * If the given permission was 'multiverse.core.tp.self', this would return 'multiverse.core.tp.*'.
     *
     * @param seperated
     *
     * @return
     */
    private String getParentPerm(String[] seperated) {
        if (seperated.length == 1) {
            return null;
        }
        String returnString = "";
        for (int i = 0; i < seperated.length - 1; i++) {
            returnString += seperated[i] + ".";
        }
        return returnString + "*";
    }

    public boolean isOpRequired() {
        return this.permission.getDefault() == PermissionDefault.OP;
    }

    public String getCommandName() {
        return this.commandName;
    }

    public String getCommandDesc() {
        return this.permission.getDescription();
    }

    public List<String> getCommandExamples() {
        return this.examples;
    }

    public String getCommandUsage() {
        return this.commandUsage;
    }

    public void addCommandExample(String example) {
        this.examples.add(example);
    }

    public void setCommandUsage(String usage) {
        this.commandUsage = usage;
    }

    public void setArgRange(int min, int max) {
        this.minimumArgLength = min;
        this.maximumArgLength = max;
    }

    public void setName(String name) {
        this.commandName = name;
    }

    public void addKey(String key) {
        this.commandKeys.add(new CommandKey(key, this));
        Collections.sort(this.commandKeys, new ReverseLengthSorter());
    }

    public void addKey(String key, int minArgs, int maxArgs) {
        this.commandKeys.add(new CommandKey(key, this, minArgs, maxArgs));
        Collections.sort(this.commandKeys, new ReverseLengthSorter());
    }

    /** @return the plugin */
    protected Plugin getPlugin() {
        return this.plugin;
    }

    public Integer getMaxArgs() {
        return this.maximumArgLength;
    }

    public Integer getMinArgs() {
        return this.minimumArgLength;
    }

    public List<String> getAllPermissionStrings() {
        List<String> permStrings = new ArrayList<String>();
        permStrings.add(this.permission.getName());
        for (Permission p : this.auxPerms) {
            permStrings.add(p.getName());
        }
        return permStrings;
    }
    public void showHelp(CommandSender sender) {
            sender.sendMessage(ChatColor.AQUA + "--- " + this.getCommandName() + " ---");
            sender.sendMessage(ChatColor.YELLOW + this.getCommandDesc());
            sender.sendMessage(ChatColor.DARK_AQUA + this.getCommandUsage());
            sender.sendMessage("Permission: " + ChatColor.GREEN + this.getPermissionString());
            String keys = "";
            for (String key : this.getKeyStrings()) {
                keys += key + ", ";
            }
            keys = keys.substring(0, keys.length() - 2);
            sender.sendMessage(ChatColor.BLUE + "Aliases: " + ChatColor.RED + keys);
            if (this.getCommandExamples().size() > 0) {
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "Examples:");
                if (sender instanceof Player) {
                    for (int i = 0; i < 4 && i < this.getCommandExamples().size(); i++) {
                        sender.sendMessage(this.getCommandExamples().get(i));
                    }
                } else {
                    for (String c : this.getCommandExamples()) {
                        sender.sendMessage(c);
                    }
                }
            }
        }
}
