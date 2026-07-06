package com.cognizant.ormlearn;

import com.cognizant.ormlearn.model.Country;
import com.cognizant.ormlearn.service.CountryService;
import com.cognizant.ormlearn.service.exception.CountryNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * OrmLearnApplication — Spring Boot entry point.
 *
 * This class wires together all hands-on exercises:
 *
 *   Hands-on 1 : testGetAllCountries()    — fetch all countries
 *   Hands-on 6 : testFindCountryByCode()  — fetch country by code
 *   Hands-on 7 : testAddCountry()         — add a new country
 *   Hands-on 8 : testUpdateCountry()      — update country name
 *   Hands-on 9 : testDeleteCountry()      — delete a country
 *   Hands-on 5 : testSearchCountries()    — search by partial name
 *
 * Run this class, then observe the console log output.
 * SQL statements are logged at TRACE level by Hibernate.
 *
 * @SpringBootApplication combines:
 *   @Configuration          — bean source
 *   @EnableAutoConfiguration — Spring Boot auto-config
 *   @ComponentScan          — scans com.cognizant.ormlearn package tree
 */
@SpringBootApplication
public class OrmLearnApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrmLearnApplication.class);

    // Static reference to CountryService, set from Spring context
    private static CountryService countryService;

    public static void main(String[] args) {
        // Load Spring ApplicationContext
        ApplicationContext context = SpringApplication.run(OrmLearnApplication.class, args);
        LOGGER.info("Inside main");

        // Retrieve CountryService bean from context
        countryService = context.getBean(CountryService.class);

        // ---------------------------------------------------------------
        // Run all hands-on test methods in sequence
        // ---------------------------------------------------------------
        testGetAllCountries();       // Hands-on 1
        testFindCountryByCode();     // Hands-on 6
        testAddCountry();            // Hands-on 7
        testUpdateCountry();         // Hands-on 8
        testDeleteCountry();         // Hands-on 9
        testSearchCountries();       // Hands-on 5
    }

    // ---------------------------------------------------------------
    // Hands-on 1: Get all countries
    // ---------------------------------------------------------------

    /**
     * Fetches and logs all countries from the database.
     */
    private static void testGetAllCountries() {
        LOGGER.info("Start testGetAllCountries");
        List<Country> countries = countryService.getAllCountries();
        LOGGER.debug("countries={}", countries);
        LOGGER.info("Total countries fetched: {}", countries.size());
        LOGGER.info("End testGetAllCountries");
    }

    // ---------------------------------------------------------------
    // Hands-on 6: Find country by code
    // ---------------------------------------------------------------

    /**
     * Fetches India (code='IN') and logs the result.
     * Also demonstrates the CountryNotFoundException for an invalid code.
     */
    private static void testFindCountryByCode() {
        LOGGER.info("Start testFindCountryByCode");
        try {
            Country country = countryService.findCountryByCode("IN");
            LOGGER.debug("Country: {}", country);
            // Verify: country name should be "India"
            LOGGER.info("Country name matches: {}", "India".equals(country.getName()));
        } catch (CountryNotFoundException e) {
            LOGGER.error("Country not found: {}", e.getMessage());
        }

        // Test with invalid code — should throw CountryNotFoundException
        try {
            countryService.findCountryByCode("XX");
        } catch (CountryNotFoundException e) {
            LOGGER.warn("Expected exception for invalid code: {}", e.getMessage());
        }

        LOGGER.info("End testFindCountryByCode");
    }

    // ---------------------------------------------------------------
    // Hands-on 7: Add a new country
    // ---------------------------------------------------------------

    /**
     * Adds a test country 'ZX' = 'Test Country', then verifies it was saved.
     */
    private static void testAddCountry() {
        LOGGER.info("Start testAddCountry");

        // Create a new Country instance with a unique code
        Country newCountry = new Country("ZX", "Test Country");

        // Add to the database via service
        countryService.addCountry(newCountry);
        LOGGER.info("Country added: {}", newCountry);

        // Verify it was saved by fetching it back
        try {
            Country fetched = countryService.findCountryByCode("ZX");
            LOGGER.debug("Fetched after add: {}", fetched);
            LOGGER.info("Add verified: {}", "Test Country".equals(fetched.getName()));
        } catch (CountryNotFoundException e) {
            LOGGER.error("Add verification failed: {}", e.getMessage());
        }

        LOGGER.info("End testAddCountry");
    }

    // ---------------------------------------------------------------
    // Hands-on 8: Update a country
    // ---------------------------------------------------------------

    /**
     * Updates the name of 'ZX' from 'Test Country' to 'Updated Country',
     * then verifies the change in the database.
     */
    private static void testUpdateCountry() {
        LOGGER.info("Start testUpdateCountry");

        try {
            // Update the name of country 'ZX'
            countryService.updateCountry("ZX", "Updated Country");
            LOGGER.info("Country ZX updated");

            // Verify the update
            Country updated = countryService.findCountryByCode("ZX");
            LOGGER.debug("After update: {}", updated);
            LOGGER.info("Update verified: {}", "Updated Country".equals(updated.getName()));
        } catch (CountryNotFoundException e) {
            LOGGER.error("Update failed: {}", e.getMessage());
        }

        LOGGER.info("End testUpdateCountry");
    }

    // ---------------------------------------------------------------
    // Hands-on 9: Delete a country
    // ---------------------------------------------------------------

    /**
     * Deletes country 'ZX' (added in testAddCountry) and verifies deletion.
     */
    private static void testDeleteCountry() {
        LOGGER.info("Start testDeleteCountry");

        // Delete the country added in Hands-on 7
        countryService.deleteCountry("ZX");
        LOGGER.info("Country ZX deleted");

        // Verify deletion — should throw CountryNotFoundException
        try {
            countryService.findCountryByCode("ZX");
            LOGGER.error("ERROR: Country ZX still exists after deletion!");
        } catch (CountryNotFoundException e) {
            LOGGER.info("Delete verified: country ZX no longer exists");
        }

        LOGGER.info("End testDeleteCountry");
    }

    // ---------------------------------------------------------------
    // Hands-on 5: Search countries by partial name
    // ---------------------------------------------------------------

    /**
     * Searches for countries whose name contains "ind" (case-insensitive).
     * Expected results include: India, Indonesia.
     */
    private static void testSearchCountries() {
        LOGGER.info("Start testSearchCountries");

        String keyword = "ind";
        List<Country> results = countryService.searchCountriesByName(keyword);
        LOGGER.info("Countries matching '{}': {}", keyword, results.size());
        results.forEach(c -> LOGGER.debug("  Match: {}", c));

        // Another search — countries containing "united"
        String keyword2 = "united";
        List<Country> results2 = countryService.searchCountriesByName(keyword2);
        LOGGER.info("Countries matching '{}': {}", keyword2, results2.size());
        results2.forEach(c -> LOGGER.debug("  Match: {}", c));

        LOGGER.info("End testSearchCountries");
    }
}
