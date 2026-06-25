package singleton;

public class LoggerTest {

    public static void main(String[] args) {

        System.out.println("-- Singleton Pattern Test --");

        Logger log1 = Logger.getInstance();
        Logger log2 = Logger.getInstance();

        log1.log("Application started");
        log2.log("User logged in");
        log1.log("Loading config file");

        System.out.println("\nAre both instances the same? " + (log1 == log2));
        System.out.println("Total logs written: " + log2.getLogCount());
    }
}
