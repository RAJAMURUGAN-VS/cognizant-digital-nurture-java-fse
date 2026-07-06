package com.cognizant.springlearn.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Country — model class loaded from country.xml.
 * Jackson serialises this to JSON: {"code":"IN","name":"India"}
 */
public class Country {

    private static final Logger LOGGER = LoggerFactory.getLogger(Country.class);

    private String code;
    private String name;

    public Country() {
        LOGGER.debug("Inside Country Constructor.");
    }

    public String getCode()          { return code; }
    public void setCode(String code) {
        LOGGER.debug("setCode={}", code);
        this.code = code;
    }

    public String getName()          { return name; }
    public void setName(String name) {
        LOGGER.debug("setName={}", name);
        this.name = name;
    }

    @Override
    public String toString() {
        return "Country{code='" + code + "', name='" + name + "'}";
    }
}
