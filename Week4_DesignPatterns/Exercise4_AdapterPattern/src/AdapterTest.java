package adapter;

public class AdapterTest {

    public static void main(String[] args) {

        System.out.println("-- Adapter Pattern Test --\n");

        PaymentProcessor stripe = new StripeAdapter(new StripeGateway());
        System.out.println("Paying with Stripe:");
        stripe.processPayment(250.00);

        System.out.println();

        PaymentProcessor paypal = new PayPalAdapter(new PayPalGateway());
        System.out.println("Paying with PayPal:");
        paypal.processPayment(99.99);
    }
}
