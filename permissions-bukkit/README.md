Permissions-Bukkit allows a Bukkit developer to easily manage all of their plugin's permissions.

Features
--------
* Easy and safe to use statically.
* One-location setup for each permission. (No need to register in plugin.yml)
* Easy to use dynamic permissions. (someperm.[anything])
* Automatically registers permissions with Bukkit. (Including dynamic permissions)
* Automatically creates a `namespace.*` global permission.
* Automatically creates a `namespace.cmd.*` global command permission.

Example Usage
-------------
``` java
public class MyPlugin implements Listener {
    public void onLoad() {
        BukkitPermFactory.setPluginName("MyPlugin");
    }
    public void onEnable() {
        Bukkit.getPluginManager().registerListeners(this, this);
    }
    @EventHandler
    public void playerInteract(final PlayerInteractEvent event) {
        if (MyPerms.SAMPLE_PERM.hasPermission(event.getPlayer()) {
            // do special stuff
        }
    }
}

public class MyPerms {
    public static final BukkitPerm SAMPLE_PERM = BukkitPermFactory.newBukkitPerm("sample") // The permission name is passed in here
            .usePluginName() // Tells it to be created using the 'myplugin.' as the prefix (lowercased from above name automatically)
            .addToAll() // Adds the permission to 'myplugin.*'
            .def(PermDefault.FALSE) // Sets the default permission access
            .description("This is a sample permission.") // Adds a permission description
            .parent("mp.*") // This assumes tha parent is created eslewhere.  You can alternately pass in a BukkitPerm object.
            .parent("noob", false) // Players will 'noob' permission will not have access to this permission by default
            .child("some.child.perm") // Works similarly to parent but affects the default of the child
            .build(); // Finalizes the permission.
    // Note, those were all of the options except for parent and child methods with different signatures.
}
```


Copyright (C) dumptruckman 2012

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at http://mozilla.org/MPL/2.0/.
