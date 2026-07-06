package com.cognizant.springlearn.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Employee — model class for employee data.
 *
 * Loaded from employee.xml via Spring Setter Injection.
 * References Department and a List of Skills.
 *
 * Jackson serialises this to JSON:
 * {
 *   "id": 1,
 *   "name": "Alice Johnson",
 *   "email": "alice@example.com",
 *   "salary": 85000.0,
 *   "permanent": true,
 *   "department": {"id":1,"name":"Engineering"},
 *   "skills": [{"id":1,"name":"Java"}, ...]
 * }
 */
public class Employee {

    private static final Logger LOGGER = LoggerFactory.getLogger(Employee.class);

    private int        id;
    private String     name;
    private String     email;
    private double     salary;
    private boolean    permanent;
    private Department department;
    private List<Skill> skills;

    public Employee() {
        LOGGER.debug("Inside Employee Constructor.");
    }

    public int getId()                  { return id; }
    public void setId(int id) {
        LOGGER.debug("setId={}", id);
        this.id = id;
    }

    public String getName()             { return name; }
    public void setName(String name) {
        LOGGER.debug("setName={}", name);
        this.name = name;
    }

    public String getEmail()            { return email; }
    public void setEmail(String email) {
        LOGGER.debug("setEmail={}", email);
        this.email = email;
    }

    public double getSalary()           { return salary; }
    public void setSalary(double salary) {
        LOGGER.debug("setSalary={}", salary);
        this.salary = salary;
    }

    public boolean isPermanent()        { return permanent; }
    public void setPermanent(boolean permanent) {
        LOGGER.debug("setPermanent={}", permanent);
        this.permanent = permanent;
    }

    public Department getDepartment()   { return department; }
    public void setDepartment(Department department) {
        LOGGER.debug("setDepartment={}", department);
        this.department = department;
    }

    public List<Skill> getSkills()      { return skills; }
    public void setSkills(List<Skill> skills) {
        LOGGER.debug("setSkills count={}", skills != null ? skills.size() : 0);
        this.skills = skills;
    }

    @Override
    public String toString() {
        return "Employee{id=" + id + ", name='" + name + "', dept=" + department + "}";
    }
}
