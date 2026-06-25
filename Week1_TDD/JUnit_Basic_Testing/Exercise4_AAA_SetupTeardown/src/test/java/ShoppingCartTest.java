import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ShoppingCartTest {

    private ShoppingCart cart;

    @Before
    public void setUp() {
        cart = new ShoppingCart();
        System.out.println("Cart ready for test.");
    }

    @After
    public void tearDown() {
        cart.clear();
        System.out.println("Cart cleared after test.");
    }

    @Test
    public void testAddItem() {
        // Arrange
        double price = 299.99;

        // Act
        cart.addItem(price);

        // Assert
        assertEquals(1, cart.getItemCount());
        assertEquals(299.99, cart.getTotal(), 0.001);
    }

    @Test
    public void testAddMultipleItems() {
        // Arrange
        double item1 = 100.0;
        double item2 = 250.0;
        double item3 = 50.0;

        // Act
        cart.addItem(item1);
        cart.addItem(item2);
        cart.addItem(item3);

        // Assert
        assertEquals(3, cart.getItemCount());
        assertEquals(400.0, cart.getTotal(), 0.001);
    }

    @Test
    public void testRemoveItem() {
        // Arrange
        cart.addItem(500.0);
        cart.addItem(200.0);

        // Act
        cart.removeItem(200.0);

        // Assert
        assertEquals(1, cart.getItemCount());
        assertEquals(500.0, cart.getTotal(), 0.001);
    }

    @Test
    public void testEmptyCartTotal() {
        // Arrange - cart is already empty from setUp

        // Act
        double total = cart.getTotal();

        // Assert
        assertEquals(0.0, total, 0.001);
    }

    @Test
    public void testClearCart() {
        // Arrange
        cart.addItem(100.0);
        cart.addItem(200.0);

        // Act
        cart.clear();

        // Assert
        assertEquals(0, cart.getItemCount());
    }
}
