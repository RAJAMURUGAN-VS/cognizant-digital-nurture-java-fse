package com.cognizant.springlearn;

import com.cognizant.springlearn.controller.DepartmentController;
import com.cognizant.springlearn.controller.EmployeeController;
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
 * SpringLearnApplicationTests — MockMVC tests for Employee and Department REST services.
 *
 * Run from Eclipse : right-click > Run As > JUnit Test
 * Run from CLI     : mvn clean test
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SpringLearnApplicationTests {

    @Autowired
    private EmployeeController employeeController;

    @Autowired
    private DepartmentController departmentController;

    @Autowired
    private MockMvc mvc;

    // ---------------------------------------------------------------
    // Test 1: Context loads + controllers registered
    // ---------------------------------------------------------------

    @Test @Order(1)
    @DisplayName("Context loads — EmployeeController and DepartmentController beans present")
    void contextLoads() {
        assertNotNull(employeeController,   "EmployeeController should be loaded");
        assertNotNull(departmentController, "DepartmentController should be loaded");
    }

    // ---------------------------------------------------------------
    // Tests 2-5: GET /employees
    // ---------------------------------------------------------------

    @Test @Order(2)
    @DisplayName("GET /employees returns HTTP 200 OK")
    void getAllEmployees_returns200() throws Exception {
        mvc.perform(get("/employees"))
           .andExpect(status().isOk());
    }

    @Test @Order(3)
    @DisplayName("GET /employees returns JSON array")
    void getAllEmployees_returnsJsonArray() throws Exception {
        mvc.perform(get("/employees"))
           .andExpect(status().isOk())
           .andExpect(content().contentType("application/json"))
           .andExpect(jsonPath("$").isArray());
    }

    @Test @Order(4)
    @DisplayName("GET /employees returns 6 employees")
    void getAllEmployees_returns6Employees() throws Exception {
        mvc.perform(get("/employees"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.length()").value(6));
    }

    @Test @Order(5)
    @DisplayName("GET /employees first employee has id, name, email, department, skills")
    void getAllEmployees_firstEmployeeHasRequiredFields() throws Exception {
        mvc.perform(get("/employees"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].id").exists())
           .andExpect(jsonPath("$[0].name").exists())
           .andExpect(jsonPath("$[0].email").exists())
           .andExpect(jsonPath("$[0].department").exists())
           .andExpect(jsonPath("$[0].skills").exists())
           .andExpect(jsonPath("$[0].skills").isArray());
    }

    @Test @Order(6)
    @DisplayName("GET /employees first employee is Alice Johnson in Engineering")
    void getAllEmployees_firstEmployeeIsAlice() throws Exception {
        mvc.perform(get("/employees"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].name").value("Alice Johnson"))
           .andExpect(jsonPath("$[0].email").value("alice@example.com"))
           .andExpect(jsonPath("$[0].permanent").value(true))
           .andExpect(jsonPath("$[0].department.name").value("Engineering"));
    }

    // ---------------------------------------------------------------
    // Tests 7-9: GET /employees/{id}
    // ---------------------------------------------------------------

    @Test @Order(7)
    @DisplayName("GET /employees/1 returns Alice Johnson")
    void getEmployeeById_returns_Alice() throws Exception {
        mvc.perform(get("/employees/1"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(1))
           .andExpect(jsonPath("$.name").value("Alice Johnson"));
    }

    @Test @Order(8)
    @DisplayName("GET /employees/3 returns Carol White in HR")
    void getEmployeeById_returns_Carol() throws Exception {
        mvc.perform(get("/employees/3"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.name").value("Carol White"))
           .andExpect(jsonPath("$.department.name").value("Human Resources"))
           .andExpect(jsonPath("$.permanent").value(false));
    }

    @Test @Order(9)
    @DisplayName("GET /employees/99 returns 404 Not Found")
    void getEmployeeById_notFound_returns404() throws Exception {
        mvc.perform(get("/employees/99"))
           .andExpect(status().isNotFound());
    }

    // ---------------------------------------------------------------
    // Tests 10-13: GET /departments
    // ---------------------------------------------------------------

    @Test @Order(10)
    @DisplayName("GET /departments returns HTTP 200 OK")
    void getAllDepartments_returns200() throws Exception {
        mvc.perform(get("/departments"))
           .andExpect(status().isOk());
    }

    @Test @Order(11)
    @DisplayName("GET /departments returns JSON array of 4 departments")
    void getAllDepartments_returns4Departments() throws Exception {
        mvc.perform(get("/departments"))
           .andExpect(status().isOk())
           .andExpect(content().contentType("application/json"))
           .andExpect(jsonPath("$").isArray())
           .andExpect(jsonPath("$.length()").value(4));
    }

    @Test @Order(12)
    @DisplayName("GET /departments contains Engineering, HR, Finance, Marketing")
    void getAllDepartments_containsAllFour() throws Exception {
        ResultActions actions = mvc.perform(get("/departments"));
        actions.andExpect(status().isOk())
               .andExpect(jsonPath("$[?(@.name=='Engineering')]").exists())
               .andExpect(jsonPath("$[?(@.name=='Human Resources')]").exists())
               .andExpect(jsonPath("$[?(@.name=='Finance')]").exists())
               .andExpect(jsonPath("$[?(@.name=='Marketing')]").exists());
    }

    @Test @Order(13)
    @DisplayName("GET /departments/1 returns Engineering")
    void getDepartmentById_returnsEngineering() throws Exception {
        mvc.perform(get("/departments/1"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(1))
           .andExpect(jsonPath("$.name").value("Engineering"));
    }

    @Test @Order(14)
    @DisplayName("GET /departments/99 returns 404 Not Found")
    void getDepartmentById_notFound_returns404() throws Exception {
        mvc.perform(get("/departments/99"))
           .andExpect(status().isNotFound());
    }

    // ---------------------------------------------------------------
    // Test 15: Employee skills are populated
    // ---------------------------------------------------------------

    @Test @Order(15)
    @DisplayName("GET /employees/1 — Alice has Java, Spring Boot, SQL skills")
    void getEmployee_AliceHasCorrectSkills() throws Exception {
        mvc.perform(get("/employees/1"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.skills[?(@.name=='Java')]").exists())
           .andExpect(jsonPath("$.skills[?(@.name=='Spring Boot')]").exists())
           .andExpect(jsonPath("$.skills[?(@.name=='SQL')]").exists());
    }
}
