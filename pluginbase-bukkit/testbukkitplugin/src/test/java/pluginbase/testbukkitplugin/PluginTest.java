package pluginbase.testbukkitplugin;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import pluginbase.testingbukkit.FileLocations;

@RunWith(PowerMockRunner.class)
public abstract class PluginTest {

    @Before
    public void setupDirectories() throws Exception {
        FileLocations.setupDirectories();
        extraSetup();
    }

    /**
     * Implement to perform extra @Before test steps after test directories are setup.
     */
    protected void extraSetup() throws Exception { }

    @After
    public void cleanupDirectories() throws Exception {
        extraCleanup();
        FileLocations.cleanupDirectories();
    }

    /**
     * Implement to perform extra @After test steps before test directories are cleaned up.
     */
    protected void extraCleanup() throws Exception { }
}
