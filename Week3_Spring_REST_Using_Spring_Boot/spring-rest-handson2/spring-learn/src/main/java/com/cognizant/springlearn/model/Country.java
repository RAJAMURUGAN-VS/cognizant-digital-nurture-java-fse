package com.cognizant.springlearn.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Country — model class for airline country data.
 *
 * Spring uses this class to create beans via Setter Injection:
 *   1. Calls no-arg constructor
 *   2. Calls setCode() with value from <property name="code">
 *   3. Calls setName() with value from <property name="name">
 *
 * Jackson (included with Spring Web) automatically serialises this
 * to JSON when returned from a @RestController method:
 *   {"code":"IN","name":"India"}
 */
public class Country {

    private static final Logger LOGGER = LoggerFactory.getLogger(Country.class);

    private String code;
    private String name;

    /** No-arg constructor required for Spring Setter Injection. */
    public Country() {
        LOGGER.debug("Inside Country Constructor.");
    }

    public String getCode() {
        LOGGER.debug("Inside getCode(). code={}", code);
        return code;
    }

    public void setCode(String code) {
        LOGGER.debug("Inside setCode(). code={}", code);
        this.code = code;
    }

    public String getName() {
        LOGGER.debug("Inside getName(). name={}", name);
        return name;
    }

    public void setName(String name) {
        LOGGER.debug("Inside setName(). name={}", name);
        this.name = name;
    }

    @Override
    public String toString() {
        return "Country{code='" + code + "', name='" + name + "'}";
    }
}
