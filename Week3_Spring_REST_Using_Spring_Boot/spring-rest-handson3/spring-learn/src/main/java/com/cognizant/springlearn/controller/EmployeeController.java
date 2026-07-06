package com.cognizant.springlearn.controller;

import com.cognizant.springlearn.model.Employee;
import com.cognizant.springlearn.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * EmployeeController — REST controller for employee endpoints.
 *
 * @RestController — combines @Controller + @ResponseBody.
 *   Each method's return value is written to the HTTP response body as JSON.
 *   Jackson (bundled with spring-boot-starter-web) auto-serialises Employee
 *   objects and List<Employee> to JSON.
 *
 * @CrossOrigin — allows the Angular frontend (running on port 4200)
 *   to call these REST endpoints from a different origin.
 *   Without this, browsers block cross-origin requests (CORS policy).
 *
 * Endpoints:
 *   GET /employees      — returns all employees as JSON array
 *   GET /employees/{id} — returns a single employee by id
 *
 * Architecture: Request → DispatcherServlet → EmployeeController
 *                       → EmployeeService → EmployeeDao → employee.xml
 *
 * Test with Postman:
 *   GET http://localhost:8083/employees
 *   GET http://localhost:8083/employees/1
 */
@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class EmployeeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    public EmployeeController() {
        LOGGER.debug("Inside EmployeeController Constructor.");
    }

    /**
     * GET /employees — returns all employees as a JSON array.
     *
     * @GetMapping("/employees") — maps HTTP GET /employees to this method.
     *
     * JSON Response:
     * [
     *   {"id":1,"name":"Alice Johnson","email":"alice@example.com",
     *    "salary":85000.0,"permanent":true,
     *    "department":{"id":1,"name":"Engineering"},
     *    "skills":[{"id":1,"name":"Java"},...]},
     *   ...
     * ]
     *
     * Verify in Postman:
     *   1. GET http://localhost:8083/employees
     *   2. Click Headers tab → Content-Type: application/json
     *   3. Check Body tab → JSON array of employees
     */
    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        LOGGER.info("START getAllEmployees REST endpoint");
        List<Employee> employees = employeeService.getAllEmployees();
        LOGGER.debug("Returning {} employees", employees.size());
        LOGGER.info("END getAllEmployees REST endpoint");
        return employees;
    }

    /**
     * GET /employees/{id} — returns a single employee by id.
     *
     * @PathVariable — extracts the {id} from the URL path.
     *   GET /employees/1 → id = 1 → returns Alice Johnson
     *   GET /employees/99 → id = 99 → returns 404 Not Found
     *
     * ResponseEntity allows us to set custom HTTP status codes:
     *   200 OK  — employee found
     *   404 Not Found — employee not found
     */
    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable int id) {
        LOGGER.info("START getEmployeeById({})", id);
        Employee employee = employeeService.getEmployeeById(id);
        if (employee == null) {
            LOGGER.warn("Employee id={} not found", id);
            return ResponseEntity.notFound().build();
        }
        LOGGER.debug("Returning: {}", employee);
        LOGGER.info("END getEmployeeById");
        return ResponseEntity.ok(employee);
    }
}
