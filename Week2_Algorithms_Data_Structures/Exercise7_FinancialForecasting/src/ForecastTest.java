package forecast;

public class ForecastTest {

    public static void main(String[] args) {

        double principal = 10000.0;
        double rate = 0.08;

        System.out.println("-- Financial Forecasting (Recursive) --\n");
        System.out.printf("Principal: %.2f, Growth Rate: %.0f%%\n\n", principal, rate * 100);

        for (int year = 1; year <= 10; year++) {
            double futureVal = FinancialForecast.calculateFutureValue(principal, rate, year);
            System.out.printf("Year %2d: %.2f\n", year, futureVal);
        }

        System.out.println("\n-- With Memoization (Year 20) --");
        double[] memo = new double[21];
        double val = FinancialForecast.calculateFutureValueMemo(principal, rate, 20, memo);
        System.out.printf("Value after 20 years: %.2f\n", val);
    }
}
