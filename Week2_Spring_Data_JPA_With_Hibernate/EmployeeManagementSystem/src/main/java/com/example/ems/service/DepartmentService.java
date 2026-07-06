package com.example.ems.service;

import com.example.ems.entity.Department;
import com.example.ems.projection.DepartmentSummaryProjection;
import com.example.ems.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * DepartmentService — Exercise 4, 5, 6, 8.
 */
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    // ---------------------------------------------------------------
    // Exercise 4: CRUD
    // ---------------------------------------------------------------

    public Department create(Department department) {
        log.info("Creating department: {}", department.getName());
        return departmentRepository.save(department);
    }

    @Transactional(readOnly = true)
    public Optional<Department> findById(Long id) {
        return departmentRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    public Department update(Long id, Department updated) {
        Department existing = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found: " + id));
        existing.setName(updated.getName());
        return departmentRepository.save(existing);
    }

    public void delete(Long id) {
        log.info("Deleting department id={}", id);
        departmentRepository.deleteById(id);
    }

    // ---------------------------------------------------------------
    // Exercise 5: Custom queries + Named queries
    // ---------------------------------------------------------------

    @Transactional(readOnly = true)
    public Optional<Department> findByName(String name) {
        return departmentRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public List<Department> findAllWithEmployees() {
        return departmentRepository.findAllWithEmployees();
    }

    // ---------------------------------------------------------------
    // Exercise 6: Sorting
    // ---------------------------------------------------------------

    @Transactional(readOnly = true)
    public List<Department> findAllSortedByName(String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by("name").descending()
                : Sort.by("name").ascending();
        return departmentRepository.findAll(sort);
    }

    // ---------------------------------------------------------------
    // Exercise 8: Projections
    // ---------------------------------------------------------------

    @Transactional(readOnly = true)
    public List<DepartmentSummaryProjection> findAllSummary() {
        return departmentRepository.findAllProjectedBy();
    }
}
