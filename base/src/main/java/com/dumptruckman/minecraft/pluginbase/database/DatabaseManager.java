package com.dumptruckman.minecraft.pluginbase.database;

public interface DatabaseManager<D extends SQLDatabase> {

    boolean load();

    void unload();

    D getDatabase();
}
