package com.cognizant.ormlearn;

import com.cognizant.ormlearn.model.Country;
import com.cognizant.ormlearn.service.CountryService;
import com.cognizant.ormlearn.service.exception.CountryNotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * OrmLearnApplicationTests — integration tests for CountryService.
 *
 * @SpringBootTest loads the full Spring ApplicationContext.
 * Tests run against the configured MySQL database (ormlearn schema).
 * Ensure MySQL is running and country table is populated before running.
 *
 * Run with: mvn test
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrmLearnApplicationTests {

    @Autowired
    private CountryService countryService;

    // Test country code used across add/update/delete tests
    private static final String TEST_CODE = "ZZ";
    private static final String TEST_NAME = "Test Nation";
    private static final String UPDATED_NAME = "Updated Nation";

    // ---------------------------------------------------------------
    // Hands-on 1: Get all countries
    // ---------------------------------------------------------------

    @Test
    @Order(1)
    @DisplayName("Hands-on 1: getAllCountries returns non-empty list")
    void getAllCountries_returnsNonEmptyList() {
        List<Country> countries = countryService.getAllCountries();
        assertNotNull(countries, "Countries list must not be null");
        assertTrue(countries.size() > 0, "Countries list must not be empty");
    }

    // ---------------------------------------------------------------
    // Hands-on 6: Find by code
    // ---------------------------------------------------------------

    @Test
    @Order(2)
    @DisplayName("Hands-on 6: findCountryByCode('IN') returns India")
    void findCountryByCode_validCode_returnsCountry() throws CountryNotFoundException {
        Country country = countryService.findCountryByCode("IN");
        assertNotNull(country);
        assertEquals("IN", country.getCode());
        assertEquals("India", country.getName());
    }

    @Test
    @Order(3)
    @DisplayName("Hands-on 6: findCountryByCode with invalid code throws CountryNotFoundException")
    void findCountryByCode_invalidCode_throwsException() {
        assertThrows(CountryNotFoundException.class,
                () -> countryService.findCountryByCode("XX"),
                "Should throw CountryNotFoundException for unknown code");
    }

    // ---------------------------------------------------------------
    // Hands-on 7: Add country
    // ---------------------------------------------------------------

    @Test
    @Order(4)
    @DisplayName("Hands-on 7: addCountry persists new country")
    void addCountry_newCountry_persistedSuccessfully() throws CountryNotFoundException {
        // Clean up in case previous test run left this entry
        try { countryService.deleteCountry(TEST_CODE); } catch (Exception ignored) {}

        Country newCountry = new Country(TEST_CODE, TEST_NAME);
        countryService.addCountry(newCountry);

        Country fetched = countryService.findCountryByCode(TEST_CODE);
        assertNotNull(fetched);
        assertEquals(TEST_CODE, fetched.getCode());
        assertEquals(TEST_NAME, fetched.getName());
    }

    // ---------------------------------------------------------------
    // Hands-on 8: Update country
    // ---------------------------------------------------------------

    @Test
    @Order(5)
    @DisplayName("Hands-on 8: updateCountry changes country name")
    void updateCountry_existingCode_updatesName() throws CountryNotFoundException {
        countryService.updateCountry(TEST_CODE, UPDATED_NAME);

        Country updated = countryService.findCountryByCode(TEST_CODE);
        assertEquals(UPDATED_NAME, updated.getName(),
                "Country name should be updated to: " + UPDATED_NAME);
    }

    @Test
    @Order(6)
    @DisplayName("Hands-on 8: updateCountry with invalid code throws CountryNotFoundException")
    void updateCountry_invalidCode_throwsException() {
        assertThrows(CountryNotFoundException.class,
                () -> countryService.updateCountry("XX", "Some Name"));
    }

    // ---------------------------------------------------------------
    // Hands-on 9: Delete country
    // ---------------------------------------------------------------

    @Test
    @Order(7)
    @DisplayName("Hands-on 9: deleteCountry removes country from database")
    void deleteCountry_existingCode_removesEntry() {
        countryService.deleteCountry(TEST_CODE);

        assertThrows(CountryNotFoundException.class,
                () -> countryService.findCountryByCode(TEST_CODE),
                "Country should no longer exist after deletion");
    }

    // ---------------------------------------------------------------
    // Hands-on 5: Search by partial name
    // ---------------------------------------------------------------

    @Test
    @Order(8)
    @DisplayName("Hands-on 5: searchCountriesByName('ind') returns India and Indonesia")
    void searchCountriesByName_partialMatch_returnsResults() {
        List<Country> results = countryService.searchCountriesByName("ind");
        assertNotNull(results);
        assertTrue(results.size() >= 2,
                "Expected at least India and Indonesia for keyword 'ind'");

        boolean hasIndia = results.stream()
                .anyMatch(c -> "India".equals(c.getName()));
        boolean hasIndonesia = results.stream()
                .anyMatch(c -> "Indonesia".equals(c.getName()));

        assertTrue(hasIndia, "Results should contain India");
        assertTrue(hasIndonesia, "Results should contain Indonesia");
    }

    @Test
    @Order(9)
    @DisplayName("Hands-on 5: searchCountriesByName with no match returns empty list")
    void searchCountriesByName_noMatch_returnsEmptyList() {
        List<Country> results = countryService.searchCountriesByName("zzzznonexistent");
        assertNotNull(results);
        assertTrue(results.isEmpty(), "No results expected for nonsense keyword");
    }
}
