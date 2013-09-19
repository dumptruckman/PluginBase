package com.dumptruckman.minecraft.pluginbase.config;

import com.dumptruckman.minecraft.pluginbase.config.examples.Child;
import com.dumptruckman.minecraft.pluginbase.config.examples.Parent;
import com.dumptruckman.minecraft.pluginbase.config.examples.Recursive;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class FieldMapperTest {

    @Before
    public void setUp() throws Exception {
        SerializationRegistrar.registerClass(Recursive.class);
        SerializationRegistrar.registerClass(Parent.class);
        SerializationRegistrar.registerClass(Child.class);
    }

    @Test
    public void testRecursiveProtection() {
        boolean thrown = false;
        try {
            FieldMapper.getFieldMap(Recursive.class);
        } catch (IllegalStateException e) {
            thrown = e.getMessage().equals("Mapping fields for " + Recursive.class + " would result in infinite recursion due self containment.");
        }
        assertTrue(thrown);
    }

    @Test
    public void testBasic() {
        assertTrue(FieldMapper.getFieldMap(Child.class).hasField("aBoolean"));
    }

    @Test
    public void testParentChild() {
        FieldMap fieldMap = FieldMapper.getFieldMap(Parent.class);
        assertTrue(fieldMap.hasField("aChild"));
        Field childField = fieldMap.getField("aChild");
        assertTrue(childField.hasField("aBoolean"));
    }

    @Test
    public void testValueGet() {
        Child child = new Child(true);
        Parent parent = new Parent(child);
        FieldMap fieldMap = FieldMapper.getFieldMap(Parent.class);
        Field childField = fieldMap.getField("aChild");
        Field booleanField = childField.getField("aBoolean");
        assertTrue((Boolean) booleanField.getValue(child));
    }
}
