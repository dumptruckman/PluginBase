package pluginbase.config.field;

import org.junit.Test;
import pluginbase.config.TestBase;
import pluginbase.config.examples.Anum;
import pluginbase.config.examples.Comprehensive;

import static org.junit.Assert.*;

public class FieldTestRename extends TestBase {

    @Test
    public void testVirtualPropertyType() throws Exception {
        FieldMap fieldMap = FieldMapper.getFieldMap(Comprehensive.class);
        Field field = fieldMap.getField("virtualEnum");
        assertEquals(Anum.class, field.getType());
    }
}
