import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exercise 2: Parameterized Logging
 *
 * Demonstrates SLF4J's parameterized (placeholder-based) logging using {}.
 *
 * WHY use parameterized logging instead of string concatenation?
 *   - Performance: The message string is only built if that log level is enabled.
 *     With concatenation (e.g., "Hello " + name), the string is always built
 *     even when the log level is disabled — wasting CPU and memory.
 *   - Cleaner code: {} placeholders are readable and concise.
 *
 * BAD  (avoid): logger.debug("User " + userId + " logged in from " + ip);
 * GOOD (use):   logger.debug("User {} logged in from {}", userId, ip);
 */
public class ParameterizedLoggingExample {

    private static final Logger logger = LoggerFactory.getLogger(ParameterizedLoggingExample.class);

    public static void main(String[] args) {
        logger.info("=== Parameterized Logging Demo ===");

        // --- Single parameter ---
        String username = "alice";
        logger.info("User '{}' has logged in", username);

        // --- Two parameters ---
        String action = "DELETE";
        String resource = "/api/orders/42";
        logger.warn("Unauthorized action '{}' attempted on resource '{}'", action, resource);

        // --- Three or more parameters (use varargs) ---
        int orderId = 1001;
        String status = "SHIPPED";
        String destination = "New York";
        logger.info("Order #{} status changed to '{}', destination: {}", orderId, status, destination);

        // --- Numeric parameters ---
        double cpuUsage = 92.5;
        int threadCount = 200;
        logger.warn("High load detected - CPU: {}%, active threads: {}", cpuUsage, threadCount);

        // --- Error level with parameterized message + exception ---
        String fileName = "report_2024.csv";
        try {
            processFile(fileName);
        } catch (Exception e) {
            logger.error("Failed to process file '{}': {}", fileName, e.getMessage(), e);
        }

        // --- DEBUG: only printed if log level is DEBUG or lower ---
        int page = 3;
        int pageSize = 25;
        logger.debug("Fetching records - page: {}, pageSize: {}", page, pageSize);

        // --- TRACE: fine-grained diagnostic info ---
        logger.trace("Entering main() method with {} args", args.length);

        logger.info("=== Demo complete ===");
    }

    /**
     * Simulates a file processing operation that throws an exception.
     */
    private static void processFile(String fileName) throws Exception {
        throw new Exception("File not found on disk");
    }
}
