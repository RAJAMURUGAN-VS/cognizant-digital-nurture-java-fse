import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exercise 1: Logging Error Messages and Warning Levels
 *
 * Demonstrates how to log ERROR and WARN level messages using SLF4J with
 * Logback as the underlying implementation.
 *
 * Log levels (lowest to highest severity):
 *   TRACE < DEBUG < INFO < WARN < ERROR
 */
public class LoggingExample {

    private static final Logger logger = LoggerFactory.getLogger(LoggingExample.class);

    public static void main(String[] args) {
        logger.info("Application started - demonstrating ERROR and WARN log levels");

        // --- ERROR level: use for serious failures that need immediate attention ---
        logger.error("This is an error message");
        logger.error("Database connection failed - unable to reach host '{}'", "db.example.com");

        try {
            int result = divide(10, 0);
        } catch (ArithmeticException e) {
            // Logging an exception with a message
            logger.error("An arithmetic exception occurred during division", e);
        }

        // --- WARN level: use for unexpected situations that are recoverable ---
        logger.warn("This is a warning message");
        logger.warn("Disk usage is at {}% - consider freeing up space", 85);
        logger.warn("Deprecated API method 'legacyProcess()' called - please upgrade");

        logger.info("Application finished");
    }

    /**
     * Helper method to trigger an ArithmeticException for demonstration.
     */
    private static int divide(int a, int b) {
        return a / b;
    }
}
