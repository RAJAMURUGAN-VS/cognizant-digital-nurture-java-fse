package com.cognizant.springlearn.controller;

import com.cognizant.springlearn.model.Employee;
import com.cognizant.springlearn.service.EmployeeService;
import com.cognizant.springlearn.service.exception.EmployeeNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * EmployeeController — full CRUD REST controller for Employee.
 *
 * RESTful URL naming convention (all under /employees):
 *   GET    /employees        — get all
 *   GET    /employees/{id}   — get one by id
 *   POST   /employees        — create (body = JSON employee)
 *   PUT    /employees        — update (body = JSON employee with id)
 *   DELETE /employees/{id}   — delete by id
 *
 * Validation:
 *   @Valid on @RequestBody triggers Hibernate Validator on Employee bean.
 *   Cascades to @Valid Department and Skill nested objects.
 *   Failures caught by GlobalExceptionHandler.
 *
 * Postman test (PUT — update employee):
 *   Method: PUT
 *   URL: http://localhost:8083/employees
 *   Body (raw JSON):
 *   {
 *     "id": 1,
 *     "name": "Alice Updated",
 *     "email": "alice@example.com",
 *     "salary": 90000.0,
 *     "permanent": true,
 *     "dateOfBirth": "15/03/1990",
 *     "department": {"id": 1, "name": "Engineering"},
 *     "skills": [{"id": 1, "name": "Java"}]
 *   }
 *
 * Wrong type test (id as string → HttpMessageNotReadableException):
 *   {"id":"abc","name":"Alice","salary":90000,...}
 *   → GlobalExceptionHandler.handleHttpMessageNotReadable()
 *   → {"message":"Incorrect format for field 'id'"}
 */
@RestController
@RequestMapping("/employees")
@CrossOrigin(origins = "http://localhost:4200")
public class EmployeeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    public EmployeeController() {
        LOGGER.debug("Inside EmployeeController Constructor.");
    }

    // ---------------------------------------------------------------
    // GET /employees
    // ---------------------------------------------------------------
    @GetMapping
    public List<Employee> getAllEmployees() {
        LOGGER.info("START getAllEmployees");
        List<Employee> list = employeeService.getAllEmployees();
        LOGGER.debug("Returning {} employees", list.size());
        LOGGER.info("END getAllEmployees");
        return list;
    }

    // ---------------------------------------------------------------
    // GET /employees/{id}
    // ---------------------------------------------------------------
    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable int id) throws EmployeeNotFoundException {
        LOGGER.info("START getEmployeeById({})", id);
        Employee e = employeeService.getEmployeeById(id);
        LOGGER.debug("Returning: {}", e);
        LOGGER.info("END getEmployeeById");
        return e;
    }

    // ---------------------------------------------------------------
    // POST /employees  — create
    // ---------------------------------------------------------------
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee addEmployee(@RequestBody @Valid Employee employee) {
        LOGGER.info("START addEmployee");
        LOGGER.debug("Employee received: {}", employee);
        Employee added = employeeService.addEmployee(employee);
        LOGGER.debug("Employee added with id={}", added.getId());
        LOGGER.info("END addEmployee");
        return added;
    }

    // ---------------------------------------------------------------
    // PUT /employees  — update
    // ---------------------------------------------------------------

    /**
     * Updates an existing employee.
     *
     * @RequestBody @Valid — Jackson deserialises JSON + Hibernate validates.
     * If id in payload doesn't match any employee → EmployeeNotFoundException (404).
     *
     * Verify update: After PUT, call GET /employees to confirm change.
     */
    @PutMapping
    public void updateEmployee(@RequestBody @Valid Employee employee)
            throws EmployeeNotFoundException {
        LOGGER.info("START updateEmployee");
        LOGGER.debug("Employee to update: {}", employee);
        employeeService.updateEmployee(employee);
        LOGGER.info("END updateEmployee");
    }

    // ---------------------------------------------------------------
    // DELETE /employees/{id}
    // ---------------------------------------------------------------

    /**
     * Deletes an employee by id.
     * Returns 204 No Content on success.
     * Returns 404 Not Found if id doesn't exist.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable int id) throws EmployeeNotFoundException {
        LOGGER.info("START deleteEmployee({})", id);
        employeeService.deleteEmployee(id);
        LOGGER.info("END deleteEmployee");
    }
}
