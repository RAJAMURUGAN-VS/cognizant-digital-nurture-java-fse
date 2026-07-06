package com.cognizant.springlearn;

import com.cognizant.springlearn.controller.CountryController;
import com.cognizant.springlearn.controller.EmployeeController;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * SpringLearnApplicationTests — MockMVC tests for all REST endpoints.
 *
 * Covers:
 *   GET/POST/PUT/DELETE for Country
 *   GET/POST/PUT/DELETE for Employee
 *   Validation error scenarios (400 Bad Request)
 *   HttpMessageNotReadable (wrong field type)
 *   EmployeeNotFoundException (404)
 *
 * Run: mvn clean test
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SpringLearnApplicationTests {

    @Autowired private CountryController  countryController;
    @Autowired private EmployeeController employeeController;
    @Autowired private MockMvc mvc;

    // ---------------------------------------------------------------
    // Context loads
    // ---------------------------------------------------------------

    @Test @Order(1)
    @DisplayName("Context loads — controllers present")
    void contextLoads() {
        assertNotNull(countryController);
        assertNotNull(employeeController);
    }

    // ================================================================
    // COUNTRY TESTS
    // ================================================================

    // ---------------------------------------------------------------
    // GET /countries
    // ---------------------------------------------------------------

    @Test @Order(2)
    @DisplayName("GET /countries returns 200 and JSON array")
    void getAllCountries_returns200() throws Exception {
        mvc.perform(get("/countries"))
           .andExpect(status().isOk())
           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("$").isArray())
           .andExpect(jsonPath("$.length()").value(4));
    }

    // ---------------------------------------------------------------
    // GET /countries/{code}
    // ---------------------------------------------------------------

    @Test @Order(3)
    @DisplayName("GET /countries/IN returns India")
    void getCountry_IN_returnsIndia() throws Exception {
        mvc.perform(get("/countries/IN"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.code").value("IN"))
           .andExpect(jsonPath("$.name").value("India"));
    }

    @Test @Order(4)
    @DisplayName("GET /countries/in (lowercase) returns India — case-insensitive")
    void getCountry_lowercaseIn_returnsIndia() throws Exception {
        mvc.perform(get("/countries/in"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.code").value("IN"));
    }

    @Test @Order(5)
    @DisplayName("GET /countries/ZZ returns 404 Not Found")
    void getCountry_notFound_returns404() throws Exception {
        mvc.perform(get("/countries/ZZ"))
           .andExpect(status().isNotFound());
    }

    // ---------------------------------------------------------------
    // POST /countries  — valid
    // ---------------------------------------------------------------

    @Test @Order(6)
    @DisplayName("POST /countries with valid body returns 201 Created")
    void addCountry_valid_returns201() throws Exception {
        String json = "{\"code\":\"AU\",\"name\":\"Australia\"}";
        mvc.perform(post("/countries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
           .andExpect(status().isCreated())
           .andExpect(jsonPath("$.code").value("AU"))
           .andExpect(jsonPath("$.name").value("Australia"));
    }

    // ---------------------------------------------------------------
    // POST /countries  — validation failures (400 Bad Request)
    // ---------------------------------------------------------------

    @Test @Order(7)
    @DisplayName("POST /countries with 1-char code returns 400 + error message")
    void addCountry_invalidCode_1char_returns400() throws Exception {
        // Country code "I" has length 1 — violates @Size(min=2,max=2)
        // GlobalExceptionHandler.handleMethodArgumentNotValid() intercepts.
        // CountryController.addCountry() is NOT called.
        String json = "{\"code\":\"I\",\"name\":\"India\"}";
        ResultActions actions = mvc.perform(post("/countries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        actions.andExpect(status().isBadRequest());
        actions.andExpect(jsonPath("$.status").value(400));
        actions.andExpect(jsonPath("$.errors").isArray());
        actions.andExpect(jsonPath("$.errors[0]")
                .value("Country code should be 2 characters"));
    }

    @Test @Order(8)
    @DisplayName("POST /countries with null code returns 400")
    void addCountry_nullCode_returns400() throws Exception {
        String json = "{\"name\":\"India\"}";
        mvc.perform(post("/countries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$.errors").isArray());
    }

    @Test @Order(9)
    @DisplayName("POST /countries with 3-char code returns 400")
    void addCountry_3charCode_returns400() throws Exception {
        String json = "{\"code\":\"IND\",\"name\":\"India\"}";
        mvc.perform(post("/countries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
           .andExpect(status().isBadRequest());
    }

    // ---------------------------------------------------------------
    // PUT /countries  — update
    // ---------------------------------------------------------------

    @Test @Order(10)
    @DisplayName("PUT /countries updates country name")
    void updateCountry_valid_returns200() throws Exception {
        // First add a country to update
        String addJson = "{\"code\":\"NZ\",\"name\":\"New Zealand\"}";
        mvc.perform(post("/countries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(addJson))
           .andExpect(status().isCreated());

        // Now update it
        String updateJson = "{\"code\":\"NZ\",\"name\":\"New Zealand Updated\"}";
        mvc.perform(put("/countries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.name").value("New Zealand Updated"));
    }

    // ---------------------------------------------------------------
    // DELETE /countries/{code}
    // ---------------------------------------------------------------

    @Test @Order(11)
    @DisplayName("DELETE /countries/AU returns 204 No Content")
    void deleteCountry_valid_returns204() throws Exception {
        // Add first
        String addJson = "{\"code\":\"XX\",\"name\":\"Test Country\"}";
        mvc.perform(post("/countries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(addJson))
           .andExpect(status().isCreated());

        // Delete
        mvc.perform(delete("/countries/XX"))
           .andExpect(status().isNoContent());

        // Verify deleted
        mvc.perform(get("/countries/XX"))
           .andExpect(status().isNotFound());
    }

    // ================================================================
    // EMPLOYEE TESTS
    // ================================================================

    // ---------------------------------------------------------------
    // GET /employees
    // ---------------------------------------------------------------

    @Test @Order(12)
    @DisplayName("GET /employees returns 200 and JSON array")
    void getAllEmployees_returns200() throws Exception {
        mvc.perform(get("/employees"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$").isArray())
           .andExpect(jsonPath("$.length()").value(4));
    }

    // ---------------------------------------------------------------
    // GET /employees/{id}
    // ---------------------------------------------------------------

    @Test @Order(13)
    @DisplayName("GET /employees/1 returns Alice Johnson")
    void getEmployeeById_returns_Alice() throws Exception {
        mvc.perform(get("/employees/1"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.name").value("Alice Johnson"))
           .andExpect(jsonPath("$.department.name").value("Engineering"));
    }

    @Test @Order(14)
    @DisplayName("GET /employees/99 returns 404 Not Found")
    void getEmployeeById_notFound() throws Exception {
        mvc.perform(get("/employees/99"))
           .andExpect(status().isNotFound());
    }

    // ---------------------------------------------------------------
    // PUT /employees  — validation error scenarios
    // ---------------------------------------------------------------

    @Test @Order(15)
    @DisplayName("PUT /employees with blank name returns 400")
    void updateEmployee_blankName_returns400() throws Exception {
        String json = "{"
            + "\"id\":1,"
            + "\"name\":\"\","          // @NotBlank violation
            + "\"email\":\"alice@example.com\","
            + "\"salary\":85000.0,"
            + "\"permanent\":true,"
            + "\"dateOfBirth\":\"15/03/1990\","
            + "\"department\":{\"id\":1,\"name\":\"Engineering\"},"
            + "\"skills\":[{\"id\":1,\"name\":\"Java\"}]"
            + "}";

        mvc.perform(put("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$.errors").isArray());
    }

    @Test @Order(16)
    @DisplayName("PUT /employees with negative salary returns 400")
    void updateEmployee_negativeSalary_returns400() throws Exception {
        String json = "{"
            + "\"id\":1,"
            + "\"name\":\"Alice\","
            + "\"email\":\"alice@example.com\","
            + "\"salary\":-100.0,"      // @Min(0) violation
            + "\"permanent\":true,"
            + "\"dateOfBirth\":\"15/03/1990\","
            + "\"department\":{\"id\":1,\"name\":\"Engineering\"},"
            + "\"skills\":[{\"id\":1,\"name\":\"Java\"}]"
            + "}";

        mvc.perform(put("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$.errors").isArray());
    }

    @Test @Order(17)
    @DisplayName("PUT /employees with string id returns 400 — HttpMessageNotReadable")
    void updateEmployee_stringId_returns400() throws Exception {
        // "id":"abc" — string where int is expected
        // Jackson throws InvalidFormatException
        // → GlobalExceptionHandler.handleHttpMessageNotReadable()
        String json = "{"
            + "\"id\":\"abc\","         // WRONG TYPE — triggers handleHttpMessageNotReadable
            + "\"name\":\"Alice\","
            + "\"email\":\"alice@example.com\","
            + "\"salary\":85000.0,"
            + "\"permanent\":true,"
            + "\"dateOfBirth\":\"15/03/1990\","
            + "\"department\":{\"id\":1,\"name\":\"Engineering\"},"
            + "\"skills\":[{\"id\":1,\"name\":\"Java\"}]"
            + "}";

        mvc.perform(put("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$.message").value("Incorrect format for field 'id'"));
    }

    // ---------------------------------------------------------------
    // PUT /employees  — valid update
    // ---------------------------------------------------------------

    @Test @Order(18)
    @DisplayName("PUT /employees valid update returns 200")
    void updateEmployee_valid_returns200() throws Exception {
        String json = "{"
            + "\"id\":1,"
            + "\"name\":\"Alice Johnson Updated\","
            + "\"email\":\"alice@example.com\","
            + "\"salary\":95000.0,"
            + "\"permanent\":true,"
            + "\"dateOfBirth\":\"15/03/1990\","
            + "\"department\":{\"id\":1,\"name\":\"Engineering\"},"
            + "\"skills\":[{\"id\":1,\"name\":\"Java\"}]"
            + "}";

        mvc.perform(put("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
           .andExpect(status().isOk());

        // Verify the update was applied
        mvc.perform(get("/employees/1"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.name").value("Alice Johnson Updated"))
           .andExpect(jsonPath("$.salary").value(95000.0));
    }

    // ---------------------------------------------------------------
    // DELETE /employees/{id}
    // ---------------------------------------------------------------

    @Test @Order(19)
    @DisplayName("DELETE /employees/{id} removes employee and returns 204")
    void deleteEmployee_valid_returns204() throws Exception {
        // Add a new employee first
        String addJson = "{"
            + "\"id\":99,"
            + "\"name\":\"Temp Employee\","
            + "\"email\":\"temp@example.com\","
            + "\"salary\":50000.0,"
            + "\"permanent\":false,"
            + "\"dateOfBirth\":\"01/01/2000\","
            + "\"department\":{\"id\":1,\"name\":\"Engineering\"},"
            + "\"skills\":[{\"id\":1,\"name\":\"Java\"}]"
            + "}";
        mvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(addJson))
           .andExpect(status().isCreated());

        // Get the auto-assigned id (should be 5)
        mvc.perform(get("/employees"))
           .andExpect(status().isOk());

        // Delete by id=5
        mvc.perform(delete("/employees/5"))
           .andExpect(status().isNoContent());

        // Verify deleted
        mvc.perform(get("/employees/5"))
           .andExpect(status().isNotFound());
    }

    @Test @Order(20)
    @DisplayName("DELETE /employees/99 returns 404 Not Found")
    void deleteEmployee_notFound_returns404() throws Exception {
        mvc.perform(delete("/employees/99"))
           .andExpect(status().isNotFound());
    }
}
