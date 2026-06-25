package decorator;

public class SMSNotifierDecorator extends NotifierDecorator {

    private String phoneNumber;

    public SMSNotifierDecorator(Notifier notifier, String phoneNumber) {
        super(notifier);
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void send(String message) {
        wrappedNotifier.send(message);
        System.out.println("SMS to " + phoneNumber + ": " + message);
    }
}
