package com.cognizant.springlearn.controller;

import com.cognizant.springlearn.model.Department;
import com.cognizant.springlearn.service.DepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * DepartmentController — REST controller for department endpoints.
 *
 * Verify in logs:
 *   When GET /departments is called, the following log lines appear:
 *     DepartmentController.getAllDepartments START
 *     DepartmentService.getAllDepartments START
 *     DepartmentDao.getAllDepartments START
 *   This confirms the full Controller → Service → DAO call chain.
 *
 * Test with Postman:
 *   GET http://localhost:8083/departments
 *   GET http://localhost:8083/departments/1
 *
 * JSON Response for GET /departments:
 * [
 *   {"id":1,"name":"Engineering"},
 *   {"id":2,"name":"Human Resources"},
 *   {"id":3,"name":"Finance"},
 *   {"id":4,"name":"Marketing"}
 * ]
 */
@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class DepartmentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentController.class);

    @Autowired
    private DepartmentService departmentService;

    public DepartmentController() {
        LOGGER.debug("Inside DepartmentController Constructor.");
    }

    /**
     * GET /departments — returns all departments as a JSON array.
     *
     * Verify in logs after calling this endpoint:
     *   Look for "DepartmentService.getAllDepartments" to confirm
     *   the service layer was invoked.
     */
    @GetMapping("/departments")
    public List<Department> getAllDepartments() {
        LOGGER.info("START getAllDepartments REST endpoint");
        List<Department> departments = departmentService.getAllDepartments();
        LOGGER.debug("Returning {} departments", departments.size());
        LOGGER.info("END getAllDepartments REST endpoint");
        return departments;
    }

    /**
     * GET /departments/{id} — returns a single department by id.
     *
     * @PathVariable — extracts {id} from the URL.
     *   GET /departments/1 → Engineering
     *   GET /departments/99 → 404 Not Found
     */
    @GetMapping("/departments/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable int id) {
        LOGGER.info("START getDepartmentById({})", id);
        Department dept = departmentService.getDepartmentById(id);
        if (dept == null) {
            LOGGER.warn("Department id={} not found", id);
            return ResponseEntity.notFound().build();
        }
        LOGGER.debug("Returning: {}", dept);
        LOGGER.info("END getDepartmentById");
        return ResponseEntity.ok(dept);
    }
}
