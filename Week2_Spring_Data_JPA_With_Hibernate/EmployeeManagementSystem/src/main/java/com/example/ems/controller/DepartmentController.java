package com.example.ems.controller;

import com.example.ems.entity.Department;
import com.example.ems.projection.DepartmentSummaryProjection;
import com.example.ems.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * DepartmentController — Exercise 4, 5, 6, 8.
 *
 * Base URL: /api/departments
 *
 * Endpoints:
 *   GET    /api/departments                   — list all
 *   GET    /api/departments/{id}              — get by id
 *   POST   /api/departments                   — create
 *   PUT    /api/departments/{id}              — update
 *   DELETE /api/departments/{id}              — delete
 *   GET    /api/departments/with-employees    — JOIN FETCH (Exercise 5)
 *   GET    /api/departments/sorted?dir=asc    — sorted list (Exercise 6)
 *   GET    /api/departments/summary           — projection (Exercise 8)
 */
@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    // ---------------------------------------------------------------
    // Exercise 4: CRUD
    // ---------------------------------------------------------------

    @GetMapping
    public ResponseEntity<List<Department>> getAll() {
        return ResponseEntity.ok(departmentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Department> getById(@PathVariable Long id) {
        return departmentService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Department> create(@RequestBody Department department) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(departmentService.create(department));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Department> update(@PathVariable Long id,
                                              @RequestBody Department department) {
        try {
            return ResponseEntity.ok(departmentService.update(id, department));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        departmentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ---------------------------------------------------------------
    // Exercise 5: Custom queries
    // ---------------------------------------------------------------

    /** All departments with employees (JOIN FETCH). */
    @GetMapping("/with-employees")
    public ResponseEntity<List<Department>> getAllWithEmployees() {
        return ResponseEntity.ok(departmentService.findAllWithEmployees());
    }

    /** Find by name — GET /api/departments/name?value=Engineering */
    @GetMapping("/name")
    public ResponseEntity<Department> findByName(@RequestParam String value) {
        return departmentService.findByName(value)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ---------------------------------------------------------------
    // Exercise 6: Sorting
    // ---------------------------------------------------------------

    /** Sorted list — GET /api/departments/sorted?dir=asc */
    @GetMapping("/sorted")
    public ResponseEntity<List<Department>> getSorted(
            @RequestParam(defaultValue = "asc") String dir) {
        return ResponseEntity.ok(departmentService.findAllSortedByName(dir));
    }

    // ---------------------------------------------------------------
    // Exercise 8: Projections
    // ---------------------------------------------------------------

    /** Summary projection — GET /api/departments/summary */
    @GetMapping("/summary")
    public ResponseEntity<List<DepartmentSummaryProjection>> getSummary() {
        return ResponseEntity.ok(departmentService.findAllSummary());
    }
}
