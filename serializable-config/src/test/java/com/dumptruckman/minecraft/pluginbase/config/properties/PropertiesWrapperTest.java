package com.dumptruckman.minecraft.pluginbase.config.properties;

import com.dumptruckman.minecraft.pluginbase.config.SerializationRegistrar;
import com.dumptruckman.minecraft.pluginbase.config.examples.Child;
import com.dumptruckman.minecraft.pluginbase.config.examples.Comprehensive;
import com.dumptruckman.minecraft.pluginbase.config.examples.Custom;
import com.dumptruckman.minecraft.pluginbase.config.examples.Parent;
import com.dumptruckman.minecraft.pluginbase.config.examples.Recursive;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PropertiesWrapperTest {

    Comprehensive comprehensive;

    @Before
    public void setUp() throws Exception {
        SerializationRegistrar.registerClass(Recursive.class);
        SerializationRegistrar.registerClass(Parent.class);
        SerializationRegistrar.registerClass(Child.class);
        SerializationRegistrar.registerClass(Comprehensive.class);
        SerializationRegistrar.registerClass(Custom.class);
        comprehensive = new Comprehensive();
    }

    @Test
    public void testGetProperty() throws Exception {
        assertEquals(Comprehensive.A_INT, comprehensive.getProperty("aInt"));
        assertEquals(Comprehensive.STRING_OBJECT_MAP, comprehensive.getProperty("stringObjectMap"));
    }

    @Test
    public void testGetPropertyAnyCase() throws Exception {
        assertEquals(Comprehensive.A_INT, comprehensive.getProperty("aint"));
        assertEquals(Comprehensive.STRING_OBJECT_MAP, comprehensive.getProperty("STRINGOBJECTMAP"));
    }

    @Test
    public void testGetPropertyNested() throws Exception {
        assertEquals(Comprehensive.CUSTOM.name, comprehensive.getProperty("custom", "name"));
    }

    @Test(expected = NoSuchFieldException.class)
    public void testGetPropertyNoSuchField() throws Exception {
        comprehensive.getProperty("booboooboo");
    }

    @Test(expected = NoSuchFieldException.class)
    public void testGetPropertyNestedNoSuchField() throws Exception {
        comprehensive.getProperty("custom", "fake");
    }

    @Test
    public void testGetPropertyUnchecked() throws Exception {
        assertEquals(Comprehensive.A_INT, comprehensive.getPropertyUnchecked("aInt"));
    }

    @Test
    public void testGetPropertyUncheckedNested() throws Exception {
        assertEquals(Comprehensive.CUSTOM.name, comprehensive.getPropertyUnchecked("custom", "name"));
    }

    @Test
    public void testGetPropertyUncheckedNoSuchField() throws Exception {
        assertNull(comprehensive.getPropertyUnchecked("booboooboo"));
    }

    @Test
    public void testGetPropertyUncheckedNestedNoSuchField() throws Exception {
        assertNull(comprehensive.getPropertyUnchecked("custom", "fake"));
    }

    @Test
    public void testSetProperty() throws Exception {
        assertFalse(comprehensive.aInt == 500);
        comprehensive.setProperty(500, "aint");
        assertEquals(500, comprehensive.aInt);
    }

    @Test
    public void testSetPropertyNested() throws Exception {
        assertFalse(comprehensive.custom.name.equals("aname"));
        comprehensive.setProperty("aname", "custom", "name");
        assertEquals("aname", comprehensive.custom.name);
    }

    @Test(expected = NoSuchFieldException.class)
    public void testSetPropertyNoSuchField() throws Exception {
        comprehensive.setProperty("value", "booboooboo");
    }

    @Test(expected = NoSuchFieldException.class)
    public void testSetPropertyNestedNoSuchField() throws Exception {
        comprehensive.setProperty("value", "custom", "fake");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPropertyIllegalValue() throws Exception {
        comprehensive.setProperty("value", "aint");
    }

    @Test
    public void testSetPropertyUnchecked() throws Exception {
        assertFalse(comprehensive.aInt == 500);
        comprehensive.setPropertyUnchecked(500, "aint");
        assertEquals(500, comprehensive.aInt);
    }

    @Test
    public void testSetPropertyUncheckedNested() throws Exception {
        assertFalse(comprehensive.custom.name.equals("aname"));
        comprehensive.setPropertyUnchecked("aname", "custom", "name");
        assertEquals("aname", comprehensive.custom.name);
    }

    @Test
    public void testSetPropertyUncheckedNoSuchField() throws Exception {
        comprehensive.setPropertyUnchecked("value", "booboooboo");
    }

    @Test
    public void testSetPropertyUncheckedNestedNoSuchField() throws Exception {
        comprehensive.setPropertyUnchecked("value", "custom", "fake");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPropertyUncheckedIllegalValue() throws Exception {
        comprehensive.setPropertyUnchecked("value", "aint");
    }

    @Test
    public void testPropertiesWrapper() throws Exception {
        Custom custom = new Custom("custom");
        Properties properties = PropertiesWrapper.wrapObject(custom);
        assertEquals(custom.name, properties.getProperty("name"));
    }
}
