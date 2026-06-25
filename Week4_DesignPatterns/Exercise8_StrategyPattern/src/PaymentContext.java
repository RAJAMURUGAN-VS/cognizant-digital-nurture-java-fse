package strategy;

public class PaymentContext {

    private PaymentStrategy strategy;

    public PaymentContext(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public void executePayment(double amount) {
        System.out.println("Payment method: " + strategy.getMethodName());
        strategy.pay(amount);
        System.out.println("Payment done!");
    }
}
