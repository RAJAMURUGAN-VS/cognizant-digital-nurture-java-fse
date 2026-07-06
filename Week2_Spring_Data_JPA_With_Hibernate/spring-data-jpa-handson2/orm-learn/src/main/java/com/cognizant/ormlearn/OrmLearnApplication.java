package com.cognizant.ormlearn;

import com.cognizant.ormlearn.model.*;
import com.cognizant.ormlearn.repository.CountryRepository;
import com.cognizant.ormlearn.repository.StockRepository;
import com.cognizant.ormlearn.service.DepartmentService;
import com.cognizant.ormlearn.service.EmployeeService;
import com.cognizant.ormlearn.service.SkillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * OrmLearnApplication — main class for all 6 hands-on exercises.
 *
 * Hands-on 1 : Country Query Methods  (findByNameContaining etc.)
 * Hands-on 2 : Stock Query Methods    (date range, greater than, top N)
 * Hands-on 3 : Payroll schema bean mapping (no test methods here)
 * Hands-on 4 : Many-to-One  (Employee ↔ Department)
 * Hands-on 5 : One-to-Many  (Department → Employees, EAGER fetch)
 * Hands-on 6 : Many-to-Many (Employee ↔ Skill)
 */
@SpringBootApplication
public class OrmLearnApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrmLearnApplication.class);

    // Repositories used directly (Hands-on 1 & 2)
    private static CountryRepository countryRepository;
    private static StockRepository   stockRepository;

    // Services (Hands-on 4, 5, 6)
    private static EmployeeService   employeeService;
    private static DepartmentService departmentService;
    private static SkillService      skillService;

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(OrmLearnApplication.class, args);
        LOGGER.info("Inside main");

        // Wire beans from context
        countryRepository  = context.getBean(CountryRepository.class);
        stockRepository    = context.getBean(StockRepository.class);
        employeeService    = context.getBean(EmployeeService.class);
        departmentService  = context.getBean(DepartmentService.class);
        skillService       = context.getBean(SkillService.class);

        // ---------------------------------------------------------------
        // Hands-on 1: Country Query Methods
        // ---------------------------------------------------------------
        testCountryContaining();
        testCountryContainingSorted();
        testCountryStartingWith();

        // ---------------------------------------------------------------
        // Hands-on 2: Stock Query Methods
        // ---------------------------------------------------------------
        testFacebookSeptemberStocks();
        testGoogleCloseGreaterThan();
        testTop3ByVolume();
        testNetflixLowest3();

        // ---------------------------------------------------------------
        // Hands-on 4: Many-to-One
        // ---------------------------------------------------------------
        testGetEmployee();
        testAddEmployee();
        testUpdateEmployee();

        // ---------------------------------------------------------------
        // Hands-on 5: One-to-Many (Department → Employee list)
        // ---------------------------------------------------------------
        testGetDepartment();

        // ---------------------------------------------------------------
        // Hands-on 6: Many-to-Many (Employee ↔ Skill)
        // ---------------------------------------------------------------
        testGetEmployeeSkills();
        testAddSkillToEmployee();
    }

    // ================================================================
    // HANDS-ON 1: Country Query Methods
    // ================================================================

    /**
     * Search countries whose name contains "ou" (case-sensitive).
     * Expected: Bouvet Island, Djibouti, Guadeloupe, Luxembourg, South Sudan...
     */
    private static void testCountryContaining() {
        LOGGER.info("Start testCountryContaining");
        List<Country> countries = countryRepository.findByNameContaining("ou");
        LOGGER.debug("Countries containing 'ou': count={}", countries.size());
        countries.forEach(c -> LOGGER.debug("  {}", c));
        LOGGER.info("End testCountryContaining");
    }

    /**
     * Same search but sorted ascending by name.
     */
    private static void testCountryContainingSorted() {
        LOGGER.info("Start testCountryContainingSorted");
        List<Country> countries = countryRepository.findByNameContainingOrderByNameAsc("ou");
        LOGGER.debug("Countries containing 'ou' (sorted): count={}", countries.size());
        countries.forEach(c -> LOGGER.debug("  {}", c));
        LOGGER.info("End testCountryContainingSorted");
    }

    /**
     * Countries starting with "Z".
     * Expected: Zambia, Zimbabwe
     */
    private static void testCountryStartingWith() {
        LOGGER.info("Start testCountryStartingWith");
        List<Country> countries = countryRepository.findByNameStartingWith("Z");
        LOGGER.debug("Countries starting with 'Z': count={}", countries.size());
        countries.forEach(c -> LOGGER.debug("  {}", c));
        LOGGER.info("End testCountryStartingWith");
    }

    // ================================================================
    // HANDS-ON 2: Stock Query Methods
    // ================================================================

    /**
     * Facebook (FB) stock records for September 2019.
     */
    private static void testFacebookSeptemberStocks() {
        LOGGER.info("Start testFacebookSeptemberStocks");
        List<Stock> stocks = stockRepository.findByCodeAndDateBetween(
                "FB",
                LocalDate.of(2019, 9, 1),
                LocalDate.of(2019, 9, 30));
        LOGGER.debug("FB Sep 2019 records: count={}", stocks.size());
        stocks.forEach(s -> LOGGER.debug("  {}", s));
        LOGGER.info("End testFacebookSeptemberStocks");
    }

    /**
     * Google (GOOGL) stocks where close > 1250.
     */
    private static void testGoogleCloseGreaterThan() {
        LOGGER.info("Start testGoogleCloseGreaterThan");
        List<Stock> stocks = stockRepository.findByCodeAndCloseGreaterThan(
                "GOOGL", new BigDecimal("1250"));
        LOGGER.debug("GOOGL close > 1250: count={}", stocks.size());
        stocks.forEach(s -> LOGGER.debug("  {}", s));
        LOGGER.info("End testGoogleCloseGreaterThan");
    }

    /**
     * Top 3 records with highest trading volume across all stocks.
     */
    private static void testTop3ByVolume() {
        LOGGER.info("Start testTop3ByVolume");
        List<Stock> stocks = stockRepository.findTop3ByOrderByVolumeDesc();
        LOGGER.debug("Top 3 by volume:");
        stocks.forEach(s -> LOGGER.debug("  {}", s));
        LOGGER.info("End testTop3ByVolume");
    }

    /**
     * Netflix (NFLX) 3 records with lowest close price.
     */
    private static void testNetflixLowest3() {
        LOGGER.info("Start testNetflixLowest3");
        List<Stock> stocks = stockRepository.findTop3ByCodeOrderByCloseAsc("NFLX");
        LOGGER.debug("NFLX lowest 3 close:");
        stocks.forEach(s -> LOGGER.debug("  {}", s));
        LOGGER.info("End testNetflixLowest3");
    }

    // ================================================================
    // HANDS-ON 4: Many-to-One — Employee + Department
    // ================================================================

    /**
     * Fetch employee id=1 and log both employee and their department.
     * Hibernate fires a LEFT OUTER JOIN with department table (EAGER default).
     */
    private static void testGetEmployee() {
        LOGGER.info("Start testGetEmployee");
        Employee employee = employeeService.get(1);
        LOGGER.debug("Employee: {}", employee);
        LOGGER.debug("Department: {}", employee.getDepartment());
        LOGGER.info("End testGetEmployee");
    }

    /**
     * Add a new employee linked to department id=1.
     */
    private static void testAddEmployee() {
        LOGGER.info("Start testAddEmployee");

        Employee employee = new Employee();
        employee.setName("Grace Hopper");
        employee.setSalary(95000.00);
        employee.setPermanent(true);
        employee.setDateOfBirth(new Date(70, 11, 9)); // 1970-12-09

        // Get department from DB and set on employee
        Department department = departmentService.get(1);
        employee.setDepartment(department);

        employeeService.save(employee);
        LOGGER.debug("Saved new employee: {}", employee);
        LOGGER.info("End testAddEmployee");
    }

    /**
     * Update an existing employee's department.
     */
    private static void testUpdateEmployee() {
        LOGGER.info("Start testUpdateEmployee");

        // Get employee id=2
        Employee employee = employeeService.get(2);
        LOGGER.debug("Before update: {}", employee);

        // Change to a different department (id=3)
        Department newDept = departmentService.get(3);
        employee.setDepartment(newDept);

        employeeService.save(employee);
        LOGGER.debug("After update: {}", employee);
        LOGGER.info("End testUpdateEmployee");
    }

    // ================================================================
    // HANDS-ON 5: One-to-Many — Department + Employee list
    // ================================================================

    /**
     * Fetch department id=1 (Engineering) and list all its employees.
     *
     * Without FetchType.EAGER on @OneToMany in Department.java this would
     * throw LazyInitializationException because the session closes after
     * departmentService.get() returns.
     *
     * With FetchType.EAGER, Hibernate fetches employees in the same query.
     */
    private static void testGetDepartment() {
        LOGGER.info("Start testGetDepartment");
        Department department = departmentService.get(1);
        LOGGER.debug("Department: {}", department);
        LOGGER.debug("Employee list: {}", department.getEmployeeList());
        LOGGER.debug("Employee count in dept: {}", department.getEmployeeList().size());
        LOGGER.info("End testGetDepartment");
    }

    // ================================================================
    // HANDS-ON 6: Many-to-Many — Employee + Skills
    // ================================================================

    /**
     * Fetch employee id=1 and log all associated skills.
     *
     * FetchType.EAGER on @ManyToMany in Employee.java ensures skillList
     * is loaded without LazyInitializationException.
     */
    private static void testGetEmployeeSkills() {
        LOGGER.info("Start testGetEmployeeSkills");
        Employee employee = employeeService.get(1);
        LOGGER.debug("Employee: {}", employee);
        LOGGER.debug("Department: {}", employee.getDepartment());
        LOGGER.debug("Skills: {}", employee.getSkillList());
        LOGGER.info("End testGetEmployeeSkills");
    }

    /**
     * Add skill id=4 (Python) to employee id=2 (Bob Smith).
     * Bob already has Java (1) and Python (4) from seed data —
     * change skill id to one not already assigned to verify the insert.
     *
     * Checks employee_skill join table after execution.
     */
    private static void testAddSkillToEmployee() {
        LOGGER.info("Start testAddSkillToEmployee");

        // Employee id=2 (Bob Smith), add skill id=3 (MySQL) which he doesn't have
        Employee employee = employeeService.get(2);
        Skill skill = skillService.get(3); // MySQL

        LOGGER.debug("Before add — skills: {}", employee.getSkillList());

        // Add the skill to the employee's skill set
        employee.getSkillList().add(skill);

        // Save — Hibernate inserts a row into employee_skill
        employeeService.save(employee);

        LOGGER.debug("After add — skills: {}", employee.getSkillList());
        LOGGER.info("End testAddSkillToEmployee");
    }
}
