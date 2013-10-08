package pluginbase.config;

import pluginbase.config.examples.Child;
import pluginbase.config.examples.Comprehensive;
import pluginbase.config.examples.Custom;
import pluginbase.config.examples.Parent;
import pluginbase.config.examples.Recursive;
import org.junit.Before;
import pluginbase.config.examples.Simple;

public class TestBase {

    @Before
    public void registerClasses() {
        SerializationRegistrar.registerClass(Parent.class);
        SerializationRegistrar.registerClass(Child.class);
        SerializationRegistrar.registerClass(Comprehensive.class);
        SerializationRegistrar.registerClass(Custom.class);
        SerializationRegistrar.registerClass(Recursive.class);
        SerializationRegistrar.registerClass(Simple.class);
    }
}
