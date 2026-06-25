package decorator;

public class SlackNotifierDecorator extends NotifierDecorator {

    private String slackChannel;

    public SlackNotifierDecorator(Notifier notifier, String slackChannel) {
        super(notifier);
        this.slackChannel = slackChannel;
    }

    @Override
    public void send(String message) {
        wrappedNotifier.send(message);
        System.out.println("Slack [" + slackChannel + "]: " + message);
    }
}
