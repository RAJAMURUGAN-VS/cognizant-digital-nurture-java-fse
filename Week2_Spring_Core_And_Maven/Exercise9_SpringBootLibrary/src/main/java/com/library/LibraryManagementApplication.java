package com.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Exercise 9: Spring Boot entry point.
 *
 * @SpringBootApplication is a convenience annotation that combines:
 *   - @Configuration       : marks this class as a source of bean definitions
 *   - @EnableAutoConfiguration : enables Spring Boot's auto-configuration mechanism
 *   - @ComponentScan       : scans com.library and sub-packages for components
 *
 * Run this class and then test the REST API at http://localhost:8080/api/books
 * H2 Console is available at http://localhost:8080/h2-console
 *   JDBC URL : jdbc:h2:mem:librarydb
 *   User     : sa
 *   Password : (empty)
 */
@SpringBootApplication
public class LibraryManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryManagementApplication.class, args);
    }
}
