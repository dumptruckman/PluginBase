package com.dumptruckman.minecraft.pluginbase.config.field;

import com.dumptruckman.minecraft.pluginbase.config.SerializationRegistrar;
import com.dumptruckman.minecraft.pluginbase.config.examples.Child;
import com.dumptruckman.minecraft.pluginbase.config.examples.Parent;
import com.dumptruckman.minecraft.pluginbase.config.examples.Recursive;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class FieldInstanceTest {

    @Before
    public void setUp() throws Exception {
        SerializationRegistrar.registerClass(Recursive.class);
        SerializationRegistrar.registerClass(Parent.class);
        SerializationRegistrar.registerClass(Child.class);
    }

    @Test
    public void testLocateField() {
        Child child = new Child(true);
        Parent parent = new Parent(child);
        FieldInstance fieldInstance = Field.locateField(parent, "achild", "aboolean");

        assertNotNull(fieldInstance);
        assertTrue((Boolean) fieldInstance.getFieldValue());
        fieldInstance.setFieldValue(false);
        assertFalse((Boolean) fieldInstance.getFieldValue());
    }

    @Test
    public void testLocateFieldWithAlias() {
        Child child = new Child(true);
        Parent parent = new Parent(child);
        FieldInstance fieldInstance = Field.locateField(parent, "cbool");

        assertNotNull(fieldInstance);
        assertTrue((Boolean) fieldInstance.getFieldValue());
        fieldInstance.setFieldValue(false);
        assertFalse((Boolean) fieldInstance.getFieldValue());
    }
}
