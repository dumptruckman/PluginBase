package pluginbase.messages

import ninja.leaping.configurate.hocon.HoconConfigurationLoader
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import pluginbase.logging.Logging
import pluginbase.logging.PluginLogger

import java.io.File
import java.util.Arrays
import java.util.Locale

class MessageTest {

    val testKey = "this is my key"
    val testMessage = "this is my default message: %s"

    internal enum class TestEnumMessages constructor(key: String, defaultMessage: String,
                                                     vararg additionalLines: String) : LocalizablePlugin, Message {
        IN_GAME_ONLY("commands.in_game_only", "To use from console, use \"-p <name>\" to specify a player"),
        INVALID_AMOUNT("commands.invalid_amount", "'%s' is not a valid amount!");

        val message: Message = Messages.createMessage(key, defaultMessage, *additionalLines)

        override fun getLog(): PluginLogger {
            return Logging.getLogger()
        }

        override val default: String
            get() = message.default
        override val key: Array<*>?
            get() = message.key
        override val argCount: Int
            get() = message.argCount
    }

    @Before
    fun setUp() {
        // Tests the loading of theme.xml
        Messages.BLANK.default
    }

    @Test
    @Throws(Exception::class)
    fun testEnumMessages() {
        Messages.registerMessages(TestEnumMessages.IN_GAME_ONLY, TestEnumMessages::class.java)
        Assert.assertNotNull(Messages.getMessage(TestEnumMessages.IN_GAME_ONLY, TestEnumMessages.INVALID_AMOUNT.message.key))
        val provider = Messages.loadMessages(TestEnumMessages.IN_GAME_ONLY,
                HoconConfigurationLoader.builder().setFile(File.createTempFile("pluginbase", "test")).build(),
                Locale.ENGLISH)
        Assert.assertEquals(TestEnumMessages.IN_GAME_ONLY.message.default, provider.getLocalizedMessage(TestEnumMessages.IN_GAME_ONLY.message))
    }

    @Test
    @Throws(Exception::class)
    fun testCreateMessage() {
        val message = Messages.createMessage(testKey, testMessage)
        Assert.assertEquals(Arrays.toString(arrayOf<Any>(testKey)), Arrays.toString(message.key))
        Assert.assertEquals(testMessage, message.default)
    }

    @Test
    @Throws(Exception::class)
    fun testBundleMessage() {
        val message = Messages.createMessage(testKey, testMessage)
        //Message.bundleMessage()
    }
}
