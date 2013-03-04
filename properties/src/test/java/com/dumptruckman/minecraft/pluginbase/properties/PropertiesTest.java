package com.dumptruckman.minecraft.pluginbase.properties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
public class PropertiesTest {

    @SuppressWarnings("ConstantConditions")
    @Test
    public void testMemoryProperties() throws Exception {
        Properties props = new TestProperties();
        assertEquals(TestProperties.SOME_BOOLEAN.getDefault(), props.get(TestProperties.SOME_BOOLEAN));
        assertEquals(TestProperties.SOME_LIST.getDefault(), props.get(TestProperties.SOME_LIST));
        assertEquals(TestProperties.SOME_MAP.getDefault(), props.get(TestProperties.SOME_MAP));
        assertEquals(TestProperties.NestedTest.SOME_BOOLEAN.getDefault(), props.get(TestProperties.SOME_NEST).get(TestProperties.NestedTest.SOME_BOOLEAN));
        assertEquals(TestProperties.NestedTest.NestedNestedTest.SOME_BOOLEAN.getDefault(), props.get(TestProperties.SOME_NEST).get(TestProperties.NestedTest.SOME_NESTED_NEST).get(TestProperties.NestedTest.NestedNestedTest.SOME_BOOLEAN));

        props.set(TestProperties.SOME_BOOLEAN, true);
        assertEquals(true, props.get(TestProperties.SOME_BOOLEAN));

        List<String> list = props.get(TestProperties.SOME_LIST);
        list.add("weeee");
        props.set(TestProperties.SOME_LIST, list);
        assertEquals(list, props.get(TestProperties.SOME_LIST));

        Map<String, String> map = props.get(TestProperties.SOME_MAP);
        map.remove("1");
        props.set(TestProperties.SOME_MAP, map);
        assertEquals(map, props.get(TestProperties.SOME_MAP));

        props.get(TestProperties.SOME_NEST).set(TestProperties.NestedTest.SOME_BOOLEAN, true);
        assertEquals(true, props.get(TestProperties.SOME_NEST).get(TestProperties.NestedTest.SOME_BOOLEAN));
    }
}
