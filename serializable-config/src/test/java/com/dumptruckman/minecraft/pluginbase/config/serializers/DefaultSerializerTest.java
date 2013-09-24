package com.dumptruckman.minecraft.pluginbase.config.serializers;

import com.dumptruckman.minecraft.pluginbase.config.SerializationRegistrar;
import com.dumptruckman.minecraft.pluginbase.config.examples.Child;
import com.dumptruckman.minecraft.pluginbase.config.examples.Comprehensive;
import com.dumptruckman.minecraft.pluginbase.config.examples.Parent;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DefaultSerializerTest {

    Serializer<Object> serializer;

    @Before
    public void setUp() throws Exception {
        SerializationRegistrar.registerClass(Parent.class);
        SerializationRegistrar.registerClass(Child.class);
        SerializationRegistrar.registerClass(Comprehensive.class);
        serializer = new DefaultSerializer();
    }

    @Test
    public void testSerializeDeserializeComprehensive() throws Exception {
        Comprehensive comprehensive = new Comprehensive();
        Object serializedForm = serializer.serialize(comprehensive);
        Comprehensive deserializedForm = (Comprehensive) serializer.deserialize(serializedForm, Comprehensive.class);
        assertEquals(comprehensive, deserializedForm);
    }
}
