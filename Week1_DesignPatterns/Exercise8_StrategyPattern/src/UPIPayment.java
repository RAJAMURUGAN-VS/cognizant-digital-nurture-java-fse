package strategy;

public class UPIPayment implements PaymentStrategy {

    private String upiId;

    public UPIPayment(String upiId) {
        this.upiId = upiId;
    }

    @Override
    public void pay(double amount) {
        System.out.println("UPI transfer of $" + amount + " from " + upiId);
    }

    @Override
    public String getMethodName() {
        return "UPI";
    }
}
