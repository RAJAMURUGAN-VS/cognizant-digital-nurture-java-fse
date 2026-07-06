package com.cognizant.springlearn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Country — model class representing an airline-supported country.
 *
 * Exercise 4: Loaded from country.xml via Spring Setter Injection.
 *   Spring calls the no-arg constructor first, then setCode() and setName().
 *
 * Exercise 5: Demonstrates SINGLETON vs PROTOTYPE scope.
 *   SINGLETON  — constructor called ONCE; same instance returned every time.
 *   PROTOTYPE  — constructor called for EACH getBean() call.
 *
 * Exercise 3: All constructor/setter/getter methods include DEBUG logs.
 *   This lets us trace exactly what Spring calls during bean creation.
 */
public class Country {

    private static final Logger LOGGER = LoggerFactory.getLogger(Country.class);

    /** ISO 3166-1 alpha-2 country code, e.g. "IN", "US". */
    private String code;

    /** Full country name, e.g. "India". */
    private String name;

    // ---------------------------------------------------------------
    // Constructor
    // ---------------------------------------------------------------

    /**
     * No-arg constructor required for Spring Setter Injection.
     * Spring first calls this constructor, then calls the setter methods.
     *
     * Exercise 5 insight:
     *   SINGLETON scope → this log appears ONCE (bean created once at startup).
     *   PROTOTYPE scope → this log appears for EACH getBean() call.
     */
    public Country() {
        LOGGER.debug("Inside Country Constructor.");
    }

    // ---------------------------------------------------------------
    // Getters and Setters (Exercise 4: Spring calls these for injection)
    // ---------------------------------------------------------------

    public String getCode() {
        LOGGER.debug("Inside getCode(). code={}", code);
        return code;
    }

    /**
     * Called by Spring when <property name="code" value="IN" /> is processed.
     */
    public void setCode(String code) {
        LOGGER.debug("Inside setCode(). code={}", code);
        this.code = code;
    }

    public String getName() {
        LOGGER.debug("Inside getName(). name={}", name);
        return name;
    }

    /**
     * Called by Spring when <property name="name" value="India" /> is processed.
     */
    public void setName(String name) {
        LOGGER.debug("Inside setName(). name={}", name);
        this.name = name;
    }

    // ---------------------------------------------------------------
    // toString
    // ---------------------------------------------------------------

    @Override
    public String toString() {
        return "Country{code='" + code + "', name='" + name + "'}";
    }
}
