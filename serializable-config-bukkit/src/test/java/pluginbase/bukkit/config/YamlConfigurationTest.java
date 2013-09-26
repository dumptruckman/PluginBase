package pluginbase.bukkit.config;

import pluginbase.config.examples.Child;
import pluginbase.config.examples.Parent;
import org.bukkit.configuration.InvalidConfigurationException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class YamlConfigurationTest extends BukkitConfigurationTest {

    @Override
    public YamlConfiguration getConfig() {
        return new YamlConfiguration();
    }

    @Override
    public String getTestHeaderInput() {
        return "This is a sample\nheader.\n\nNewline above should be commented.\n\n";
    }

    @Override
    public String getTestHeaderResult() {
        return "# This is a sample\n# header.\n# \n# Newline above should be commented.\n\n";
    }

    @Override
    public String getTestValuesString() {
        return "integer: -2147483648\n" +
            "string: String Value\n" +
            "long: 9223372036854775807\n" +
            "true-boolean: true\n" +
            "false-boolean: false\n" +
            "vector:\n" +
            "  ==: Vector\n" +
            "  x: 12345.67\n" +
            "  y: 64.0\n" +
            "  z: -12345.6789\n" +
            "list:\n" +
            "- 1\n" +
            "- 2\n" +
            "- 3\n" +
            "- 4\n" +
            "- 5\n" +
            "'42': The Answer\n";
    }

    @Test
    public void testSaveToStringWithIndent() {
        YamlConfiguration config = getConfig();
        config.options().indent(9);

        config.set("section.key", 1);

        String result = config.saveToString();
        String expected = "section:\n         key: 1\n";

        assertEquals(expected, result);
    }

    @Test
    public void testYamlSerializableConfigBasic() throws Exception {
        Child child = new Child(true);
        Parent parent = new Parent(child);
        YamlConfiguration yamlConfig = getConfig();
        yamlConfig.set("test", parent);
        String yamlString = yamlConfig.saveToString();
        yamlConfig = getConfig();
        try {
            yamlConfig.loadFromString(yamlString);
        } catch (InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
        assertEquals(parent, yamlConfig.get("test"));
    }

    @Test
    public void testCommentInstrumentation() throws Exception {
        Child child = new Child(true);
        Parent parent = new Parent(child);
        YamlConfiguration yamlConfig = getConfig();
        yamlConfig.set("test", parent);
        yamlConfig.options().comments(true);
        String yamlString = yamlConfig.saveToString();
        assertTrue(yamlString.contains("# Test comment 1"));
        assertTrue(yamlString.contains("# Test comment 2"));
        assertTrue(yamlString.contains("# Test boolean comments"));
    }
}