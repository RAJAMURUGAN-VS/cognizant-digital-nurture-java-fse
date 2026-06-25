package strategy;

public class PayPalPayment implements PaymentStrategy {

    private String email;

    public PayPalPayment(String email) {
        this.email = email;
    }

    @Override
    public void pay(double amount) {
        System.out.println("Sending $" + amount + " via PayPal to/from " + email);
    }

    @Override
    public String getMethodName() {
        return "PayPal";
    }
}
