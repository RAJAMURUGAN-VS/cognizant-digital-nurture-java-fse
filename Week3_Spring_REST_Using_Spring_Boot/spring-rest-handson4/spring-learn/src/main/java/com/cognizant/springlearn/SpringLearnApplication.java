package com.cognizant.springlearn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SpringLearnApplication — Spring Boot entry point.
 *
 * Endpoints available after startup (port 8083):
 *
 * COUNTRY (GET/POST/PUT/DELETE /countries):
 *   GET    http://localhost:8083/countries
 *   GET    http://localhost:8083/countries/IN
 *   POST   http://localhost:8083/countries   body: {"code":"XX","name":"Test"}
 *   PUT    http://localhost:8083/countries   body: {"code":"IN","name":"Bharat"}
 *   DELETE http://localhost:8083/countries/IN
 *
 * EMPLOYEE (GET/POST/PUT/DELETE /employees):
 *   GET    http://localhost:8083/employees
 *   GET    http://localhost:8083/employees/1
 *   POST   http://localhost:8083/employees   body: {employee JSON}
 *   PUT    http://localhost:8083/employees   body: {employee JSON with id}
 *   DELETE http://localhost:8083/employees/1
 */
@SpringBootApplication
public class SpringLearnApplication {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(SpringLearnApplication.class);

    public static void main(String[] args) {
        LOGGER.info("START SpringLearnApplication");
        SpringApplication.run(SpringLearnApplication.class, args);
        LOGGER.info("END SpringLearnApplication — server on port 8083");
    }
}
