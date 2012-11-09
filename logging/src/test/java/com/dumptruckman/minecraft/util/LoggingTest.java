package com.dumptruckman.minecraft.util;

import com.dumptruckman.minecraft.util.Logging.InterceptedLogger;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.MockGateway;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PluginDescriptionFile.class)
public class LoggingTest {

    static final String NAME = "Logging-Test";
    static final String VERSION = "1.2.3-test";

    static final String SIMPLE_MESSAGE = "This is a test.";
    static final String ARGS_MESSAGE = "This is a %s test with some %s%s";

    Plugin plugin;
    final TestHandler handler = new TestHandler();

    @Before
    public void setUp() throws Exception {
        MockGateway.MOCK_STANDARD_METHODS = false;
        plugin = mock(Plugin.class);
        when(plugin.getName()).thenReturn("Logging-Test");
        final PluginDescriptionFile pdf = PowerMockito.spy(new PluginDescriptionFile(NAME, VERSION,
                "com.dumptruckman.minecraft.util.LoggingTest"));
        when(plugin.getDescription()).thenReturn(pdf);
        FileUtils.deleteFolder(new File("bin"));
        final File testFolder = new File("bin/test/server/plugins/Logging-Test");
        testFolder.mkdirs();
        when(plugin.getDataFolder()).thenReturn(testFolder);
        Logging.init(plugin);
        ((InterceptedLogger) Logging.getLogger()).logger.addHandler(handler);
    }

    @After
    public void tearDown() throws Exception {
        Logging.shutdown();
        //FileUtils.deleteFolder(new File("bin"));
    }

    @Test
    public void testInit() throws Exception {
        assertEquals(Logging.name, plugin.getName());
        assertEquals(Logging.version, plugin.getDescription().getVersion());
        assertEquals(DebugLog.debugLevel, DebugLog.ORIGINAL_DEBUG_LEVEL);
        assertEquals(Logging.plugin, plugin);
    }

    @Test
    public void testShutdown() throws Exception {
        Logging.shutdown();
        assertEquals(Logging.name, Logging.ORIGINAL_NAME);
        assertEquals(Logging.version, Logging.ORIGINAL_VERSION);
        assertEquals(DebugLog.debugLevel, DebugLog.ORIGINAL_DEBUG_LEVEL);
        assertEquals(Logging.debug, Logging.ORIGINAL_DEBUG);
        assertNull(Logging.plugin);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDebugLevelTooHigh() throws Exception {
        Logging.setDebugLevel(4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDebugLevelTooLow() throws Exception {
        Logging.setDebugLevel(-1);
    }

    @Test
    public void testSetDebugLevel() throws Exception {
        assertNull(Logging.debugLog);
        assertTrue(DebugLog.isClosed());
        Logging.setDebugLevel(1);
        assertFalse(DebugLog.isClosed());
        assertEquals(DebugLog.getLoggerName(), plugin.getName());
        assertEquals(DebugLog.getFileName(), Logging.getDebugFileName(plugin));
        assertNotNull(Logging.debugLog);
        final DebugLog debugLog = Logging.debugLog;
        assertSame(DebugLog.getDebugLogger(), debugLog);
        Logging.setDebugLevel(2);
        assertSame(debugLog, Logging.debugLog);
        Logging.setDebugLevel(3);
        assertSame(debugLog, Logging.debugLog);
        Logging.setDebugLevel(0);
        assertNull(Logging.debugLog);
        assertTrue(DebugLog.isClosed());
    }

    @Test
    public void testGetDebugLevel() throws Exception {
        assertEquals(Logging.getDebugLevel(), 0);
        for (int i = 3; i >= 0; i--) {
            Logging.setDebugLevel(i);
            assertEquals(Logging.getDebugLevel(), i);
        }
    }

    @Test
    public void testCloseDebugLog() throws Exception {
        Logging.setDebugLevel(3);
        assertFalse(DebugLog.isClosed());
        Logging.closeDebugLog();
        assertTrue(DebugLog.isClosed());
    }

    @Test
    public void testGetPrefixedMessage() throws Exception {
        assertEquals("[" + NAME + "] " + SIMPLE_MESSAGE, Logging.getPrefixedMessage(SIMPLE_MESSAGE, false));
        assertEquals("[" + NAME + " " + VERSION + "] " + SIMPLE_MESSAGE, Logging.getPrefixedMessage(SIMPLE_MESSAGE, true));
    }

    @Test
    public void testSetGetDebugPrefix() throws Exception {
        final String debugging = "-debugging";
        assertEquals("[" + NAME + Logging.ORIGINAL_DEBUG + "] " + SIMPLE_MESSAGE, Logging.getDebugString(SIMPLE_MESSAGE));
        Logging.setDebugPrefix(debugging);
        assertEquals("[" + NAME + debugging + "] " + SIMPLE_MESSAGE, Logging.getDebugString(SIMPLE_MESSAGE));
        Logging.init(plugin);
        assertEquals("[" + NAME + Logging.ORIGINAL_DEBUG + "] " + SIMPLE_MESSAGE, Logging.getDebugString(SIMPLE_MESSAGE));
    }

    static class TestHandler extends Handler {

        static Level level;
        static RecordTester tester;

        Collection<LogRecord> records = new LinkedHashSet<LogRecord>();

        @Override
        public void publish(LogRecord record) {
            assertEquals(Logging.getLogger().getName(), record.getLoggerName());
            assertEquals(level, record.getLevel());
            records.add(record);
            tester.test(record);
        }

        @Override
        public void flush() {
            records.clear();
        }

        public boolean hasMessage(Level level, String message) {
            for (LogRecord record : records) {
                if (record.getLevel() == level && record.getMessage().equals(message)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public void close() throws SecurityException { }
    }

    static abstract class RecordTester {
        public abstract void test(final LogRecord record);
    }

    public void testLog() throws Exception {
        Logging.setDebugLevel(3);
        TestHandler.tester = new RecordTester() {
            @Override
            public void test(LogRecord record) {
                assertEquals(Logging.getPrefixedMessage(SIMPLE_MESSAGE, false), record.getMessage());
            }
        };
        TestHandler.level = Level.INFO;
        Logging.log(false, Level.INFO, SIMPLE_MESSAGE);
        assertTrue(handler.hasMessage(handler.level, Logging.getPrefixedMessage(SIMPLE_MESSAGE, false)));
        TestHandler.level = Level.WARNING;
        Logging.log(false, Level.WARNING, SIMPLE_MESSAGE);
        assertTrue(handler.hasMessage(handler.level, Logging.getPrefixedMessage(SIMPLE_MESSAGE, false)));
        TestHandler.level = Level.SEVERE;
        Logging.log(false, Level.SEVERE, SIMPLE_MESSAGE);
        assertTrue(handler.hasMessage(handler.level, Logging.getPrefixedMessage(SIMPLE_MESSAGE, false)));
        TestHandler.level = Level.INFO;
        Logging.log(false, Level.CONFIG, SIMPLE_MESSAGE);
        assertTrue(handler.hasMessage(handler.level, Logging.getPrefixedMessage(SIMPLE_MESSAGE, false)));
        handler.flush();

        TestHandler.tester = new RecordTester() {
            @Override
            public void test(LogRecord record) {
                assertEquals(Logging.getPrefixedMessage(SIMPLE_MESSAGE, true), record.getMessage());
            }
        };
        TestHandler.level = Level.INFO;
        Logging.log(true, Level.INFO, SIMPLE_MESSAGE);
        assertTrue(handler.hasMessage(handler.level, Logging.getPrefixedMessage(SIMPLE_MESSAGE, true)));
        TestHandler.level = Level.WARNING;
        Logging.log(true, Level.WARNING, SIMPLE_MESSAGE);
        assertTrue(handler.hasMessage(handler.level, Logging.getPrefixedMessage(SIMPLE_MESSAGE, true)));
        TestHandler.level = Level.SEVERE;
        Logging.log(true, Level.SEVERE, SIMPLE_MESSAGE);
        assertTrue(handler.hasMessage(handler.level, Logging.getPrefixedMessage(SIMPLE_MESSAGE, true)));
        TestHandler.level = Level.INFO;
        Logging.log(true, Level.CONFIG, SIMPLE_MESSAGE);
        assertTrue(handler.hasMessage(handler.level, Logging.getPrefixedMessage(SIMPLE_MESSAGE, true)));
        handler.flush();

        TestHandler.tester = new RecordTester() {
            @Override
            public void test(LogRecord record) {
                assertEquals(Logging.getDebugString(SIMPLE_MESSAGE), record.getMessage());
            }
        };
        TestHandler.level = Level.INFO;
        Logging.log(false, Level.FINE, SIMPLE_MESSAGE);
        assertTrue(handler.hasMessage(TestHandler.level, Logging.getDebugString(SIMPLE_MESSAGE)));
        handler.flush();
        Logging.log(false, Level.FINER, SIMPLE_MESSAGE);
        assertTrue(handler.hasMessage(TestHandler.level, Logging.getDebugString(SIMPLE_MESSAGE)));
        handler.flush();
        Logging.log(false, Level.FINEST, SIMPLE_MESSAGE);
        assertTrue(handler.hasMessage(TestHandler.level, Logging.getDebugString(SIMPLE_MESSAGE)));
        handler.flush();
        Logging.log(true, Level.FINE, SIMPLE_MESSAGE);
        assertTrue(handler.hasMessage(TestHandler.level, Logging.getDebugString(SIMPLE_MESSAGE)));
        handler.flush();
        Logging.log(true, Level.FINER, SIMPLE_MESSAGE);
        assertTrue(handler.hasMessage(TestHandler.level, Logging.getDebugString(SIMPLE_MESSAGE)));
        handler.flush();
        Logging.log(true, Level.FINEST, SIMPLE_MESSAGE);
        assertTrue(handler.hasMessage(TestHandler.level, Logging.getDebugString(SIMPLE_MESSAGE)));
        handler.flush();

        final Object o = new Object();
        TestHandler.tester = new RecordTester() {
            @Override
            public void test(LogRecord record) {
                assertEquals(Logging.getPrefixedMessage(String.format(ARGS_MESSAGE, "poop", 2, o), false), record.getMessage());
            }
        };
        TestHandler.level = Level.INFO;
        Logging.log(false, Level.INFO, ARGS_MESSAGE, "poop", 2, o);

        TestHandler.tester = new RecordTester() {
            @Override
            public void test(LogRecord record) {
                assertEquals(Logging.getPrefixedMessage(String.format(ARGS_MESSAGE, "poop", 2, o), false), record.getMessage());
            }
        };
        TestHandler.level = Level.INFO;
        Logging.log(false, Level.INFO, ARGS_MESSAGE, "poop", 2);
    }

    @Test
    public void testGetLogger() throws Exception {
        Logging.setDebugLevel(3);
        final Logger logger = Logging.getLogger();

        TestHandler.tester = new RecordTester() {
            @Override
            public void test(LogRecord record) {
                assertEquals(Logging.getPrefixedMessage(SIMPLE_MESSAGE, false), record.getMessage());
            }
        };
        TestHandler.level = Level.INFO;
        logger.log(Level.INFO, SIMPLE_MESSAGE);
        TestHandler.level = Level.WARNING;
        logger.log(Level.WARNING, SIMPLE_MESSAGE);
        TestHandler.level = Level.SEVERE;
        logger.log(Level.SEVERE, SIMPLE_MESSAGE);

        TestHandler.tester = new RecordTester() {
            @Override
            public void test(LogRecord record) {
                assertEquals(Logging.getDebugString(SIMPLE_MESSAGE), record.getMessage());
            }
        };
        TestHandler.level = Level.INFO;
        logger.log(Level.FINE, SIMPLE_MESSAGE);
        logger.log(Level.FINER, SIMPLE_MESSAGE);
        logger.log(Level.FINEST, SIMPLE_MESSAGE);

    }
}
