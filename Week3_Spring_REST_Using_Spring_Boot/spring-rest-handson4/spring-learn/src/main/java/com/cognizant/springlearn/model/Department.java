package com.cognizant.springlearn.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Department model with validation constraints.
 */
public class Department {

    private static final Logger LOGGER = LoggerFactory.getLogger(Department.class);

    @NotNull(message = "Department id must not be null")
    private Integer id;

    @NotNull(message = "Department name must not be null")
    @NotBlank(message = "Department name must not be blank")
    @Size(min = 1, max = 30, message = "Department name should be between 1 and 30 characters")
    private String name;

    public Department() {
        LOGGER.debug("Inside Department Constructor.");
    }

    public Integer getId()              { return id; }
    public void setId(Integer id) {
        LOGGER.debug("setId={}", id);
        this.id = id;
    }

    public String getName()             { return name; }
    public void setName(String name) {
        LOGGER.debug("setName={}", name);
        this.name = name;
    }

    @Override
    public String toString() {
        return "Department{id=" + id + ", name='" + name + "'}";
    }
}
