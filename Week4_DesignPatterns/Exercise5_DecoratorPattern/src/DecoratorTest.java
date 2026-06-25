package decorator;

public class DecoratorTest {

    public static void main(String[] args) {

        System.out.println("-- Decorator Pattern Test --\n");

        Notifier emailOnly = new EmailNotifier("user@example.com");
        System.out.println("Email only:");
        emailOnly.send("Server is down!");

        System.out.println();

        Notifier emailAndSms = new SMSNotifierDecorator(
                new EmailNotifier("user@example.com"),
                "+91-9876543210"
        );
        System.out.println("Email + SMS:");
        emailAndSms.send("Disk usage above 90%");

        System.out.println();

        Notifier allChannels = new SlackNotifierDecorator(
                new SMSNotifierDecorator(
                        new EmailNotifier("user@example.com"),
                        "+91-9876543210"
                ),
                "#alerts"
        );
        System.out.println("Email + SMS + Slack:");
        allChannels.send("Database connection lost");
    }
}
