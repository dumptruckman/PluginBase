package com.dumptruckman.minecraft.pluginbase.logging;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.MockGateway;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
public class LoggingTest {

    static final String NAME = "Logging-Test";
    static final String SIMPLE_MESSAGE = "This is a test.";
    static final String ARGS_MESSAGE = "This is a %s test with some %s%s";

    LoggablePlugin plugin;
    final TestHandler handler = new TestHandler();

    PluginLogger logging;

    @Before
    public void setUp() throws Exception {
        MockGateway.MOCK_STANDARD_METHODS = false;
        plugin = mock(LoggablePlugin.class);
        when(plugin.getName()).thenReturn("Logging-Test");
        FileUtils.deleteFolder(new File("bin"));
        final File testFolder = new File("bin/test/server/plugins/Logging-Test");
        testFolder.mkdirs();
        when(plugin.getDataFolder()).thenReturn(testFolder);
        logging = PluginLogger.getLogger(plugin);
    }

    @After
    public void tearDown() throws Exception {
        logging.shutdown();
        PluginLogger.initializedLoggers.clear();
    }

    @Test
    public void testInit() throws Exception {
        assertEquals(logging.name, plugin.getName());
        assertEquals(logging.debugLog.debugLevel, DebugLog.ORIGINAL_DEBUG_LEVEL);
        assertEquals(logging.plugin, plugin);
        assertNotNull(logging.debugLog);
    }

    @Test
    public void testShutdown() throws Exception {
        logging.shutdown();
        assertEquals(logging.debugLog.debugLevel, DebugLog.ORIGINAL_DEBUG_LEVEL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDebugLevelTooHigh() throws Exception {
        logging.setDebugLevel(4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDebugLevelTooLow() throws Exception {
        logging.setDebugLevel(-1);
    }

    @Test
    public void testSetDebugLevel() throws Exception {
        assertTrue(logging.debugLog.isClosed());
        logging.setDebugLevel(1);
        assertFalse(logging.debugLog.isClosed());
        assertEquals(logging.logger, logging.debugLog.getLogger());
        assertEquals(logging.debugLog.getFileName(), PluginLogger.getDebugFileName(plugin));
        logging.setDebugLevel(0);
        assertTrue(logging.debugLog.isClosed());
        logging.setDebugLevel(1);
        assertFalse(logging.debugLog.isClosed());
    }

    @Test
    public void testGetDebugLevel() throws Exception {
        assertEquals(logging.getDebugLevel(), 0);
        for (int i = 3; i >= 0; i--) {
            logging.setDebugLevel(i);
            assertEquals(logging.getDebugLevel(), i);
        }
    }

    @Test
    public void testCloseDebugLog() throws Exception {
        logging.setDebugLevel(3);
        assertFalse(logging.debugLog.isClosed());
        logging.shutdown();
        assertTrue(logging.debugLog.isClosed());
    }

    @Test
    public void testGetPrefixedMessage() throws Exception {
        assertEquals("[" + NAME + "] " + SIMPLE_MESSAGE, logging.getPrefixedMessage(SIMPLE_MESSAGE));
    }

    @Test
    public void testSetGetDebugPrefix() throws Exception {
        final String debugging = "-debugging";
        assertEquals("[" + NAME + PluginLogger.ORIGINAL_DEBUG + "] " + SIMPLE_MESSAGE, logging.getDebugString(SIMPLE_MESSAGE));
        logging.setDebugPrefix(debugging);
        assertEquals("[" + NAME + debugging + "] " + SIMPLE_MESSAGE, logging.getDebugString(SIMPLE_MESSAGE));
    }

    class TestHandler extends Handler {

        Level level;
        RecordTester tester;

        Collection<LogRecord> records = new LinkedHashSet<LogRecord>();

        @Override
        public void publish(LogRecord record) {
            assertEquals(logging.getName(), record.getLoggerName());
            assertEquals(level, record.getLevel());
            records.add(record);
            tester.test(record);
            System.out.println(record.getLevel() + " " + record.getMessage());
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

    @Test
    public void testLog() throws Exception {
        logging.setDebugLevel(3);
        logging.logger.addHandler(handler);
        handler.tester = new RecordTester() {
            @Override
            public void test(LogRecord record) {
                assertEquals(logging.getPrefixedMessage(SIMPLE_MESSAGE), record.getMessage());
            }
        };

        handler.level = Level.INFO;
        logging.log(Level.INFO, SIMPLE_MESSAGE);
        assertTrue(handler.hasMessage(handler.level, logging.getPrefixedMessage(SIMPLE_MESSAGE)));
        handler.level = Level.WARNING;
        logging.log(Level.WARNING, SIMPLE_MESSAGE);
        assertTrue(handler.hasMessage(handler.level, logging.getPrefixedMessage(SIMPLE_MESSAGE)));
        handler.level = Level.SEVERE;
        logging.log(Level.SEVERE, SIMPLE_MESSAGE);
        assertTrue(handler.hasMessage(handler.level, logging.getPrefixedMessage(SIMPLE_MESSAGE)));
        handler.level = Level.INFO;
        logging.log(Level.CONFIG, SIMPLE_MESSAGE);
        assertTrue(handler.hasMessage(handler.level, logging.getPrefixedMessage(SIMPLE_MESSAGE)));
        handler.flush();

        handler.tester = new RecordTester() {
            @Override
            public void test(LogRecord record) {
                assertEquals(logging.getPrefixedMessage(SIMPLE_MESSAGE), record.getMessage());
            }
        };
        handler.level = Level.INFO;
        logging.log(Level.INFO, SIMPLE_MESSAGE);
        assertTrue(handler.hasMessage(handler.level, logging.getPrefixedMessage(SIMPLE_MESSAGE)));
        handler.level = Level.WARNING;
        logging.log(Level.WARNING, SIMPLE_MESSAGE);
        assertTrue(handler.hasMessage(handler.level, logging.getPrefixedMessage(SIMPLE_MESSAGE)));
        handler.level = Level.SEVERE;
        logging.log(Level.SEVERE, SIMPLE_MESSAGE);
        assertTrue(handler.hasMessage(handler.level, logging.getPrefixedMessage(SIMPLE_MESSAGE)));
        handler.level = Level.INFO;
        logging.log(Level.CONFIG, SIMPLE_MESSAGE);
        assertTrue(handler.hasMessage(handler.level, logging.getPrefixedMessage(SIMPLE_MESSAGE)));
        handler.flush();

        handler.tester = new RecordTester() {
            @Override
            public void test(LogRecord record) {
                assertEquals(logging.getDebugString(SIMPLE_MESSAGE), record.getMessage());
            }
        };
        handler.level = Level.INFO;
        logging.log(Level.FINE, SIMPLE_MESSAGE);
        assertTrue(handler.hasMessage(handler.level, logging.getDebugString(SIMPLE_MESSAGE)));
        handler.flush();
        logging.log(Level.FINER, SIMPLE_MESSAGE);
        assertTrue(handler.hasMessage(handler.level, logging.getDebugString(SIMPLE_MESSAGE)));
        handler.flush();
        logging.log(Level.FINEST, SIMPLE_MESSAGE);
        assertTrue(handler.hasMessage(handler.level, logging.getDebugString(SIMPLE_MESSAGE)));
        handler.flush();
        logging.log(Level.FINE, SIMPLE_MESSAGE);
        assertTrue(handler.hasMessage(handler.level, logging.getDebugString(SIMPLE_MESSAGE)));
        handler.flush();
        logging.log(Level.FINER, SIMPLE_MESSAGE);
        assertTrue(handler.hasMessage(handler.level, logging.getDebugString(SIMPLE_MESSAGE)));
        handler.flush();
        logging.log(Level.FINEST, SIMPLE_MESSAGE);
        assertTrue(handler.hasMessage(handler.level, logging.getDebugString(SIMPLE_MESSAGE)));
        handler.flush();

        final Object o = new Object();
        handler.tester = new RecordTester() {
            @Override
            public void test(LogRecord record) {
                assertEquals(logging.getPrefixedMessage(String.format(ARGS_MESSAGE, "poop", 2, o)), record.getMessage());
            }
        };
        handler.level = Level.INFO;
        logging.log(Level.INFO, ARGS_MESSAGE, "poop", 2, o);

        handler.tester = new RecordTester() {
            @Override
            public void test(LogRecord record) {
                assertEquals(logging.getPrefixedMessage(ARGS_MESSAGE), record.getMessage());
            }
        };
        handler.level = Level.INFO;
        logging.log(Level.INFO, ARGS_MESSAGE, "poop", 2);
    }
}
