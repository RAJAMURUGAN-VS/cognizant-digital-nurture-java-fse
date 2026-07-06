package com.cognizant.springlearn.controller;

import com.cognizant.springlearn.model.Country;
import com.cognizant.springlearn.service.CountryService;
import com.cognizant.springlearn.service.exception.CountryNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * CountryController — full CRUD REST controller for Country.
 *
 * RESTful URL naming convention (all under /countries):
 *   GET    /countries        — get all
 *   GET    /countries/{code} — get one
 *   POST   /countries        — create (body = JSON country)
 *   PUT    /countries        — update (body = JSON country)
 *   DELETE /countries/{code} — delete
 *
 * @RequestMapping at class level → all methods share /countries base path.
 *
 * @RequestBody:
 *   Jackson deserialises the JSON request body to a Country object.
 *   For each JSON field, Jackson calls the matching setter:
 *     {"code":"IN","name":"India"} → setCode("IN"), setName("India")
 *   If a field name is misspelled (e.g. "nae" instead of "name"),
 *   Jackson ignores it and the field stays null.
 *
 * @Valid:
 *   Triggers Hibernate Validator on the Country bean BEFORE the method runs.
 *   If any constraint fails → MethodArgumentNotValidException
 *   → GlobalExceptionHandler.handleMethodArgumentNotValid() responds.
 *   Controller method is NOT called on validation failure.
 *
 * curl test commands:
 *   POST   curl -i -H 'Content-Type: application/json' -X POST -s \
 *               -d '{"code":"IN","name":"India"}' http://localhost:8083/countries
 *   POST invalid (1 char code):
 *          curl -i -H 'Content-Type: application/json' -X POST -s \
 *               -d '{"code":"I","name":"India"}' http://localhost:8083/countries
 *   PUT    curl -i -H 'Content-Type: application/json' -X PUT -s \
 *               -d '{"code":"IN","name":"Bharat"}' http://localhost:8083/countries
 *   DELETE curl -i -X DELETE -s http://localhost:8083/countries/IN
 */
@RestController
@RequestMapping("/countries")
public class CountryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountryController.class);

    @Autowired
    private CountryService countryService;

    public CountryController() {
        LOGGER.debug("Inside CountryController Constructor.");
    }

    // ---------------------------------------------------------------
    // GET /countries
    // ---------------------------------------------------------------
    @GetMapping
    public List<Country> getAllCountries() {
        LOGGER.info("START getAllCountries");
        List<Country> list = countryService.getAllCountries();
        LOGGER.debug("Returning {} countries", list.size());
        LOGGER.info("END getAllCountries");
        return list;
    }

    // ---------------------------------------------------------------
    // GET /countries/{code}
    // ---------------------------------------------------------------
    @GetMapping("/{code}")
    public Country getCountry(@PathVariable String code) throws CountryNotFoundException {
        LOGGER.info("START getCountry({})", code);
        Country c = countryService.getCountry(code);
        LOGGER.debug("Returning: {}", c);
        LOGGER.info("END getCountry");
        return c;
    }

    // ---------------------------------------------------------------
    // POST /countries  — create
    // ---------------------------------------------------------------

    /**
     * Adds a new country from the JSON request body.
     *
     * @RequestBody — Jackson maps JSON → Country object (setter injection).
     * @Valid       — Hibernate Validator checks @NotNull + @Size constraints.
     *
     * If validation passes: method runs, country returned with 201 Created.
     * If validation fails:  GlobalExceptionHandler intercepts, 400 returned.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Country addCountry(@RequestBody @Valid Country country) {
        LOGGER.info("START addCountry");
        LOGGER.debug("Country received: {}", country);
        Country added = countryService.addCountry(country);
        LOGGER.debug("Country added: {}", added);
        LOGGER.info("END addCountry");
        return added;
    }

    // ---------------------------------------------------------------
    // PUT /countries  — update
    // ---------------------------------------------------------------

    /**
     * Updates an existing country. Body contains full country JSON.
     * @Valid validates the payload before the method is called.
     */
    @PutMapping
    public Country updateCountry(@RequestBody @Valid Country country)
            throws CountryNotFoundException {
        LOGGER.info("START updateCountry");
        LOGGER.debug("Country to update: {}", country);
        Country updated = countryService.updateCountry(country);
        LOGGER.debug("Country updated: {}", updated);
        LOGGER.info("END updateCountry");
        return updated;
    }

    // ---------------------------------------------------------------
    // DELETE /countries/{code}
    // ---------------------------------------------------------------

    /**
     * Deletes a country by its ISO code.
     * Returns 204 No Content on success.
     */
    @DeleteMapping("/{code}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCountry(@PathVariable String code) throws CountryNotFoundException {
        LOGGER.info("START deleteCountry({})", code);
        countryService.deleteCountry(code);
        LOGGER.info("END deleteCountry");
    }
}
