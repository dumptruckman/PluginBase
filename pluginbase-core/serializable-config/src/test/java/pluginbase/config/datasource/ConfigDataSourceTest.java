package pluginbase.config.datasource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pluginbase.config.TestBase;
import pluginbase.config.datasource.gson.GsonDataSource;
import pluginbase.config.datasource.hocon.HoconDataSource;
import pluginbase.config.datasource.json.JsonDataSource;
import pluginbase.config.datasource.yaml.YamlDataSource;
import pluginbase.config.examples.Comprehensive;
import pluginbase.config.examples.Parent;

import java.io.File;

import static org.junit.Assert.*;

public class ConfigDataSourceTest extends TestBase {

    File hoconFile = new File("bin/test.conf");
    File jsonFile = new File("bin/test.json");
    File gsonFile = new File("bin/test.gson");
    File yamlFile = new File("bin/test.yml");
    Comprehensive expected;

    @Before
    public void setup() {
        expected = new Comprehensive();
        expected.aInt = 5;
        ((Parent) expected.stringObjectMap.get("parent")).aChild.aBoolean = false;
    }

    @After
    public void tearDown() {
        hoconFile.delete();
        jsonFile.delete();
        gsonFile.delete();
        yamlFile.delete();
    }

    @Test
    public void testHoconConfigDataSource() throws Exception {
        HoconDataSource dataSource = HoconDataSource.builder().setFile(hoconFile).build();
        dataSource.save(expected);
        dataSource = HoconDataSource.builder().setFile(hoconFile).build();
        Comprehensive actual = (Comprehensive) dataSource.load();
        assertEquals(expected, actual);
    }

    @Test
    public void testJsonConfigDataSource() throws Exception {
        JsonDataSource dataSource = JsonDataSource.builder().setFile(jsonFile).build();
        dataSource.save(expected);
        dataSource = JsonDataSource.builder().setFile(jsonFile).build();
        Comprehensive actual = (Comprehensive) dataSource.load();
        assertEquals(expected, actual);
    }

    @Test
    public void testYamlConfigDataSource() throws Exception {
        YamlDataSource dataSource = YamlDataSource.builder().setFile(yamlFile).build();
        dataSource.save(expected);
        dataSource = YamlDataSource.builder().setFile(yamlFile).build();
        Comprehensive actual = (Comprehensive) dataSource.load();
        assertEquals(expected, actual);
    }

    @Test
    public void testGsonConfigDataSource() throws Exception {
        GsonDataSource dataSource = GsonDataSource.builder().setFile(gsonFile).build();
        dataSource.save(expected);
        dataSource = GsonDataSource.builder().setFile(gsonFile).build();
        Comprehensive actual = (Comprehensive) dataSource.load();
        assertEquals(expected, actual);
    }
}
