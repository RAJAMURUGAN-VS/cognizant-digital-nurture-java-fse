import org.junit.Test;
import static org.junit.Assert.*;

public class AssertionsTest {

    MathHelper helper = new MathHelper();

    @Test
    public void testAssertions() {
        assertEquals(5, 2 + 3);
        assertTrue(5 > 3);
        assertFalse(5 < 3);
        assertNull(null);
        assertNotNull(new Object());
    }

    @Test
    public void testMultiply() {
        assertEquals(12, helper.multiply(3, 4));
    }

    @Test
    public void testDivide_normalCase() {
        assertNotNull(helper.divide(10, 2));
        assertEquals(5.0, helper.divide(10, 2), 0.001);
    }

    @Test
    public void testDivide_byZero_returnsNull() {
        assertNull(helper.divide(10, 0));
    }

    @Test
    public void testIsEven_true() {
        assertTrue(helper.isEven(4));
    }

    @Test
    public void testIsEven_false() {
        assertFalse(helper.isEven(7));
    }

    @Test
    public void testArrayEquals() {
        int[] expected = {1, 2, 3};
        int[] actual = {1, 2, 3};
        assertArrayEquals(expected, actual);
    }
}
