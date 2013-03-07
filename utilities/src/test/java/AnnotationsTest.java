import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
public class AnnotationsTest {

    @Test
    public void testIntellijAnnotations() throws Exception {
        boolean thrown = false;
        try {
            testNullParam(null);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    public void testNullParam(@NotNull final Object object) { }
}
