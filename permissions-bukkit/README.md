Permissions-Bukkit allows a Bukkit developer to easily manage all of their plugin's permissions.

It is highly recommended that you use the maven-shade-plugin to relocate this code if you intend to use it.  It will NOT play well with a copy employed by someone elses plugin.  Maven details will be at the bottom.

This module is built on top of this non-bukkit dependent module: https://github.com/dumptruckman/PluginBase/tree/master/permissions

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
            .parent("noob", false) // Players with 'noob' permission will not have access to this permission by default
            .child("some.child.perm") // Works similarly to parent but affects the default of the child
            .build(); // Finalizes the permission.
    // Note, those were all of the options except for parent and child methods with different signatures.
}
```

You can add this as a maven dependency with:
``` xml
<dependency>
    <groupId>com.dumptruckman.minecraft</groupId>
    <artifactId>Permissions-Bukkit</artifactId>
    <version>1.5-SNAPSHOT</version>
    <type>jar</type>
    <scope>compile</scope>
</dependency>
```
And then shade/relocate it into your plugin with:
``` xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-shade-plugin</artifactId>
    <version>1.5</version>
    <executions>
        <execution>
            <id>permissions</id>
            <phase>package</phase>
            <goals>
                <goal>shade</goal>
            </goals>
            <configuration>
                <artifactSet>
                    <includes>
                        <include>com.dumptruckman.minecraft:Permissions-Bukkit</include>
                    </includes>
                </artifactSet>
                <relocations>
                    <relocation>
                        <pattern>com.dumptruckman.minecraft.pluginbase</pattern>
                        <shadedPattern>your.namespace.here.pluginbase</shadedPattern>
                    </relocation>
                </relocations>
            </configuration>
        </execution>
    </executions>
</plugin>
```

Copyright (C) dumptruckman 2012

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at http://mozilla.org/MPL/2.0/.
