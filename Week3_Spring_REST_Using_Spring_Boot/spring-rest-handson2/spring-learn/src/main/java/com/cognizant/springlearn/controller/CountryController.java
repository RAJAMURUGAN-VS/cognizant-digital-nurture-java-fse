package com.cognizant.springlearn.controller;

import com.cognizant.springlearn.model.Country;
import com.cognizant.springlearn.service.CountryService;
import com.cognizant.springlearn.service.exception.CountryNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CountryController — REST controller for Country endpoints.
 *
 * How bean-to-JSON conversion works:
 *   @RestController tells Spring to write return values to the HTTP response.
 *   Jackson (included with spring-boot-starter-web) serialises the
 *   Country object to JSON automatically:
 *     Country{code="IN", name="India"} → {"code":"IN","name":"India"}
 *   The Content-Type header in the response is set to application/json.
 *
 * Dispatcher Servlet flow:
 *   HTTP Request → DispatcherServlet → HandlerMapping → CountryController
 *     → Service → country.xml beans → JSON response → HTTP Response
 *
 * Endpoints:
 *   GET /country              — returns India (single country)
 *   GET /countries            — returns all four countries
 *   GET /countries/{code}     — returns country matching code (case-insensitive)
 */
@RestController
public class CountryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountryController.class);

    @Autowired
    private CountryService countryService;

    /**
     * Constructor — log message allows MockMVC test to verify bean loading.
     */
    public CountryController() {
        LOGGER.debug("Inside CountryController Constructor.");
    }

    // ---------------------------------------------------------------
    // REST - Country Web Service
    // GET /country → returns India as JSON
    // ---------------------------------------------------------------

    /**
     * Returns India country details loaded from Spring XML configuration.
     *
     * @RequestMapping — maps HTTP requests to this method.
     *   Without method attribute it responds to ALL HTTP methods (GET, POST…).
     *   Use @GetMapping for GET-only endpoints (preferred).
     *
     * Sample request:  GET http://localhost:8083/country
     * Sample response: {"code":"IN","name":"India"}
     *
     * HTTP Response Headers (visible in Chrome DevTools / Postman):
     *   Content-Type: application/json
     *   Status: 200 OK
     */
    @RequestMapping("/country")
    public Country getCountryIndia() {
        LOGGER.info("START getCountryIndia");
        Country country = countryService.getCountryIndia();
        LOGGER.debug("Returning country: {}", country);
        LOGGER.info("END getCountryIndia");
        return country;
    }

    // ---------------------------------------------------------------
    // REST - Get all countries
    // GET /countries → returns list of all countries as JSON array
    // ---------------------------------------------------------------

    /**
     * Returns all four airline-supported countries as a JSON array.
     *
     * Jackson serialises List<Country> to a JSON array:
     *   [{"code":"IN","name":"India"}, {"code":"US","name":"United States"}, ...]
     *
     * Sample request:  GET http://localhost:8083/countries
     * Sample response: [{"code":"IN",...},{"code":"US",...},...]
     */
    @GetMapping("/countries")
    public List<Country> getAllCountries() {
        LOGGER.info("START getAllCountries");
        List<Country> countries = countryService.getAllCountries();
        LOGGER.debug("Returning {} countries", countries.size());
        LOGGER.info("END getAllCountries");
        return countries;
    }

    // ---------------------------------------------------------------
    // REST - Get country by code
    // GET /countries/{code} → returns specific country (case-insensitive)
    // ---------------------------------------------------------------

    /**
     * Returns a specific country based on the country code in the URL path.
     *
     * @PathVariable — extracts the {code} segment from the URL path.
     *   GET /countries/in  → code = "in"  → matches India (IN)
     *   GET /countries/US  → code = "US"  → matches United States
     *   GET /countries/az  → code = "az"  → not found → 404 response
     *
     * throws CountryNotFoundException:
     *   @ResponseStatus on the exception class maps it to HTTP 404.
     *   Spring automatically sends the error response — no try-catch needed here.
     *
     * Sample request:  GET http://localhost:8083/countries/in
     * Sample response: {"code":"IN","name":"India"}
     *
     * Error case:
     * Sample request:  GET http://localhost:8083/countries/az
     * Sample response: {"status":404,"error":"Not Found","message":"Country not found"}
     *
     * Test with curl:
     *   curl -i http://localhost:8083/countries/az
     */
    @GetMapping("/countries/{code}")
    public Country getCountry(@PathVariable String code)
            throws CountryNotFoundException {
        LOGGER.info("START getCountry({})", code);
        Country country = countryService.getCountry(code);
        LOGGER.debug("Returning country: {}", country);
        LOGGER.info("END getCountry");
        return country;
    }
}
