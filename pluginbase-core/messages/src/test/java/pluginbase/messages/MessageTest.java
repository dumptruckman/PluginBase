package pluginbase.messages;

import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.cglib.core.Local;
import pluginbase.logging.Logging;
import pluginbase.logging.PluginLogger;

import java.io.File;
import java.util.Arrays;
import java.util.Locale;

public class MessageTest {

    final String testKey = "this is my key";
    final String testMessage = "this is my default message: %s";

    enum TestEnumMessages implements Message, LocalizablePlugin {
        IN_GAME_ONLY("commands.in_game_only", "To use from console, use \"-p <name>\" to specify a player"),
        INVALID_AMOUNT("commands.invalid_amount", "'%s' is not a valid amount!"),
        ;

        private final Message message;

        TestEnumMessages(@NotNull String key, @NotNull String defaultMessage, String... additionalLines) {
            this.message = Message.createMessage(key, defaultMessage, additionalLines);
        }

        @Override
        @NotNull
        public String getDefault() {
            return message.getDefault();
        }

        @Override
        @Nullable
        public Object[] getKey() {
            return message.getKey();
        }

        @Override
        public int getArgCount() {
            return message.getArgCount();
        }

        @NotNull
        @Override
        public PluginLogger getLog() {
            return Logging.getLogger();
        }
    }

    @Before
    public void setUp() {
        // Tests the loading of theme.xml
        Messages.BLANK.getDefault();
    }

    @Test
    public void testEnumMessages() throws Exception {
        Messages.registerMessages(TestEnumMessages.IN_GAME_ONLY, TestEnumMessages.class);
        Assert.assertNotNull(Messages.getMessage(TestEnumMessages.IN_GAME_ONLY, TestEnumMessages.INVALID_AMOUNT.getKey()));
        MessageProvider provider = Messages.loadMessages(TestEnumMessages.IN_GAME_ONLY,
                HoconConfigurationLoader.builder().setFile(File.createTempFile("pluginbase", "test")).build(),
                Locale.ENGLISH);
        Assert.assertEquals(TestEnumMessages.IN_GAME_ONLY.getDefault(), provider.getLocalizedMessage(TestEnumMessages.IN_GAME_ONLY));
    }

    @Test
    public void testCreateMessage() throws Exception {
        final Message message = Message.createMessage(testKey, testMessage);
        Assert.assertEquals(Arrays.toString(new Object[] {testKey}), Arrays.toString(message.getKey()));
        Assert.assertEquals(testMessage, message.getDefault());
    }

    @Test
    public void testBundleMessage() throws Exception {
        final Message message = Message.createMessage(testKey, testMessage);
        //Message.bundleMessage()
    }
}
