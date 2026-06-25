package strategy;

public class CreditCardPayment implements PaymentStrategy {

    private String cardNumber;
    private String cardHolder;

    public CreditCardPayment(String cardHolder, String cardNumber) {
        this.cardHolder = cardHolder;
        this.cardNumber = cardNumber;
    }

    @Override
    public void pay(double amount) {
        String maskedCard = "xxxx-xxxx-xxxx-" + cardNumber.substring(cardNumber.length() - 4);
        System.out.println("Charging $" + amount + " to credit card " + maskedCard + " (" + cardHolder + ")");
    }

    @Override
    public String getMethodName() {
        return "Credit Card";
    }
}
