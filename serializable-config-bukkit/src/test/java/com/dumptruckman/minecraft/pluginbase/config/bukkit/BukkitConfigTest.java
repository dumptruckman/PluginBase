package com.dumptruckman.minecraft.pluginbase.config.bukkit;

import com.dumptruckman.minecraft.pluginbase.config.ConfigSerializer;
import com.dumptruckman.minecraft.pluginbase.config.bukkit.examples.Child;
import com.dumptruckman.minecraft.pluginbase.config.bukkit.examples.Parent;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public abstract class BukkitConfigTest extends MemoryConfigurationTest {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Override
    public abstract BukkitConfig getConfig();

    public abstract String getTestValuesString();

    public abstract String getTestHeaderInput();

    public abstract String getTestHeaderResult();

    @Test
    public void testSave_File() throws Exception {
        FileConfiguration config = getConfig();
        File file = testFolder.newFile("test.config");

        for (Map.Entry<String, Object> entry : getTestValues().entrySet()) {
            config.set(entry.getKey(), entry.getValue());
        }

        config.save(file);

        assertTrue(file.isFile());
    }

    @Test
    public void testSave_String() throws Exception {
        FileConfiguration config = getConfig();
        File file = testFolder.newFile("test.config");

        for (Map.Entry<String, Object> entry : getTestValues().entrySet()) {
            config.set(entry.getKey(), entry.getValue());
        }

        config.save(file.getAbsolutePath());

        assertTrue(file.isFile());
    }

    @Test
    public void testSaveToString() {
        FileConfiguration config = getConfig();

        for (Map.Entry<String, Object> entry : getTestValues().entrySet()) {
            config.set(entry.getKey(), entry.getValue());
        }

        String result = config.saveToString();
        String expected = getTestValuesString();

        assertEquals(expected, result);
    }

    @Test
    public void testLoad_File() throws Exception {
        FileConfiguration config = getConfig();
        File file = testFolder.newFile("test.config");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        String saved = getTestValuesString();
        Map<String, Object> values = getTestValues();

        try {
            writer.write(saved);
        } finally {
            writer.close();
        }

        config.load(file);

        for (Map.Entry<String, Object> entry : values.entrySet()) {
            assertEquals(entry.getValue(), config.get(entry.getKey()));
        }

        assertEquals(values.keySet(), config.getKeys(true));
    }

    @Test
    public void testLoad_String() throws Exception {
        FileConfiguration config = getConfig();
        File file = testFolder.newFile("test.config");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        String saved = getTestValuesString();
        Map<String, Object> values = getTestValues();

        try {
            writer.write(saved);
        } finally {
            writer.close();
        }

        config.load(file.getAbsolutePath());

        for (Map.Entry<String, Object> entry : values.entrySet()) {
            assertEquals(entry.getValue(), config.get(entry.getKey()));
        }

        assertEquals(values.keySet(), config.getKeys(true));
    }

    @Test
    public void testLoadFromString() throws Exception {
        FileConfiguration config = getConfig();
        Map<String, Object> values = getTestValues();
        String saved = getTestValuesString();

        config.loadFromString(saved);

        for (Map.Entry<String, Object> entry : values.entrySet()) {
            assertEquals(entry.getValue(), config.get(entry.getKey()));
        }

        assertEquals(values.keySet(), config.getKeys(true));
        assertEquals(saved, config.saveToString());
    }

    @Test
    public void testSaveToStringWithHeader() {
        FileConfiguration config = getConfig();
        config.options().header(getTestHeaderInput());

        for (Map.Entry<String, Object> entry : getTestValues().entrySet()) {
            config.set(entry.getKey(), entry.getValue());
        }

        String result = config.saveToString();
        String expected = getTestHeaderResult() + "\n" + getTestValuesString();

        assertEquals(expected, result);
    }

    @Test
    public void testParseHeader() throws Exception {
        FileConfiguration config = getConfig();
        Map<String, Object> values = getTestValues();
        String saved = getTestValuesString();
        String header = getTestHeaderResult();
        String expected = getTestHeaderInput();

        config.loadFromString(header + "\n" + saved);

        assertEquals(expected, config.options().header());

        for (Map.Entry<String, Object> entry : values.entrySet()) {
            assertEquals(entry.getValue(), config.get(entry.getKey()));
        }

        assertEquals(values.keySet(), config.getKeys(true));
        assertEquals(header + "\n" + saved, config.saveToString());
    }

    @Test
    public void testCopyHeader() throws Exception {
        FileConfiguration config = getConfig();
        FileConfiguration defaults = getConfig();
        Map<String, Object> values = getTestValues();
        String saved = getTestValuesString();
        String header = getTestHeaderResult();
        String expected = getTestHeaderInput();

        defaults.loadFromString(header);
        config.loadFromString(saved);
        config.setDefaults(defaults);

        assertNull(config.options().header());
        assertEquals(expected, defaults.options().header());

        for (Map.Entry<String, Object> entry : values.entrySet()) {
            assertEquals(entry.getValue(), config.get(entry.getKey()));
        }

        assertEquals(values.keySet(), config.getKeys(true));
        assertEquals(header + "\n" + saved, config.saveToString());

        config = getConfig();
        config.loadFromString(getTestHeaderResult() + saved);
        assertEquals(getTestHeaderResult() + saved, config.saveToString());
    }

    @Test
    public void testReloadEmptyConfig() throws Exception {
        FileConfiguration config = getConfig();

        assertEquals("", config.saveToString());

        config = getConfig();
        config.loadFromString("");

        assertEquals("", config.saveToString());

        config = getConfig();
        config.loadFromString("\n\n"); // Should trim the first newlines of a header

        assertEquals("", config.saveToString());
    }

    @Test
    public void testGetToObject() throws Exception {
        Child child = new Child(true);
        Parent parent = new Parent(child);
        BukkitConfig config = getConfig();
        config.set("test", parent);
        String yamlString = config.saveToString();
        List<String> lines = new ArrayList<String>(Arrays.asList(yamlString.split("\n")));
        StringBuilder builder = new StringBuilder();
        for (String line : lines) {
            if (!line.contains(ConfigSerializer.SERIALIZED_TYPE_KEY)) {
                builder.append(line);
                builder.append("\n");
            }
        }
        yamlString = builder.toString();
        System.out.println(yamlString);
        config = getConfig();
        try {
            config.loadFromString(yamlString);
        } catch (InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
        assertFalse(parent.equals(config.get("test")));
        assertEquals(parent, config.getToObject("test", new Parent(new Child(false))));
    }
}
