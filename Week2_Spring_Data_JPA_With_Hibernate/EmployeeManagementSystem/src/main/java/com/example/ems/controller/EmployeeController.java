package com.example.ems.controller;

import com.example.ems.entity.Employee;
import com.example.ems.projection.EmployeeNameEmailProjection;
import com.example.ems.projection.EmployeeSummaryDTO;
import com.example.ems.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * EmployeeController — Exercise 4, 5, 6, 8, 10.
 *
 * REST API for Employee CRUD, search, pagination/sorting, projections, batch.
 *
 * Base URL: /api/employees
 *
 * Endpoints:
 *   GET    /api/employees                  — list all (Exercise 4)
 *   GET    /api/employees/{id}             — get by id
 *   POST   /api/employees                  — create
 *   PUT    /api/employees/{id}             — update
 *   DELETE /api/employees/{id}             — delete
 *   GET    /api/employees/search?name=     — search by name (Exercise 5)
 *   GET    /api/employees/email?value=     — find by email (Exercise 5)
 *   GET    /api/employees/department/{id}  — by department (Exercise 5)
 *   GET    /api/employees/with-department  — JOIN FETCH (Exercise 5)
 *   GET    /api/employees/paginated        — pagination + sort (Exercise 6)
 *   GET    /api/employees/projection       — interface projection (Exercise 8)
 *   GET    /api/employees/summary          — DTO projection (Exercise 8)
 *   POST   /api/employees/batch/{count}    — batch insert (Exercise 10)
 */
@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    // ---------------------------------------------------------------
    // Exercise 4: Basic CRUD endpoints
    // ---------------------------------------------------------------

    @GetMapping
    public ResponseEntity<List<Employee>> getAll() {
        return ResponseEntity.ok(employeeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getById(@PathVariable Long id) {
        return employeeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Employee> create(@RequestBody Employee employee) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(employeeService.create(employee));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> update(@PathVariable Long id,
                                           @RequestBody Employee employee) {
        try {
            return ResponseEntity.ok(employeeService.update(id, employee));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ---------------------------------------------------------------
    // Exercise 5: Custom query endpoints
    // ---------------------------------------------------------------

    /** Search by name keyword — GET /api/employees/search?name=alice */
    @GetMapping("/search")
    public ResponseEntity<List<Employee>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(employeeService.findByNameContaining(name));
    }

    /** Find by email — GET /api/employees/email?value=alice@example.com */
    @GetMapping("/email")
    public ResponseEntity<Employee> findByEmail(@RequestParam String value) {
        return employeeService.findByEmail(value)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** Find by department id — GET /api/employees/department/1 */
    @GetMapping("/department/{deptId}")
    public ResponseEntity<List<Employee>> findByDepartment(@PathVariable Long deptId) {
        return ResponseEntity.ok(employeeService.findByDepartmentId(deptId));
    }

    /** All employees with department (JOIN FETCH) — GET /api/employees/with-department */
    @GetMapping("/with-department")
    public ResponseEntity<List<Employee>> findWithDepartment() {
        return ResponseEntity.ok(employeeService.findAllWithDepartment());
    }

    /** Bulk update department — PUT /api/employees/bulk-update-dept?from=1&to=2 */
    @PutMapping("/bulk-update-dept")
    public ResponseEntity<String> bulkUpdateDept(@RequestParam Long from,
                                                  @RequestParam Long to) {
        int count = employeeService.bulkUpdateDepartment(from, to);
        return ResponseEntity.ok(count + " employees updated from dept " + from + " to " + to);
    }

    // ---------------------------------------------------------------
    // Exercise 6: Pagination and Sorting
    // ---------------------------------------------------------------

    /**
     * Paginated + sorted employee list.
     * GET /api/employees/paginated?page=0&size=2&sortBy=name&sortDir=asc
     *
     * Response includes: content, totalElements, totalPages, currentPage.
     */
    @GetMapping("/paginated")
    public ResponseEntity<Page<Employee>> getPaginated(
            @RequestParam(defaultValue = "0")    int    page,
            @RequestParam(defaultValue = "5")    int    size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc")  String sortDir) {
        return ResponseEntity.ok(
                employeeService.findPaginated(page, size, sortBy, sortDir));
    }

    /**
     * Paginated employees by department.
     * GET /api/employees/paginated/department/1?page=0&size=3
     */
    @GetMapping("/paginated/department/{deptId}")
    public ResponseEntity<Page<Employee>> getPaginatedByDept(
            @PathVariable Long deptId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(
                employeeService.findByDepartmentPaginated(deptId, page, size));
    }

    /**
     * Paginated search by name.
     * GET /api/employees/paginated/search?name=ali&page=0&size=2
     */
    @GetMapping("/paginated/search")
    public ResponseEntity<Page<Employee>> searchPaginated(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(employeeService.searchByName(name, page, size));
    }

    // ---------------------------------------------------------------
    // Exercise 8: Projections
    // ---------------------------------------------------------------

    /** Interface-based projection — GET /api/employees/projection */
    @GetMapping("/projection")
    public ResponseEntity<List<EmployeeNameEmailProjection>> getProjection() {
        return ResponseEntity.ok(employeeService.findAllNameEmailProjection());
    }

    /** Class-based DTO projection — GET /api/employees/summary */
    @GetMapping("/summary")
    public ResponseEntity<List<EmployeeSummaryDTO>> getSummary() {
        return ResponseEntity.ok(employeeService.findAllAsSummaryDTO());
    }

    // ---------------------------------------------------------------
    // Exercise 10: Batch Processing
    // ---------------------------------------------------------------

    /**
     * Trigger batch insert of N employees.
     * POST /api/employees/batch/50
     */
    @PostMapping("/batch/{count}")
    public ResponseEntity<String> batchInsert(@PathVariable int count) {
        employeeService.demoBatchInsert(count);
        return ResponseEntity.ok("Batch inserted " + count + " employees");
    }
}
