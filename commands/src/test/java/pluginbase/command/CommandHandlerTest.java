package pluginbase.command;

import junit.framework.TestCase;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

public class CommandHandlerTest extends TestCase {

    @Test
    public void testCommandDetection() throws Exception {
        TestPlugin plugin = PowerMockito.mock(TestPlugin.class);
        CommandHandler ch = Mockito.spy(new CommandHandler(plugin) {
            {
                configureCommandKeys("pb");
                configureCommandKeys("pb reload");
            }
            @Override
            protected boolean register(CommandRegistration commandInfo, Command cmd) {
                return true;
            }
        });
        String[] res = ch.commandDetection(new String[] {"pb"});
        assertEquals(res.length, 1);
        assertEquals(res[0], "pb");

        res = ch.commandDetection(new String[] {"pb", "reload"});
        assertEquals(res.length, 1);
        assertEquals(res[0], "pb reload");

        res = ch.commandDetection(new String[] {"pb", "reload", "poop"});
        assertEquals(res.length, 2);
        assertEquals(res[0], "pb reload");
        assertEquals(res[1], "poop");
    }
}
