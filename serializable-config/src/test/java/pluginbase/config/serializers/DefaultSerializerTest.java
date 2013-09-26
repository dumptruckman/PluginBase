package pluginbase.config.serializers;

import pluginbase.config.TestBase;
import pluginbase.config.examples.Comprehensive;
import pluginbase.config.examples.Unknown;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DefaultSerializerTest extends TestBase {

    Serializer serializer;

    @Before
    public void setUp() throws Exception {
        serializer = new DefaultSerializer();
    }

    @Test
    public void testSerializeDeserializeComprehensive() throws Exception {
        Comprehensive comprehensive = new Comprehensive();
        Object serializedForm = serializer.serialize(comprehensive);
        Comprehensive deserializedForm = (Comprehensive) serializer.deserialize(serializedForm, Comprehensive.class);
        assertEquals(comprehensive, deserializedForm);
    }

    @Test
    public void testSerializeDeserializeComprehensiveNullKeyMap() throws Exception {
        Comprehensive comprehensive = new Comprehensive();
        comprehensive.stringObjectMap.put(null, "something");
        Object serializedForm = serializer.serialize(comprehensive);
        Comprehensive deserializedForm = (Comprehensive) serializer.deserialize(serializedForm, Comprehensive.class);
        assertFalse(comprehensive.equals(deserializedForm));
    }

    @Test
    public void testSerializeDeserializeComprehensiveNullValueMap() throws Exception {
        Comprehensive comprehensive = new Comprehensive();
        comprehensive.stringObjectMap.put("something", null);
        Object serializedForm = serializer.serialize(comprehensive);
        Comprehensive deserializedForm = (Comprehensive) serializer.deserialize(serializedForm, Comprehensive.class);
        assertFalse(comprehensive.equals(deserializedForm));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeUnknownObject() throws Exception {
        serializer.serialize(new Unknown());
    }
}
