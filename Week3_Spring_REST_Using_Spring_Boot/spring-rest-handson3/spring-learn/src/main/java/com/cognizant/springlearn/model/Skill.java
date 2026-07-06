package com.cognizant.springlearn.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Skill — model class for employee skill data.
 * Loaded from employee.xml and reused across employees.
 * Jackson serialises: {"id":1,"name":"Java"}
 */
public class Skill {

    private static final Logger LOGGER = LoggerFactory.getLogger(Skill.class);

    private int    id;
    private String name;

    public Skill() {
        LOGGER.debug("Inside Skill Constructor.");
    }

    public int getId()              { return id; }
    public void setId(int id) {
        LOGGER.debug("Inside setId(). id={}", id);
        this.id = id;
    }

    public String getName()         { return name; }
    public void setName(String name) {
        LOGGER.debug("Inside setName(). name={}", name);
        this.name = name;
    }

    @Override
    public String toString() {
        return "Skill{id=" + id + ", name='" + name + "'}";
    }
}
