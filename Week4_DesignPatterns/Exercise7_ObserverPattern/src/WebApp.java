package observer;

public class WebApp implements Observer {

    private String siteName;

    public WebApp(String siteName) {
        this.siteName = siteName;
    }

    @Override
    public void update(String stockName, double newPrice) {
        System.out.println("  [" + siteName + " - Web] Stock update: " + stockName + " = $" + newPrice);
    }
}
