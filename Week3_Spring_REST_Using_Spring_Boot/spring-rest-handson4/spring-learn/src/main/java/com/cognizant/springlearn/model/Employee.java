package com.cognizant.springlearn.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

/**
 * Employee model with full javax.validation constraints.
 *
 * Validation annotations:
 *   @NotNull     — field must not be null
 *   @NotBlank    — string must not be null or whitespace-only
 *   @Size        — string length constraints
 *   @Min         — numeric minimum
 *   @Valid       — cascade validation into nested objects (department, skills)
 *
 * @JsonFormat:
 *   Tells Jackson to parse/format the dateOfBirth field using "dd/MM/yyyy".
 *   Without this, Jackson would expect ISO-8601 format.
 *   If a malformed date is sent, Jackson throws HttpMessageNotReadableException
 *   → caught by GlobalExceptionHandler.handleHttpMessageNotReadable().
 */
public class Employee {

    private static final Logger LOGGER = LoggerFactory.getLogger(Employee.class);

    @NotNull(message = "Employee id must not be null")
    private Integer id;

    @NotNull(message = "Employee name must not be null")
    @NotBlank(message = "Employee name must not be blank")
    @Size(min = 1, max = 30, message = "Employee name should be between 1 and 30 characters")
    private String name;

    @NotNull(message = "Email must not be null")
    private String email;

    @NotNull(message = "Salary must not be null")
    @Min(value = 0, message = "Salary must be zero or above")
    private Double salary;

    @NotNull(message = "Permanent flag must not be null")
    private Boolean permanent;

    /**
     * @JsonFormat — instructs Jackson to parse this field as "dd/MM/yyyy".
     * If an incorrect format is passed (e.g. "1990-03-15"), Jackson throws
     * InvalidFormatException, which is wrapped in HttpMessageNotReadableException.
     * GlobalExceptionHandler.handleHttpMessageNotReadable() handles this case.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date dateOfBirth;

    /**
     * @Valid — cascade validation into Department object.
     * If department fields fail validation, errors are included in the response.
     */
    @NotNull(message = "Department must not be null")
    @Valid
    private Department department;

    /**
     * @Valid — cascade validation into each Skill in the list.
     */
    @NotNull(message = "Skills must not be null")
    private List<@Valid Skill> skills;

    public Employee() {
        LOGGER.debug("Inside Employee Constructor.");
    }

    public Integer getId()                  { return id; }
    public void setId(Integer id) {
        LOGGER.debug("setId={}", id);
        this.id = id;
    }

    public String getName()                 { return name; }
    public void setName(String name) {
        LOGGER.debug("setName={}", name);
        this.name = name;
    }

    public String getEmail()                { return email; }
    public void setEmail(String email) {
        LOGGER.debug("setEmail={}", email);
        this.email = email;
    }

    public Double getSalary()               { return salary; }
    public void setSalary(Double salary) {
        LOGGER.debug("setSalary={}", salary);
        this.salary = salary;
    }

    public Boolean getPermanent()           { return permanent; }
    public void setPermanent(Boolean permanent) {
        LOGGER.debug("setPermanent={}", permanent);
        this.permanent = permanent;
    }

    public Date getDateOfBirth()            { return dateOfBirth; }
    public void setDateOfBirth(Date dateOfBirth) {
        LOGGER.debug("setDateOfBirth={}", dateOfBirth);
        this.dateOfBirth = dateOfBirth;
    }

    public Department getDepartment()       { return department; }
    public void setDepartment(Department department) {
        LOGGER.debug("setDepartment={}", department);
        this.department = department;
    }

    public List<Skill> getSkills()          { return skills; }
    public void setSkills(List<Skill> skills) {
        LOGGER.debug("setSkills count={}", skills != null ? skills.size() : 0);
        this.skills = skills;
    }

    @Override
    public String toString() {
        return "Employee{id=" + id + ", name='" + name + "', dept=" + department + "}";
    }
}
