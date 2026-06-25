package decorator;

public class EmailNotifier implements Notifier {

    private String emailAddress;

    public EmailNotifier(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public void send(String message) {
        System.out.println("Email to " + emailAddress + ": " + message);
    }
}
