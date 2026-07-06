package com.example.ems;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * EmployeeManagementSystemApplication — Spring Boot entry point.
 *
 * @SpringBootApplication combines:
 *   @Configuration          — bean source
 *   @EnableAutoConfiguration — Spring Boot auto-config (DataSource, JPA, Web)
 *   @ComponentScan          — scans com.example.ems and sub-packages
 *
 * Covers Exercises 1-10 via REST API and service/repository layers.
 *
 * After starting, access:
 *   API  : http://localhost:8080/api/employees
 *        : http://localhost:8080/api/departments
 *   H2   : http://localhost:8080/h2-console
 *          JDBC URL: jdbc:h2:mem:testdb  Username: sa  Password: password
 */
@SpringBootApplication
public class EmployeeManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeManagementSystemApplication.class, args);
    }
}
