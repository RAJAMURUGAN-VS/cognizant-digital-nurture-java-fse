package com.cognizant.springlearn.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Country model with javax.validation constraints.
 *
 * @NotNull  — field must not be null; fails before size check.
 * @Size     — exactly 2 characters for code; min/max for name.
 *
 * When @Valid is placed on @RequestBody in the controller,
 * Spring calls the Hibernate Validator before the method runs.
 * If any constraint is violated, MethodArgumentNotValidException
 * is thrown → caught by GlobalExceptionHandler.
 */
public class Country {

    private static final Logger LOGGER = LoggerFactory.getLogger(Country.class);

    /**
     * ISO 3166-1 alpha-2 country code.
     * Must be exactly 2 characters (e.g. "IN", "US").
     */
    @NotNull(message = "Country code must not be null")
    @Size(min = 2, max = 2, message = "Country code should be 2 characters")
    private String code;

    @NotNull(message = "Country name must not be null")
    @Size(min = 1, max = 50, message = "Country name should be between 1 and 50 characters")
    private String name;

    public Country() {
        LOGGER.debug("Inside Country Constructor.");
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        LOGGER.debug("setCode={}", code);
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        LOGGER.debug("setName={}", name);
        this.name = name;
    }

    @Override
    public String toString() {
        return "Country{code='" + code + "', name='" + name + "'}";
    }
}
