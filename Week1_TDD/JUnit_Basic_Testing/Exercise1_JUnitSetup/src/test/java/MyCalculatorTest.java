import org.junit.Test;
import static org.junit.Assert.*;

public class MyCalculatorTest {

    @Test
    public void testAdd() {
        MyCalculator calc = new MyCalculator();
        int result = calc.add(3, 4);
        assertEquals(7, result);
    }

    @Test
    public void testSubtract() {
        MyCalculator calc = new MyCalculator();
        int result = calc.subtract(10, 4);
        assertEquals(6, result);
    }
}
