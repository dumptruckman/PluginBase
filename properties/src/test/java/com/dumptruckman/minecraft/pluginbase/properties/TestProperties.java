package com.dumptruckman.minecraft.pluginbase.properties;

import java.util.*;

public class TestProperties extends MemoryProperties {

    private static final Map<String, String> DEFAULT_MAP = new HashMap<String, String>();
    private static final List<String> DEFAULT_LIST = new ArrayList<String>();
    static {
        DEFAULT_MAP.put("1", "poop");
        DEFAULT_MAP.put("loopty", "loop");
        DEFAULT_LIST.addAll(Arrays.asList("1", "dinosaur", "abc"));
    }

    public static final SimpleProperty<Boolean> SOME_BOOLEAN = PropertyFactory.newProperty(Boolean.class, "some_boolean", false).build();
    public static final ListProperty<String> SOME_LIST = PropertyFactory.newListProperty(String.class, "some_list", DEFAULT_LIST).build();
    public static final MappedProperty<String> SOME_MAP = PropertyFactory.newMappedProperty(String.class, "some_map", DEFAULT_MAP).build();
    public static final NestedProperty<NestedTest> SOME_NEST = PropertyFactory.newNestedProperty(NestedTest.class, "some_nest").build();

    public static interface NestedTest extends NestedProperties {
        public static final SimpleProperty<Boolean> SOME_BOOLEAN = PropertyFactory.newProperty(Boolean.class, "some_boolean", false).build();
        public static final NestedProperty<NestedNestedTest> SOME_NESTED_NEST = PropertyFactory.newNestedProperty(NestedNestedTest.class, "some_nested_nest").build();

        public static interface NestedNestedTest extends NestedProperties {
            public static final SimpleProperty<Boolean> SOME_BOOLEAN = PropertyFactory.newProperty(Boolean.class, "some_boolean", false).build();
        }
    }

    public TestProperties() {
        super(true, TestProperties.class);
    }

}
