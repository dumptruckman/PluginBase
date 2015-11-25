package pluginbase.config;

import pluginbase.config.examples.Child;
import pluginbase.config.examples.Comprehensive;
import pluginbase.config.examples.Parent;
import pluginbase.config.examples.Unknown;
import org.junit.Test;
import pluginbase.config.examples.Unregistered;

import static org.junit.Assert.*;

public class SerializableConfigTest extends TestBase {

    @Test
    public void testSerialize() throws Exception {
        Child child = new Child(true);
        Parent parent = new Parent(child);
        assertEquals("{" + SerializableConfig.SERIALIZED_TYPE_KEY + "="  + Parent.class.getName() + ", aChild={" + SerializableConfig.SERIALIZED_TYPE_KEY + "=" + Child.class.getName() + ", aBoolean=true}}", SerializableConfig.serialize(parent).toString());
    }

    @Test
    public void testDeserialize() throws Exception {
        Child child = new Child(true);
        Parent parent = new Parent(child);
        Object data = SerializableConfig.serialize(parent);
        Object deserialized = SerializableConfig.deserialize(data);
        assertEquals(parent, deserialized);
    }

    @Test
    public void testSerializeUnknownObject() throws Exception {
        Unknown unknown = new Unknown();
        try {
            SerializableConfig.serialize(unknown);
        } catch (IllegalArgumentException e) {
            fail();
        }
    }

    @Test
    public void testSerializeUnregistered() {
        Unregistered unregistered = new Unregistered();
        Comprehensive comprehensive = new Comprehensive();
        assertEquals(comprehensive, unregistered);
        Object serialized = SerializableConfig.serialize(unregistered);
        assertNotNull(serialized);
        Unregistered deserialized = (Unregistered) SerializableConfig.deserialize(unregistered);
        assertEquals(unregistered, deserialized);
        assertEquals(comprehensive, deserialized);
    }
}
