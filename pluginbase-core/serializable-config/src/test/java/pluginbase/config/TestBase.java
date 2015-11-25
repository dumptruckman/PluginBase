package pluginbase.config;

import pluginbase.config.examples.Comprehensive;
import org.junit.Before;

public class TestBase {

    @Before
    public void registerClasses() {
        SerializableConfig.registerSerializableAsClass(Comprehensive.class);
    }
}
