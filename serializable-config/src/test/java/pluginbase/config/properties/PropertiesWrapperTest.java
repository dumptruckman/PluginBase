package pluginbase.config.properties;

import pluginbase.config.TestBase;
import pluginbase.config.examples.Comprehensive;
import pluginbase.config.examples.Custom;
import org.junit.Before;
import org.junit.Test;
import pluginbase.config.examples.Simple;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PropertiesWrapperTest extends TestBase {

    Comprehensive comprehensive;

    @Before
    public void setUp() throws Exception {
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
        assertEquals(Comprehensive.CUSTOM.name, comprehensive.getProperty("custom.name"));
    }

    @Test(expected = NoSuchFieldException.class)
    public void testGetPropertyNoSuchField() throws Exception {
        comprehensive.getProperty("booboooboo");
    }

    @Test(expected = NoSuchFieldException.class)
    public void testGetPropertyNestedNoSuchField() throws Exception {
        comprehensive.getProperty("custom.fake");
    }

    @Test
    public void testGetPropertyUnchecked() throws Exception {
        assertEquals(Comprehensive.A_INT, comprehensive.getPropertyUnchecked("aInt"));
    }

    @Test
    public void testGetPropertyUncheckedNested() throws Exception {
        assertEquals(Comprehensive.CUSTOM.name, comprehensive.getPropertyUnchecked("custom.name"));
    }

    @Test
    public void testGetPropertyUncheckedNoSuchField() throws Exception {
        assertNull(comprehensive.getPropertyUnchecked("booboooboo"));
    }

    @Test
    public void testGetPropertyUncheckedNestedNoSuchField() throws Exception {
        assertNull(comprehensive.getPropertyUnchecked("custom.fake"));
    }

    @Test
    public void testSetProperty() throws Exception {
        assertFalse(comprehensive.aInt == 500);
        comprehensive.setProperty("aint", "500");
        assertEquals(500, comprehensive.aInt);
    }

    @Test
    public void testSetPropertyNested() throws Exception {
        assertFalse(comprehensive.custom.name.equals("aname"));
        comprehensive.setProperty("custom.name", "aname");
        assertEquals("aname", comprehensive.custom.name);
    }

    @Test(expected = NoSuchFieldException.class)
    public void testSetPropertyNoSuchField() throws Exception {
        comprehensive.setProperty("booboooboo", "value");
    }

    @Test(expected = NoSuchFieldException.class)
    public void testSetPropertyNestedNoSuchField() throws Exception {
        comprehensive.setProperty("custom.fake", "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPropertyIllegalValue() throws Exception {
        comprehensive.setProperty("aint", "value");
    }

    @Test
    public void testSetPropertyUnchecked() throws Exception {
        assertFalse(comprehensive.aInt == 500);
        comprehensive.setPropertyUnchecked("aint", "500");
        assertEquals(500, comprehensive.aInt);
    }

    @Test
    public void testSetPropertyUncheckedNested() throws Exception {
        assertFalse(comprehensive.custom.name.equals("aname"));
        assertTrue(comprehensive.setPropertyUnchecked("custom.name", "aname"));
        assertEquals("aname", comprehensive.custom.name);
    }

    @Test
    public void testSetPropertyUncheckedNoSuchField() throws Exception {
        comprehensive.setPropertyUnchecked("booboooboo", "value");
    }

    @Test
    public void testSetPropertyUncheckedNestedNoSuchField() throws Exception {
        comprehensive.setPropertyUnchecked("custom.fake", "value");
    }

    public void testSetPropertyUncheckedIllegalValue() throws Exception {
        comprehensive.setPropertyUnchecked("aint", "value");
    }

    @Test
    public void testPropertiesWrapper() throws Exception {
        Custom custom = new Custom("custom");
        Properties properties = PropertiesWrapper.wrapObject(custom, ".");
        assertEquals(custom.name, properties.getProperty("name"));
    }

    @Test
    public void testSetPropertyFinalField() throws Exception {
        Comprehensive comp = new Comprehensive();
        Object original = comp.getProperty("custom");
        assertNotNull(original);
        Simple newValue = new Simple();
        newValue.string = "a";
        assertFalse(original.equals(newValue));
        comp.setProperty("simple", newValue.toString());
        assertEquals(newValue, comp.getProperty("simple"));
    }

    @Test
    public void testGetPropertyAlias() throws Exception {
        assertEquals(comprehensive.custom.name, comprehensive.getProperty("cname"));
    }

    @Test(expected = NoSuchFieldException.class)
    public void testGetPropertyFakeAlias() throws Exception {
        comprehensive.getProperty("bname");
    }

    @Test(expected = NoSuchFieldException.class)
    public void testGetPropertyBadAlias() throws Exception {
        PropertyAliases.createAlias(Comprehensive.class, "testing", "custom", "test");
        comprehensive.getProperty("testing");
    }

    @Test(expected = IllegalAccessException.class)
    public void testSetPropertyImmutable() throws Exception {
        comprehensive.setProperty("immutableString", new Custom("test").toString());
    }

    @Test
    public void testAddProperty() throws Exception {
        List<String> original = new ArrayList<String>(comprehensive.wordList);
        comprehensive.addProperty("wordList", "hubbub");
        assertFalse(original.equals(comprehensive.wordList));
        original.add("hubbub");
        assertEquals(original, comprehensive.getProperty("wordList"));
    }

    @Test
    public void testRemoveProperty() throws Exception {
        List<String> original = new ArrayList<String>(comprehensive.wordList);
        comprehensive.removeProperty("wordList", "test");
        assertFalse(original.equals(comprehensive.wordList));
        original.remove("test");
        assertEquals(original, comprehensive.getProperty("wordList"));
    }

    @Test
    public void testClearProperty() throws Exception {
        assertFalse(comprehensive.wordList.isEmpty());
        comprehensive.clearProperty("wordList");
        assertTrue(comprehensive.wordList.isEmpty());
    }

    @Test
    public void testSetCustomStringifierProperty() throws Exception {
        List<Simple> test = new ArrayList<Simple>();
        test.add(new Simple("hubbub"));
        assertFalse(test.equals(comprehensive.simpleList));
        comprehensive.setProperty("simpleList", "hubbub");
        assertEquals(test, comprehensive.getProperty("simpleList"));
    }

    @Test
    public void testAddCustomStringifierProperty() throws Exception {
        List<Simple> original = new ArrayList<Simple>(comprehensive.simpleList);
        comprehensive.addProperty("simpleList", "hubbub");
        assertFalse(original.equals(comprehensive.simpleList));
        original.add(new Simple("hubbub"));
        assertEquals(original, comprehensive.getProperty("simpleList"));
    }

    @Test
    public void testRemoveCustomStringifierProperty() throws Exception {
        List<Simple> original = new ArrayList<Simple>(comprehensive.simpleList);
        comprehensive.removeProperty("simpleList", "test");
        assertFalse(original.equals(comprehensive.simpleList));
        original.remove(new Simple("test"));
        assertEquals(original, comprehensive.getProperty("simpleList"));
    }

    @Test
    public void testClearCustomStringifierProperty() throws Exception {
        assertFalse(comprehensive.simpleList.isEmpty());
        comprehensive.clearProperty("simpleList");
        assertTrue(comprehensive.simpleList.isEmpty());
    }
}
