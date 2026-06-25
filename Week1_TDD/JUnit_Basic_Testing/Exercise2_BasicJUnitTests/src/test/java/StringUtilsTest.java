import org.junit.Test;
import static org.junit.Assert.*;

public class StringUtilsTest {

    StringUtils utils = new StringUtils();

    @Test
    public void testReverse() {
        assertEquals("olleh", utils.reverse("hello"));
    }

    @Test
    public void testReverseNull() {
        assertNull(utils.reverse(null));
    }

    @Test
    public void testIsPalindrome_true() {
        assertTrue(utils.isPalindrome("racecar"));
    }

    @Test
    public void testIsPalindrome_false() {
        assertFalse(utils.isPalindrome("hello"));
    }

    @Test
    public void testCountVowels() {
        assertEquals(3, utils.countVowels("hello world"));
    }

    @Test
    public void testCountVowels_null() {
        assertEquals(0, utils.countVowels(null));
    }
}
