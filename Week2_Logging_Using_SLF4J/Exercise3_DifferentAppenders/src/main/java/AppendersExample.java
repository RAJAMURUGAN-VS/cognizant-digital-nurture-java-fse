import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exercise 3: Using Different Appenders
 *
 * Demonstrates SLF4J + Logback with three appenders configured in logback.xml:
 *
 *   1. CONSOLE     - Prints all log events (DEBUG+) to the terminal.
 *   2. FILE        - Writes all log events (DEBUG+) to logs/app.log with daily rolling.
 *   3. ERROR_FILE  - Writes only ERROR events to logs/error.log (filtered via ThresholdFilter).
 *
 * After running this class, check:
 *   - Terminal output        → from CONSOLE appender
 *   - logs/app.log           → from FILE appender (all levels)
 *   - logs/error.log         → from ERROR_FILE appender (errors only)
 */
public class AppendersExample {

    private static final Logger logger = LoggerFactory.getLogger(AppendersExample.class);

    public static void main(String[] args) {
        logger.info("=== Appenders Demo Started ===");

        // TRACE - very fine-grained diagnostic info (goes to CONSOLE + FILE)
        logger.trace("TRACE: Entering main method");

        // DEBUG - diagnostic info useful during development (goes to CONSOLE + FILE)
        logger.debug("DEBUG: Loading application configuration");
        logger.debug("DEBUG: Config property 'server.port' = {}", 8080);

        // INFO - normal application events (goes to CONSOLE + FILE)
        logger.info("INFO: Server started successfully on port {}", 8080);
        logger.info("INFO: Connected to database '{}'", "orders_db");

        // WARN - recoverable unexpected situations (goes to CONSOLE + FILE)
        logger.warn("WARN: Response time {}ms exceeds threshold of {}ms", 3200, 2000);
        logger.warn("WARN: Retrying failed request, attempt {}/{}", 2, 3);

        // ERROR - serious failures (goes to CONSOLE + FILE + ERROR_FILE)
        logger.error("ERROR: Payment service is unreachable");
        logger.error("ERROR: Failed to process order #{} for user '{}'", 5042, "bob");

        try {
            simulateCriticalFailure();
        } catch (Exception e) {
            // Logs exception stack trace — goes to all three appenders (but only
            // stack trace appears in error.log due to the ThresholdFilter)
            logger.error("ERROR: Critical failure in simulateCriticalFailure()", e);
        }

        logger.info("=== Appenders Demo Finished ===");
        logger.info("Check logs/app.log for all events and logs/error.log for errors only");
    }

    /**
     * Simulates a critical failure for demonstration purposes.
     */
    private static void simulateCriticalFailure() throws Exception {
        throw new RuntimeException("Null pointer encountered in order processing pipeline");
    }
}
