package adapter;

public class PayPalGateway {

    public void sendPayPalPayment(double amount) {
        System.out.println("PayPal: Sending payment of $" + amount);
    }
}
