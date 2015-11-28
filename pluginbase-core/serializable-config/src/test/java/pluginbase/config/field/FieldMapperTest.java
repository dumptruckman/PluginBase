package pluginbase.config.field;

import pluginbase.config.TestBase;
import pluginbase.config.examples.*;
import pluginbase.config.serializers.CustomSerializer;
import pluginbase.config.serializers.CustomSerializer2;
import org.junit.Test;

import static org.junit.Assert.*;

public class FieldMapperTest extends TestBase {

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

    @Test
    public void testMapFields() {
        Comprehensive expected = new Comprehensive();
        expected.aInt = 125612;
        expected.custom.name = "asdhhojao";
        expected.simpleList.add(new Simple("asdfaghrhah"));
        expected.custom2 = new Custom("135r3trgah");

        Comprehensive actual = new Comprehensive();
        assertFalse(expected.equals(actual));

        FieldMapper.mapFields(expected, actual);

        assertEquals(expected, actual);
    }
}
