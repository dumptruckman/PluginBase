package pluginbase.command;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommandTreeTest {

    CommandTree commandTree;

    @Before
    public void setUp() throws Exception {
        commandTree = new CommandTree();
        commandTree.registerKeysForAlias("test");
        commandTree.registerKeysForAlias("two args");
        commandTree.registerKeysForAlias("parent");
        commandTree.registerKeysForAlias("parent child");
    }

    @Test
    public void removeInitialArgTest() throws Exception {
        assertArrayEquals(new String[]{"one"}, commandTree.removeInitialArg(new String[]{"test", "one"}));
    }

    @Test
    public void test1ArgCommandWith1Arg() throws Exception {
        String[] args = commandTree.joinArgsForKnownCommands(new String[] {"test"});
        assertArrayEquals(new String[] {"test"}, args);
    }

    @Test
    public void test1ArgCommandWith2Args() throws Exception {
        String[] args = commandTree.joinArgsForKnownCommands(new String[] {"test", "one"});
        assertArrayEquals(new String[] {"test", "one"}, args);
    }

    @Test
    public void test1ArgCommandWith3Args() throws Exception {
        String[] args = commandTree.joinArgsForKnownCommands(new String[] {"test", "one", "two"});
        assertArrayEquals(new String[] {"test", "one", "two"}, args);
    }

    @Test
    public void test2ArgCommandWith2Args() throws Exception {
        String[] args = commandTree.joinArgsForKnownCommands(new String[] {"two", "args"});
        assertArrayEquals(new String[] {"two args"}, args);
    }

    @Test
    public void test2ArgCommandWith3Args() throws Exception {
        String[] args = commandTree.joinArgsForKnownCommands(new String[] {"two", "args", "one"});
        assertArrayEquals(new String[] {"two args", "one"}, args);
    }

    @Test
    public void test2ArgCommandWith4Args() throws Exception {
        String[] args = commandTree.joinArgsForKnownCommands(new String[] {"two", "args", "one", "two"});
        assertArrayEquals(new String[] {"two args", "one", "two"}, args);
    }

    @Test
    public void test1ArgParentCommandWith1Args() throws Exception {
        String[] args = commandTree.joinArgsForKnownCommands(new String[] {"parent"});
        assertArrayEquals(new String[] {"parent"}, args);
    }

    @Test
    public void test1ArgParentCommandWith2Args() throws Exception {
        String[] args = commandTree.joinArgsForKnownCommands(new String[] {"parent", "one"});
        assertArrayEquals(new String[] {"parent", "one"}, args);
    }

    @Test
    public void test1ArgParentCommandWith3Args() throws Exception {
        String[] args = commandTree.joinArgsForKnownCommands(new String[] {"parent", "one", "two"});
        assertArrayEquals(new String[] {"parent", "one", "two"}, args);
    }

    @Test
    public void test2ArgChildCommandWith2Args() throws Exception {
        String[] args = commandTree.joinArgsForKnownCommands(new String[] {"parent", "child"});
        assertArrayEquals(new String[] {"parent child"}, args);
    }

    @Test
    public void test2ArgChildCommandWith3Args() throws Exception {
        String[] args = commandTree.joinArgsForKnownCommands(new String[] {"parent", "child", "one"});
        assertArrayEquals(new String[] {"parent child", "one"}, args);
    }

    @Test
    public void test2ArgChildCommandWith4Args() throws Exception {
        String[] args = commandTree.joinArgsForKnownCommands(new String[] {"parent", "child", "one", "two"});
        assertArrayEquals(new String[] {"parent child", "one", "two"}, args);
    }
}
