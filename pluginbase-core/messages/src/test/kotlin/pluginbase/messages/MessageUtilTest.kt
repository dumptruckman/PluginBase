package pluginbase.messages

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Locale

class MessageUtilTest {

    @Test
    fun formatMessage() {
        assertEquals("Test", formatMessage(Locale.getDefault(), "Test"))
        assertEquals("Test", formatMessage(Locale.getDefault(), "%s", "Test"))
    }
}