package com.cognizant.springlearn;

import com.cognizant.springlearn.controller.CountryController;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * SpringLearnApplicationTests — MockMVC tests for all REST endpoints.
 *
 * @SpringBootTest — loads the full Spring application context.
 *   This means all @RestController and @Service beans are registered,
 *   just as they would be when the app starts normally.
 *
 * @AutoConfigureMockMvc — auto-configures MockMvc.
 *   MockMvc lets us fire HTTP requests without starting a real server.
 *   The DispatcherServlet is invoked in-memory, so tests run faster.
 *
 * MockMvc flow:
 *   mvc.perform(get("/country"))   — builds a mock HTTP GET /country request
 *   .andExpect(status().isOk())    — asserts HTTP 200 response status
 *   .andExpect(jsonPath("$.code")) — asserts JSON field "code" exists in body
 *   .andExpect(jsonPath("$.code").value("IN")) — asserts value of "code" is "IN"
 *
 * Run from command line: mvn clean test
 * Run from Eclipse: right-click SpringLearnApplicationTests > Run As > JUnit Test
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SpringLearnApplicationTests {

    // ---------------------------------------------------------------
    // MockMVC - Test loading CountryController
    // ---------------------------------------------------------------

    /**
     * @Autowired — Spring injects the CountryController bean.
     * If autowiring succeeds, the controller was loaded by Spring.
     * Used in contextLoads() to verify the bean is present.
     */
    @Autowired
    private CountryController countryController;

    /**
     * MockMvc — the test HTTP client.
     * @AutoConfigureMockMvc creates and injects this automatically.
     * We use it to perform HTTP requests and assert responses.
     */
    @Autowired
    private MockMvc mvc;

    // ---------------------------------------------------------------
    // Test 1: Context loads + CountryController is registered
    // ---------------------------------------------------------------

    /**
     * Verifies:
     *   1. Spring application context loads without errors.
     *   2. CountryController bean is present in the context.
     *
     * If CountryController is not found, Spring throws NoSuchBeanDefinitionException
     * and this test fails — telling us the controller wasn't scanned.
     */
    @Test
    @Order(1)
    @DisplayName("Context loads and CountryController bean is available")
    void contextLoads() {
        assertNotNull(countryController,
                "CountryController should be loaded by Spring context");
    }

    // ---------------------------------------------------------------
    // Test 2: GET /hello
    // ---------------------------------------------------------------

    /**
     * Tests HelloController.sayHello().
     * Expected: HTTP 200, body = "Hello World!!"
     */
    @Test
    @Order(2)
    @DisplayName("GET /hello returns 'Hello World!!' with status 200")
    void testSayHello() throws Exception {
        ResultActions actions = mvc.perform(get("/hello"));

        // Assert HTTP status 200 OK
        actions.andExpect(status().isOk());

        // Assert response body is "Hello World!!"
        actions.andExpect(content().string("Hello World!!"));
    }

    // ---------------------------------------------------------------
    // Test 3: GET /country — returns India
    // ---------------------------------------------------------------

    /**
     * MockMVC - Test get country service (full step-by-step as per hands-on).
     *
     * Step 1: Perform GET /country
     * Step 2: Assert HTTP 200 OK
     * Step 3: Assert JSON field "code" exists
     * Step 4: Assert "code" value is "IN"
     * Step 5: Assert "name" exists
     * Step 6: Assert "name" value is "India"
     *
     * jsonPath("$.code"):
     *   $    — root of the JSON object
     *   .code — the "code" field
     *   For arrays: $[0].code — first element's code field
     */
    @Test
    @Order(3)
    @DisplayName("GET /country returns India with code=IN and name=India")
    void testGetCountry() throws Exception {
        ResultActions actions = mvc.perform(get("/country"));

        // Step 2: Assert HTTP 200 OK
        actions.andExpect(status().isOk());

        // Step 3: Assert "code" field exists in JSON response
        actions.andExpect(jsonPath("$.code").exists());

        // Step 4: Assert "code" value is "IN"
        actions.andExpect(jsonPath("$.code").value("IN"));

        // Step 5: Assert "name" field exists in JSON response
        actions.andExpect(jsonPath("$.name").exists());

        // Step 6: Assert "name" value is "India"
        actions.andExpect(jsonPath("$.name").value("India"));
    }

    // ---------------------------------------------------------------
    // Test 4: GET /countries — returns all four countries
    // ---------------------------------------------------------------

    /**
     * Tests CountryController.getAllCountries().
     * Expected: HTTP 200, JSON array with 4 elements.
     *
     * jsonPath("$") refers to the root of the response.
     * jsonPath("$.length()") checks array size.
     * jsonPath("$[0].code") checks first element's code.
     */
    @Test
    @Order(4)
    @DisplayName("GET /countries returns JSON array of 4 countries")
    void testGetAllCountries() throws Exception {
        ResultActions actions = mvc.perform(get("/countries"));

        actions.andExpect(status().isOk());

        // Assert the response is a JSON array with 4 elements
        actions.andExpect(jsonPath("$.length()").value(4));

        // Assert at least the first element has a "code" field
        actions.andExpect(jsonPath("$[0].code").exists());
        actions.andExpect(jsonPath("$[0].name").exists());
    }

    // ---------------------------------------------------------------
    // Test 5: GET /countries/{code} — valid code (case-insensitive)
    // ---------------------------------------------------------------

    /**
     * Tests CountryController.getCountry() with a valid lowercase code "in".
     * Service performs case-insensitive match → returns India.
     * Expected: HTTP 200, code="IN", name="India"
     */
    @Test
    @Order(5)
    @DisplayName("GET /countries/in (lowercase) returns India with status 200")
    void testGetCountryByCode_validLowercase() throws Exception {
        ResultActions actions = mvc.perform(get("/countries/in"));

        actions.andExpect(status().isOk());
        actions.andExpect(jsonPath("$.code").value("IN"));
        actions.andExpect(jsonPath("$.name").value("India"));
    }

    /**
     * Tests GET /countries/US (uppercase).
     * Expected: HTTP 200, United States.
     */
    @Test
    @Order(6)
    @DisplayName("GET /countries/US returns United States with status 200")
    void testGetCountryByCode_validUppercase() throws Exception {
        ResultActions actions = mvc.perform(get("/countries/US"));

        actions.andExpect(status().isOk());
        actions.andExpect(jsonPath("$.code").value("US"));
        actions.andExpect(jsonPath("$.name").value("United States"));
    }

    /**
     * Tests GET /countries/De (mixed case).
     * Expected: HTTP 200, Germany.
     */
    @Test
    @Order(7)
    @DisplayName("GET /countries/De (mixed case) returns Germany with status 200")
    void testGetCountryByCode_mixedCase() throws Exception {
        ResultActions actions = mvc.perform(get("/countries/De"));

        actions.andExpect(status().isOk());
        actions.andExpect(jsonPath("$.code").value("DE"));
        actions.andExpect(jsonPath("$.name").value("Germany"));
    }

    // ---------------------------------------------------------------
    // Test 6: GET /countries/{code} — invalid code → 404 Not Found
    // ---------------------------------------------------------------

    /**
     * MockMVC - Test get country service for exceptional scenario.
     *
     * When code "az" is not found:
     *   CountryService throws CountryNotFoundException.
     *   @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Country not found")
     *   Spring MVC maps this to HTTP 404 with reason "Country not found".
     *
     * We test:
     *   status().isNotFound()          → HTTP 404
     *   status().reason("Country not found") → reason phrase in response
     *
     * curl test: curl -i http://localhost:8083/countries/az
     */
    @Test
    @Order(8)
    @DisplayName("GET /countries/az returns 404 Not Found with reason 'Country not found'")
    void testGetCountryException_notFound() throws Exception {
        ResultActions actions = mvc.perform(get("/countries/az"));

        // Assert HTTP 404 Not Found
        actions.andExpect(status().isNotFound());

        // Assert the reason phrase matches @ResponseStatus reason
        actions.andExpect(status().reason("Country not found"));
    }

    /**
     * Another invalid code — "xx".
     */
    @Test
    @Order(9)
    @DisplayName("GET /countries/xx returns 404 Not Found")
    void testGetCountryException_anotherInvalidCode() throws Exception {
        ResultActions actions = mvc.perform(get("/countries/xx"));

        actions.andExpect(status().isNotFound());
        actions.andExpect(status().reason("Country not found"));
    }

    // ---------------------------------------------------------------
    // Test 7: Content-Type header verification
    // ---------------------------------------------------------------

    /**
     * Verifies the Content-Type response header for JSON endpoints.
     * Spring Boot sets Content-Type: application/json automatically
     * when a @RestController method returns an object or List.
     */
    @Test
    @Order(10)
    @DisplayName("GET /country response Content-Type is application/json")
    void testGetCountry_contentTypeIsJson() throws Exception {
        mvc.perform(get("/country"))
           .andExpect(status().isOk())
           .andExpect(content().contentType("application/json"));
    }

    /**
     * Verifies that GET /hello returns Content-Type text/plain.
     * A plain String from @RestController is written as text/plain.
     */
    @Test
    @Order(11)
    @DisplayName("GET /hello response Content-Type is text/plain")
    void testSayHello_contentTypeIsText() throws Exception {
        mvc.perform(get("/hello"))
           .andExpect(status().isOk())
           .andExpect(content().contentTypeCompatibleWith("text/plain"));
    }

    // ---------------------------------------------------------------
    // Test 8: GET /countries — verify all country codes present
    // ---------------------------------------------------------------

    /**
     * Verifies each of the four country codes exists in the countries list.
     * Uses jsonPath with array index expressions.
     */
    @Test
    @Order(12)
    @DisplayName("GET /countries contains IN, US, DE, JP codes")
    void testGetAllCountries_containsAllCodes() throws Exception {
        ResultActions actions = mvc.perform(get("/countries"));
        actions.andExpect(status().isOk());

        // Check that array contains an object with code "IN"
        actions.andExpect(jsonPath("$[?(@.code=='IN')]").exists());
        actions.andExpect(jsonPath("$[?(@.code=='US')]").exists());
        actions.andExpect(jsonPath("$[?(@.code=='DE')]").exists());
        actions.andExpect(jsonPath("$[?(@.code=='JP')]").exists());
    }
}
