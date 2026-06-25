package adapter;

public class StripeGateway {

    public void makeStripePayment(double amount) {
        System.out.println("Stripe: Processing payment of $" + amount);
    }
}
