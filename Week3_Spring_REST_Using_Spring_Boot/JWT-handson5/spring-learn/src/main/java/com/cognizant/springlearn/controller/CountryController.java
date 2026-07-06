package com.cognizant.springlearn.controller;

import com.cognizant.springlearn.model.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * CountryController — REST controller for Country endpoints.
 *
 * Security:
 *   All endpoints require a valid JWT Bearer token.
 *   (configured via anyRequest().authenticated() in SecurityConfig)
 *
 * Access flow:
 *   1. curl -s -u user:pwd http://localhost:8083/authenticate
 *      → receives {"token":"eyJ..."}
 *   2. curl -s -H "Authorization: Bearer eyJ..." http://localhost:8083/countries
 *      → JwtAuthorizationFilter validates token
 *      → if valid → returns country list
 *      → if tampered/expired → 401 Unauthorized
 *
 * curl test (Basic Auth — Step 2 only, no JWT):
 *   curl -s -u user:pwd http://localhost:8083/countries
 *
 * curl test (JWT — Step 3):
 *   curl -s -H "Authorization: Bearer TOKEN" http://localhost:8083/countries
 */
@RestController
@RequestMapping("/countries")
public class CountryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountryController.class);

    public CountryController() {
        LOGGER.debug("Inside CountryController Constructor.");
    }

    @GetMapping
    @SuppressWarnings("unchecked")
    public List<Country> getAllCountries() {
        LOGGER.info("START getAllCountries");
        ApplicationContext ctx = new ClassPathXmlApplicationContext("country.xml");
        List<Country> countries = ctx.getBean("countryList", List.class);
        ((ClassPathXmlApplicationContext) ctx).close();
        LOGGER.debug("Returning {} countries", countries.size());
        LOGGER.info("END getAllCountries");
        return countries;
    }

    @GetMapping("/{code}")
    @SuppressWarnings("unchecked")
    public Country getCountry(@PathVariable String code) {
        LOGGER.info("START getCountry({})", code);
        ApplicationContext ctx = new ClassPathXmlApplicationContext("country.xml");
        List<Country> countries = ctx.getBean("countryList", List.class);
        ((ClassPathXmlApplicationContext) ctx).close();
        Country found = countries.stream()
                .filter(c -> c.getCode().equalsIgnoreCase(code))
                .findFirst().orElse(null);
        LOGGER.debug("Returning: {}", found);
        LOGGER.info("END getCountry");
        return found;
    }
}
