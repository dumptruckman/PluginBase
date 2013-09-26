package pluginbase.config;

import pluginbase.config.examples.Child;
import pluginbase.config.examples.Parent;
import pluginbase.config.examples.Unknown;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class ConfigSerializerTest extends TestBase {

    @Test
    public void testSerialize() throws Exception {
        Child child = new Child(true);
        Parent parent = new Parent(child);
        assertEquals("{" + ConfigSerializer.SERIALIZED_TYPE_KEY + "="  + Parent.class.getName() + ", aChild={" + ConfigSerializer.SERIALIZED_TYPE_KEY + "=" + Child.class.getName() + ", aBoolean=true}}", ConfigSerializer.serialize(parent).toString());

    }

    @Test
    public void testDeserialize() throws Exception {
        Child child = new Child(true);
        Parent parent = new Parent(child);
        Map<String, Object> data = ConfigSerializer.serialize(parent);
        Object deserialized = ConfigSerializer.deserialize(data);
        assertEquals(parent, deserialized);
    }

    @Test
    public void testSerializeUnknownObject() throws Exception {
        Unknown unknown = new Unknown();
        boolean thrown = false;
        try {
            ConfigSerializer.serialize(unknown);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }
}
