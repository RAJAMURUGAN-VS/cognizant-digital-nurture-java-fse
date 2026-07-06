package com.cognizant.springlearn.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * CountryNotFoundException — thrown when a country code is not found.
 *
 * @ResponseStatus tells Spring MVC to map this exception to an HTTP response:
 *   value  = HttpStatus.NOT_FOUND → HTTP 404 status code
 *   reason = "Country not found"  → reason phrase in the response body
 *
 * When Spring catches this exception, it returns:
 * {
 *   "timestamp": "...",
 *   "status": 404,
 *   "error": "Not Found",
 *   "message": "Country not found",
 *   "path": "/country/az"
 * }
 *
 * No try-catch needed in the controller — Spring handles it automatically.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Country not found")
public class CountryNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    public CountryNotFoundException(String countryCode) {
        super("Country not found for code: " + countryCode);
    }
}
