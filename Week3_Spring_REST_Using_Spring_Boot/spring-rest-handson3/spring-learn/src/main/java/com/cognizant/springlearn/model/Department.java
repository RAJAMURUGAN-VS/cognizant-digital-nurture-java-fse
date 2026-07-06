package com.cognizant.springlearn.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Department — model class for department data.
 * Loaded from employee.xml via Spring Setter Injection.
 * Jackson serialises this to JSON: {"id":1,"name":"Engineering"}
 */
public class Department {

    private static final Logger LOGGER = LoggerFactory.getLogger(Department.class);

    private int    id;
    private String name;

    public Department() {
        LOGGER.debug("Inside Department Constructor.");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        LOGGER.debug("Inside setId(). id={}", id);
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        LOGGER.debug("Inside setName(). name={}", name);
        this.name = name;
    }

    @Override
    public String toString() {
        return "Department{id=" + id + ", name='" + name + "'}";
    }
}
