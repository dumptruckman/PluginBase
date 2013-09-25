package com.dumptruckman.minecraft.pluginbase.config.field;

import com.dumptruckman.minecraft.pluginbase.config.SerializationRegistrar;
import com.dumptruckman.minecraft.pluginbase.config.examples.Child;
import com.dumptruckman.minecraft.pluginbase.config.examples.Comprehensive;
import com.dumptruckman.minecraft.pluginbase.config.examples.Custom;
import com.dumptruckman.minecraft.pluginbase.config.examples.Parent;
import com.dumptruckman.minecraft.pluginbase.config.examples.Recursive;
import com.dumptruckman.minecraft.pluginbase.config.serializers.CustomSerializer;
import com.dumptruckman.minecraft.pluginbase.config.serializers.CustomSerializer2;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FieldMapperTest {

    @Before
    public void setUp() throws Exception {
        SerializationRegistrar.registerClass(Recursive.class);
        SerializationRegistrar.registerClass(Parent.class);
        SerializationRegistrar.registerClass(Child.class);
        SerializationRegistrar.registerClass(Comprehensive.class);
        SerializationRegistrar.registerClass(Custom.class);
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

    @Test
    public void testFieldGetSerializer() {
        FieldMap fieldMap = FieldMapper.getFieldMap(Comprehensive.class);

        Field field = fieldMap.getField("custom");
        assertNotNull(field);
        assertEquals(CustomSerializer.class, field.getSerializer().getClass());

        field = fieldMap.getField("custom2");
        assertNotNull(field);
        assertEquals(CustomSerializer2.class, field.getSerializer().getClass());
    }

    @Test
    public void testFieldGetDescription() {
        FieldMap fieldMap = FieldMapper.getFieldMap(Comprehensive.class);

        Field field = fieldMap.getField("aInt");
        assertNotNull(field);
        assertEquals(Comprehensive.A_INT_DESCRIPTION, field.getDescription());
    }

    @Test
    public void testFieldGetComments() {
        FieldMap fieldMap = FieldMapper.getFieldMap(Comprehensive.class);

        Field field = fieldMap.getField("aInt");
        assertNotNull(field);
        assertArrayEquals(Comprehensive.A_INT_COMMENTS, field.getComments());
    }
}
