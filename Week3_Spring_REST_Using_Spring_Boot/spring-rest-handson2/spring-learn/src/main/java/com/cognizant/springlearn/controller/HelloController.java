package com.cognizant.springlearn.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HelloController — REST controller for the Hello World endpoint.
 *
 * REST - Hello World RESTful Web Service
 *
 * @RestController combines:
 *   @Controller   — marks this class as a Spring MVC controller.
 *   @ResponseBody — return value of each method is written directly
 *                   to the HTTP response body (no view resolution).
 *
 * How a REST request flows:
 *   Browser/Postman → HTTP GET /hello
 *     → DispatcherServlet (front controller)
 *       → HelloController.sayHello()
 *         → "Hello World!!" written to HTTP response body
 *           → Browser/Postman receives text/plain response
 *
 * @GetMapping — shortcut for @RequestMapping(method = RequestMethod.GET).
 *   Maps HTTP GET requests to this method.
 *
 * Sample request:  GET http://localhost:8083/hello
 * Sample response: Hello World!!
 */
@RestController
public class HelloController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);

    /**
     * Returns a plain text "Hello World!!" string.
     *
     * HTTP Response:
     *   Status: 200 OK
     *   Content-Type: text/plain;charset=UTF-8
     *   Body: Hello World!!
     *
     * In Chrome Network tab / Postman Headers:
     *   Content-Type: text/plain;charset=UTF-8
     *   — Browser renders this as plain text.
     *   — If it were application/json, browser would show JSON.
     */
    @GetMapping("/hello")
    public String sayHello() {
        LOGGER.info("START sayHello");
        String message = "Hello World!!";
        LOGGER.debug("Returning message: {}", message);
        LOGGER.info("END sayHello");
        return message;
    }
}
