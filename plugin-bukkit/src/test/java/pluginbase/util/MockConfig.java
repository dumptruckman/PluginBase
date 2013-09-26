package pluginbase.util;

import pluginbase.config.annotation.Comment;
import pluginbase.config.annotation.Name;
import pluginbase.plugin.PluginBase;
import pluginbase.plugin.Settings;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Comment("===[ PluginBase Settings ]===")
public class MockConfig extends Settings {

    @Comment("===[ PluginBase Test ]===")
    public boolean test = true;

    @Name("specific_test")
    public Map<String, Integer> specificTest = new HashMap<String, Integer>();

    @Name("list_test")
    public List<Integer> listTest = new LinkedList<Integer>();

    @Comment("ababadfga")
    public Nested nested = new Nested();

    public MockConfig(@NotNull PluginBase plugin) {
        super(plugin);
    }

    public static class Nested {

        @Comment("# TEADFA")
        @Name("double_nested")
        public DoubleNested nestedTest = new DoubleNested();

        @Comment("# ===[ Nested Test ]===")
        @Name("nested_test")
        public boolean test = true;

        private Nested() { }

        public static class DoubleNested {

            @Comment("# ===[ Double Nested Test ]===")
            @Name("double_nested_test")
            public boolean test = true;

            private DoubleNested() { }
        }
    }
}
