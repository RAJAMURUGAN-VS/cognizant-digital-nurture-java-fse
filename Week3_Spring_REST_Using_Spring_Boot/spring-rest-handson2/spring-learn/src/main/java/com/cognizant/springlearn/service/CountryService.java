package com.cognizant.springlearn.service;

import com.cognizant.springlearn.model.Country;
import com.cognizant.springlearn.service.exception.CountryNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CountryService — service layer for country operations.
 *
 * Loads country data from the Spring XML configuration file (country.xml).
 * Provides methods used by CountryController REST endpoints.
 *
 * @Service — registers this class as a Spring-managed service bean.
 *   Spring Boot auto-detects it via component scanning.
 */
@Service
public class CountryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountryService.class);

    /**
     * Returns India (IN) — the single country bean.
     * Loads the "in" bean from country.xml.
     *
     * Used by: GET /country
     */
    public Country getCountryIndia() {
        LOGGER.info("START getCountryIndia");

        ApplicationContext context =
                new ClassPathXmlApplicationContext("country.xml");
        Country country = context.getBean("in", Country.class);

        LOGGER.debug("Country loaded: {}", country);
        ((ClassPathXmlApplicationContext) context).close();

        LOGGER.info("END getCountryIndia");
        return country;
    }

    /**
     * Returns all four countries from country.xml.
     * Loads the "countryList" ArrayList bean.
     *
     * Used by: GET /countries
     */
    @SuppressWarnings("unchecked")
    public List<Country> getAllCountries() {
        LOGGER.info("START getAllCountries");

        ApplicationContext context =
                new ClassPathXmlApplicationContext("country.xml");
        List<Country> countries = context.getBean("countryList", List.class);

        LOGGER.debug("Countries loaded: {}", countries);
        ((ClassPathXmlApplicationContext) context).close();

        LOGGER.info("END getAllCountries");
        return countries;
    }

    /**
     * Returns a specific country by code (case-insensitive).
     *
     * Used by: GET /countries/{code}
     *
     * Implementation:
     *   1. Load country list from country.xml.
     *   2. Iterate and compare codes ignoring case.
     *   3. If no match found, throw CountryNotFoundException.
     *
     * @param code ISO 3166-1 alpha-2 country code (e.g. "in", "IN", "Us")
     * @return matching Country
     * @throws CountryNotFoundException if no country matches the code
     */
    @SuppressWarnings("unchecked")
    public Country getCountry(String code) throws CountryNotFoundException {
        LOGGER.info("START getCountry({})", code);

        ApplicationContext context =
                new ClassPathXmlApplicationContext("country.xml");
        List<Country> countries = context.getBean("countryList", List.class);
        ((ClassPathXmlApplicationContext) context).close();

        // Lambda: case-insensitive match on country code
        Country found = countries.stream()
                .filter(c -> c.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElse(null);

        if (found == null) {
            LOGGER.warn("Country not found for code: {}", code);
            throw new CountryNotFoundException(code);
        }

        LOGGER.debug("Country found: {}", found);
        LOGGER.info("END getCountry");
        return found;
    }
}
