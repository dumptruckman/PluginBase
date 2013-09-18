package com.dumptruckman.minecraft.pluginbase.config;

import com.dumptruckman.minecraft.pluginbase.config.examples.Child;
import com.dumptruckman.minecraft.pluginbase.config.examples.Comprehensive;
import com.dumptruckman.minecraft.pluginbase.config.examples.Custom;
import com.dumptruckman.minecraft.pluginbase.config.examples.Parent;
import com.dumptruckman.minecraft.pluginbase.config.examples.Recursive;
import org.junit.Before;

public class TestBase {

    @Before
    public void registerClasses() {
        SerializationRegistrar.registerClass(Parent.class);
        SerializationRegistrar.registerClass(Child.class);
        SerializationRegistrar.registerClass(Comprehensive.class);
        SerializationRegistrar.registerClass(Custom.class);
        SerializationRegistrar.registerClass(Recursive.class);
    }
}
