package com.dumptruckman.minecraft.pluginbase.config.properties;

import com.dumptruckman.minecraft.pluginbase.config.TestBase;
import com.dumptruckman.minecraft.pluginbase.config.examples.Comprehensive;
import com.dumptruckman.minecraft.pluginbase.config.field.Field;
import com.dumptruckman.minecraft.pluginbase.config.field.FieldMap;
import com.dumptruckman.minecraft.pluginbase.config.field.FieldMapper;
import com.dumptruckman.minecraft.pluginbase.config.field.Validator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CustomValidatorTest extends TestBase {

    @Test
    public void testCustomValidator() {
        FieldMap fieldMap = FieldMapper.getFieldMap(Comprehensive.class);
        Field field = fieldMap.getField("name");
        assertNotNull(field);
        Validator validator = field.getValidator();
        assertNotNull(validator);
        assertEquals(Comprehensive.NameValidator.class, validator.getClass());

        field = fieldMap.getField("custom");
        assertNotNull(field);
        validator = field.getValidator();
        assertNull(validator);
    }
}
