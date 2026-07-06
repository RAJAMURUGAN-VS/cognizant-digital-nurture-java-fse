package com.cognizant.springlearn.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * CountryNotFoundException — HTTP 404 when a country code is not found.
 * @ResponseStatus maps this exception to HTTP 404 Not Found automatically.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Country not found")
public class CountryNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public CountryNotFoundException(String code) {
        super("Country not found for code: " + code);
    }
}
