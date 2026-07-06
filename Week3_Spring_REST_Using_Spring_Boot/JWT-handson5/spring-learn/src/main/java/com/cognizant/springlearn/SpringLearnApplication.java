package com.cognizant.springlearn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SpringLearnApplication — Spring Boot entry point for JWT hands-on.
 *
 * After startup, test with these curl commands in sequence:
 *
 * 1. WITHOUT credentials → 401 Unauthorized:
 *    curl -s http://localhost:8083/countries
 *
 * 2. Basic Auth (wrong password) → 401 Unauthorized:
 *    curl -s -u user:wrongpwd http://localhost:8083/countries
 *
 * 3. Basic Auth (correct, wrong role for /countries) → 403 Forbidden:
 *    curl -s -u admin:pwd http://localhost:8083/countries
 *
 * 4. Get JWT token via Basic Auth:
 *    curl -s -u user:pwd http://localhost:8083/authenticate
 *    → {"token":"eyJhbGciOiJIUzI1NiJ9..."}
 *
 * 5. Use JWT Bearer token to access /countries:
 *    curl -s -H "Authorization: Bearer <TOKEN>" http://localhost:8083/countries
 *    → [{"code":"IN","name":"India"},...]
 *
 * 6. Tampered token → 401 Unauthorized:
 *    curl -s -H "Authorization: Bearer invalid.token.here" http://localhost:8083/countries
 */
@SpringBootApplication
public class SpringLearnApplication {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(SpringLearnApplication.class);

    public static void main(String[] args) {
        LOGGER.info("START SpringLearnApplication");
        SpringApplication.run(SpringLearnApplication.class, args);
        LOGGER.info("END SpringLearnApplication — server started on port 8083");
    }
}
