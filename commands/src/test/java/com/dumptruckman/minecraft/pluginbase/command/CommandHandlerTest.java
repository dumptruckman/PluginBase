package com.dumptruckman.minecraft.pluginbase.command;

import com.sk89q.bukkit.util.CommandInfo;
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
                newKey("pb", true);
                getKey("pb").newKey("reload", true);
            }
            @Override
            protected boolean register(CommandInfo command) {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
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
