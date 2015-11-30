package pluginbase.config;

import pluginbase.config.examples.Child;
import pluginbase.config.examples.Comprehensive;
import pluginbase.config.examples.Parent;
import pluginbase.config.examples.Unknown;
import org.junit.Test;
import pluginbase.config.examples.Unregistered;
import pluginbase.config.field.FieldMap;
import pluginbase.config.field.FieldMapper;
import pluginbase.config.serializers.CustomSerializer;
import pluginbase.config.serializers.CustomSerializer2;

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

    @Test
    public void testSerializeWithFieldSerializer() {
        Comprehensive expected = new Comprehensive();
        expected.custom2.name = "aogrohjaha";
        expected.custom.data.array = new Object[] {1};
        expected.custom2.data.array = new Object[] {5,5,5};

        FieldMap fieldMap = FieldMapper.getFieldMap(Comprehensive.class);
        assertEquals(CustomSerializer.class, fieldMap.getField("custom").getSerializer().getClass());
        assertEquals(CustomSerializer2.class, fieldMap.getField("custom2").getSerializer().getClass());

        Comprehensive actual = SerializableConfig.deserializeAs(SerializableConfig.serialize(expected), Comprehensive.class);
        assertEquals(expected, actual);
    }
}
