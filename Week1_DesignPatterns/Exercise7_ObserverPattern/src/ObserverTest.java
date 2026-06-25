package observer;

public class ObserverTest {

    public static void main(String[] args) {

        System.out.println("-- Observer Pattern Test --\n");

        StockMarket appleStock = new StockMarket("AAPL", 150.00);

        MobileApp myStocksApp = new MobileApp("MyStocks");
        WebApp stockDashboard = new WebApp("StockDashboard");
        MobileApp robinhoodApp = new MobileApp("Robinhood");

        appleStock.registerObserver(myStocksApp);
        appleStock.registerObserver(stockDashboard);
        appleStock.registerObserver(robinhoodApp);

        appleStock.setStockPrice(155.75);
        appleStock.setStockPrice(148.20);

        System.out.println();

        appleStock.removeObserver(robinhoodApp);
        appleStock.setStockPrice(160.00);
    }
}
