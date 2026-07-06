package com.cognizant.springlearn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SpringLearnApplication — Spring Boot entry point.
 *
 * @SpringBootApplication combines:
 *   @Configuration          — marks this class as a bean source
 *   @EnableAutoConfiguration — auto-configures Spring beans from classpath
 *   @ComponentScan          — scans com.cognizant.springlearn for @RestController,
 *                             @Service, @Component, @Repository beans
 *
 * SpringApplication.run():
 *   1. Creates the Spring ApplicationContext.
 *   2. Registers all @RestController, @Service beans.
 *   3. Starts embedded Tomcat on port 8083 (from application.properties).
 *   4. Registers the DispatcherServlet to handle all incoming HTTP requests.
 *
 * After startup, the following REST endpoints are available:
 *   GET http://localhost:8083/hello           — Hello World
 *   GET http://localhost:8083/country         — India country details
 *   GET http://localhost:8083/countries       — All four countries
 *   GET http://localhost:8083/countries/{code}— Country by code
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
