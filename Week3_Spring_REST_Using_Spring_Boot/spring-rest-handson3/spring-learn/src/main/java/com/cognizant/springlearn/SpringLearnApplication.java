package com.cognizant.springlearn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SpringLearnApplication — Spring Boot entry point.
 *
 * On startup:
 *   1. Spring scans com.cognizant.springlearn for @RestController,
 *      @Service, @Repository beans.
 *   2. EmployeeDao and DepartmentDao constructors load employee.xml,
 *      populating EMPLOYEE_LIST and DEPARTMENT_LIST.
 *   3. Embedded Tomcat starts on port 8083.
 *
 * Available REST endpoints after startup:
 *   GET http://localhost:8083/employees          — all employees (JSON array)
 *   GET http://localhost:8083/employees/{id}     — single employee
 *   GET http://localhost:8083/departments        — all departments (JSON array)
 *   GET http://localhost:8083/departments/{id}   — single department
 */
@SpringBootApplication
public class SpringLearnApplication {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(SpringLearnApplication.class);

    public static void main(String[] args) {
        LOGGER.info("START SpringLearnApplication main");
        SpringApplication.run(SpringLearnApplication.class, args);
        LOGGER.info("END SpringLearnApplication main — server started on port 8083");
    }
}
