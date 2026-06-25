package strategy;

public class StrategyTest {

    public static void main(String[] args) {

        System.out.println("-- Strategy Pattern Test --\n");

        PaymentContext context;

        System.out.println("Order 1 - Customer chose Credit Card:");
        context = new PaymentContext(new CreditCardPayment("Raj Kumar", "4111111111111234"));
        context.executePayment(1299.99);

        System.out.println();

        System.out.println("Order 2 - Customer chose PayPal:");
        context = new PaymentContext(new PayPalPayment("raj.kumar@gmail.com"));
        context.executePayment(499.00);

        System.out.println();

        System.out.println("Order 3 - Customer chose UPI:");
        context = new PaymentContext(new UPIPayment("raj@okaxis"));
        context.executePayment(799.50);

        System.out.println();

        System.out.println("Order 4 - Switched to Credit Card at checkout:");
        context.setStrategy(new CreditCardPayment("Priya S", "5500000000005678"));
        context.executePayment(250.00);
    }
}
