package com.cognizant.springlearn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * SpringLearnApplication — main entry point for Hands-on 1–6.
 *
 * ---------------------------------------------------------------
 * Exercise 1: @SpringBootApplication
 *   Convenience annotation combining:
 *     @Configuration          — marks class as bean source
 *     @EnableAutoConfiguration — Spring Boot auto-configures beans
 *     @ComponentScan          — scans com.cognizant.springlearn package
 *   SpringApplication.run() bootstraps the application, starts
 *   embedded Tomcat, and loads the ApplicationContext.
 * ---------------------------------------------------------------
 *
 * Exercise 3: Logging
 *   - LOGGER.info()  — general lifecycle messages (start/end)
 *   - LOGGER.debug() — detailed diagnostic messages (variable values)
 *   - Configured via application.properties
 *   - NEVER use System.out.println() — use logger instead
 */
@SpringBootApplication
public class SpringLearnApplication {

    /**
     * Exercise 3: Static Logger field.
     * LoggerFactory.getLogger() creates a logger named after this class.
     * The class name appears in the log pattern under %logger.
     */
    private static final Logger LOGGER =
            LoggerFactory.getLogger(SpringLearnApplication.class);

    // ---------------------------------------------------------------
    // Exercise 1: main()
    // ---------------------------------------------------------------

    /**
     * Spring Boot entry point.
     * SpringApplication.run() does:
     *   1. Creates Spring ApplicationContext.
     *   2. Auto-configures beans (DataSource, MVC, etc.) based on classpath.
     *   3. Starts embedded Tomcat on port defined in application.properties.
     *   4. Returns the ApplicationContext.
     */
    public static void main(String[] args) {
        LOGGER.info("SpringLearnApplication starting...");

        SpringApplication.run(SpringLearnApplication.class, args);

        LOGGER.info("SpringLearnApplication started successfully.");

        // Run all hands-on demonstrations
        displayDate();       // Hands-on 2 & 3
        displayCountry();    // Hands-on 4 & 5
        displayCountries();  // Hands-on 6
    }

    // ---------------------------------------------------------------
    // Exercise 2 + 3: Load SimpleDateFormat from Spring XML
    // ---------------------------------------------------------------

    /**
     * Hands-on 2: Load a SimpleDateFormat bean from date-format.xml
     *             and parse a date string.
     * Hands-on 3: All logging uses LOGGER — no System.out.println().
     *
     * Steps:
     *   1. Create ApplicationContext from classpath XML file.
     *   2. Get the "dateFormat" bean via getBean().
     *   3. Parse "31/12/2018" and log the result.
     *
     * How Spring loads the bean:
     *   ClassPathXmlApplicationContext reads date-format.xml.
     *   It finds <bean id="dateFormat" class="java.text.SimpleDateFormat">
     *   and calls: new SimpleDateFormat("dd/MM/yyyy").
     *   getBean() returns this singleton instance.
     */
    private static void displayDate() {
        LOGGER.info("START displayDate");

        // Step 1: Load Spring context from XML file on the classpath
        ApplicationContext context =
                new ClassPathXmlApplicationContext("date-format.xml");

        // Step 2: Retrieve the SimpleDateFormat bean
        //   getBean(id, Class) — type-safe retrieval by bean id
        SimpleDateFormat format =
                context.getBean("dateFormat", SimpleDateFormat.class);

        LOGGER.debug("SimpleDateFormat pattern: {}", format.toPattern());

        // Step 3: Parse "31/12/2018" using the format bean
        try {
            Date date = format.parse("31/12/2018");
            LOGGER.debug("Parsed date: {}", date);
            LOGGER.info("Date parsed successfully: {}", date);
        } catch (ParseException e) {
            LOGGER.error("Failed to parse date: {}", e.getMessage());
        }

        // Close context to release resources
        ((ClassPathXmlApplicationContext) context).close();

        LOGGER.info("END displayDate");
    }

    // ---------------------------------------------------------------
    // Exercise 4 + 5: Load Country from Spring XML
    // ---------------------------------------------------------------

    /**
     * Hands-on 4: Load a Country bean from country.xml via Setter Injection.
     * Hands-on 5: Demonstrates SINGLETON vs PROTOTYPE scope.
     *
     * SINGLETON (default, no scope attribute in XML):
     *   - Country constructor is called ONCE.
     *   - Both country and anotherCountry point to the SAME object.
     *   - country == anotherCountry → true
     *
     * PROTOTYPE (scope="prototype" in XML):
     *   - Country constructor is called TWICE (once per getBean()).
     *   - country and anotherCountry are DIFFERENT objects.
     *   - country == anotherCountry → false
     *
     * Observe the DEBUG logs from Country constructor/setters in output.
     */
    private static void displayCountry() {
        LOGGER.info("START displayCountry");

        // Load Spring context from country.xml
        ApplicationContext context =
                new ClassPathXmlApplicationContext("country.xml");

        // First getBean() call — Spring creates Country (constructor + setters called)
        Country country = context.getBean("country", Country.class);
        LOGGER.debug("Country : {}", country.toString());

        // Exercise 5: Second getBean() call
        // SINGLETON: same object returned, constructor NOT called again
        // PROTOTYPE: new object created, constructor called again
        Country anotherCountry = context.getBean("country", Country.class);
        LOGGER.debug("Another Country : {}", anotherCountry.toString());

        // Verify scope
        boolean isSameInstance = (country == anotherCountry);
        LOGGER.info("Same instance (singleton=true, prototype=false): {}", isSameInstance);

        ((ClassPathXmlApplicationContext) context).close();

        LOGGER.info("END displayCountry");
    }

    // ---------------------------------------------------------------
    // Exercise 6: Load list of Countries from Spring XML
    // ---------------------------------------------------------------

    /**
     * Hands-on 6: Load an ArrayList of Country beans from country.xml.
     *
     * country.xml defines:
     *   - Four individual Country beans: "in", "us", "de", "jp"
     *   - An ArrayList bean "countryList" referencing those four beans
     *     via <list><ref bean="..."/></list>
     *
     * <list>  — Spring's XML representation of java.util.List
     * <ref bean="beanId"> — resolves to the Country object with that id
     *
     * When Spring loads "countryList":
     *   1. It creates each Country bean (constructor + setters called).
     *   2. It creates an ArrayList and adds all four Country references.
     *   3. getBean("countryList") returns that populated ArrayList.
     */
    @SuppressWarnings("unchecked")
    private static void displayCountries() {
        LOGGER.info("START displayCountries");

        // Load Spring context — all beans in country.xml are instantiated
        ApplicationContext context =
                new ClassPathXmlApplicationContext("country.xml");

        // Retrieve the ArrayList bean
        // We suppress unchecked cast warning — we know it's List<Country>
        List<Country> countries =
                context.getBean("countryList", List.class);

        LOGGER.debug("Number of countries: {}", countries.size());

        // Iterate and log each country
        countries.forEach(c -> LOGGER.debug("Country: {}", c));

        ((ClassPathXmlApplicationContext) context).close();

        LOGGER.info("END displayCountries");
    }
}
