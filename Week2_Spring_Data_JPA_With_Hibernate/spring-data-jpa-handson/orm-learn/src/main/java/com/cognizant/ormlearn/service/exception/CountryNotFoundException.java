package com.cognizant.ormlearn.service.exception;

/**
 * CountryNotFoundException — thrown when a country lookup by code finds no result.
 *
 * Hands-on 6: Custom exception for service-layer error reporting.
 *
 * Extends RuntimeException so callers do not need to declare it in their
 * throws clause, but the exception still propagates up the call stack.
 */
public class CountryNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * @param countryCode the code that was not found in the database
     */
    public CountryNotFoundException(String countryCode) {
        super("Country not found for code: '" + countryCode + "'");
    }
}
