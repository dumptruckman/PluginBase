package pluginbase.config.properties;

import pluginbase.config.TestBase;
import pluginbase.config.examples.Comprehensive;
import pluginbase.config.examples.Custom;
import org.junit.Before;
import org.junit.Test;

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

    @Test(expected = IllegalAccessException.class)
    public void testSetPropertyFinalField() throws Exception {
        Comprehensive comp = new Comprehensive();
        comp.setProperty(new Custom("a"), "custom");
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
    public void testSetPropertyFinal() throws Exception {
        comprehensive.setProperty("newValue", "finalString");
    }
}
