package com.cognizant.ormlearn.service;

import com.cognizant.ormlearn.model.Country;
import com.cognizant.ormlearn.repository.CountryRepository;
import com.cognizant.ormlearn.service.exception.CountryNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * CountryService — business logic layer for Country operations.
 *
 * @Service  — registers this class as a Spring-managed service bean.
 *
 * @Transactional — Spring creates a Hibernate session and wraps each method
 * in a database transaction automatically. No need to call
 * session.beginTransaction() or tx.commit() manually (as in raw Hibernate).
 * This is the key benefit of Spring Data JPA over plain Hibernate.
 *
 * Hands-on coverage:
 *   Hands-on 1 : getAllCountries()
 *   Hands-on 6 : findCountryByCode()    — throws CountryNotFoundException
 *   Hands-on 7 : addCountry()
 *   Hands-on 8 : updateCountry()
 *   Hands-on 9 : deleteCountry()
 *   Hands-on 5 : searchCountriesByName()
 */
@Service
public class CountryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountryService.class);

    @Autowired
    private CountryRepository countryRepository;

    // ---------------------------------------------------------------
    // Hands-on 1: Get all countries
    // ---------------------------------------------------------------

    /**
     * Returns a list of all countries from the database.
     * readOnly=true is an optimisation hint — Hibernate skips dirty-check
     * on entities fetched in a read-only transaction.
     */
    @Transactional(readOnly = true)
    public List<Country> getAllCountries() {
        LOGGER.debug("Fetching all countries");
        return countryRepository.findAll();
    }

    // ---------------------------------------------------------------
    // Hands-on 6: Find country by code
    // ---------------------------------------------------------------

    /**
     * Returns the country matching the given code.
     *
     * @param countryCode ISO 3166-1 alpha-2 code (e.g. "IN", "US")
     * @return matching Country entity
     * @throws CountryNotFoundException if no country found for the code
     */
    @Transactional(readOnly = true)
    public Country findCountryByCode(String countryCode) throws CountryNotFoundException {
        LOGGER.debug("Finding country by code: {}", countryCode);

        // findById returns Optional<Country>
        Optional<Country> result = countryRepository.findById(countryCode);

        // If not found, throw our custom exception
        if (!result.isPresent()) {
            LOGGER.warn("Country not found for code: {}", countryCode);
            throw new CountryNotFoundException(countryCode);
        }

        Country country = result.get();
        LOGGER.debug("Found: {}", country);
        return country;
    }

    // ---------------------------------------------------------------
    // Hands-on 7: Add a new country
    // ---------------------------------------------------------------

    /**
     * Persists a new country to the database.
     * save() inserts if no entity with the same PK exists; updates otherwise.
     *
     * @param country the Country to persist
     */
    @Transactional
    public void addCountry(Country country) {
        LOGGER.debug("Adding country: {}", country);
        countryRepository.save(country);
        LOGGER.debug("Country added successfully: {}", country);
    }

    // ---------------------------------------------------------------
    // Hands-on 8: Update country name
    // ---------------------------------------------------------------

    /**
     * Updates the name of an existing country identified by code.
     *
     * Steps:
     *   1. Fetch existing country by code (throws CountryNotFoundException if missing).
     *   2. Update the name using the setter.
     *   3. Call save() to persist the change.
     *
     * @param code    ISO country code of the country to update
     * @param newName new name to set
     * @throws CountryNotFoundException if no country found for the code
     */
    @Transactional
    public void updateCountry(String code, String newName) throws CountryNotFoundException {
        LOGGER.debug("Updating country code={} to name='{}'", code, newName);

        // Step 1: get existing country reference
        Optional<Country> result = countryRepository.findById(code);
        if (!result.isPresent()) {
            throw new CountryNotFoundException(code);
        }

        Country country = result.get();

        // Step 2: update name
        country.setName(newName);

        // Step 3: save (merge) the updated entity
        countryRepository.save(country);

        LOGGER.debug("Country updated successfully: {}", country);
    }

    // ---------------------------------------------------------------
    // Hands-on 9: Delete country by code
    // ---------------------------------------------------------------

    /**
     * Deletes the country with the given code.
     * deleteById() is a built-in JpaRepository method — no SQL needed.
     *
     * @param code ISO country code of the country to delete
     */
    @Transactional
    public void deleteCountry(String code) {
        LOGGER.debug("Deleting country with code: {}", code);
        countryRepository.deleteById(code);
        LOGGER.debug("Country with code '{}' deleted successfully", code);
    }

    // ---------------------------------------------------------------
    // Hands-on 5: Search countries by partial name
    // ---------------------------------------------------------------

    /**
     * Returns all countries whose name contains the given keyword (case-insensitive).
     * Uses a derived query method defined in CountryRepository.
     *
     * @param keyword partial name to search for (e.g. "ind" matches "India", "Indonesia")
     * @return list of matching countries
     */
    @Transactional(readOnly = true)
    public List<Country> searchCountriesByName(String keyword) {
        LOGGER.debug("Searching countries with name containing: '{}'", keyword);
        List<Country> results = countryRepository.findByNameContainingIgnoreCase(keyword);
        LOGGER.debug("Found {} countries matching '{}'", results.size(), keyword);
        return results;
    }
}
