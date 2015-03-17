package pluginbase.messages;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MessageTest {

    final String testKey = "this is my key";
    final String testMessage = "this is my default message: %s";

    @Before
    public void setUp() {
        // Tests the loading of theme.xml
        Messages.BLANK.getDefault();
    }

    @Test
    public void testCreateMessage() throws Exception {
        final Message message = Message.createMessage(testKey, testMessage);
        Assert.assertEquals(testKey, message.getKey());
        Assert.assertEquals(testMessage, message.getDefault());
    }

    @Test
    public void testBundleMessage() throws Exception {
        final Message message = Message.createMessage(testKey, testMessage);
        //Message.bundleMessage()
    }
}
