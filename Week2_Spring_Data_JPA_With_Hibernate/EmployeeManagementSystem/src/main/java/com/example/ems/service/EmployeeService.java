package com.example.ems.service;

import com.example.ems.entity.Department;
import com.example.ems.entity.Employee;
import com.example.ems.projection.EmployeeNameEmailProjection;
import com.example.ems.projection.EmployeeSummaryDTO;
import com.example.ems.repository.DepartmentRepository;
import com.example.ems.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * EmployeeService — Exercise 4, 5, 6, 8, 10.
 *
 * @Slf4j      — Lombok generates a static Logger field.
 * @Transactional — all public methods run in a transaction.
 */
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository   employeeRepository;
    private final DepartmentRepository departmentRepository;

    @PersistenceContext
    private EntityManager entityManager;

    // ---------------------------------------------------------------
    // Exercise 4: Basic CRUD
    // ---------------------------------------------------------------

    public Employee create(Employee employee) {
        log.info("Creating employee: {}", employee.getName());
        return employeeRepository.save(employee);
    }

    @Transactional(readOnly = true)
    public Optional<Employee> findById(Long id) {
        return employeeRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Employee update(Long id, Employee updated) {
        log.info("Updating employee id={}", id);
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found: " + id));
        existing.setName(updated.getName());
        existing.setEmail(updated.getEmail());
        if (updated.getDepartment() != null) {
            existing.setDepartment(updated.getDepartment());
        }
        return employeeRepository.save(existing);
    }

    public void delete(Long id) {
        log.info("Deleting employee id={}", id);
        employeeRepository.deleteById(id);
    }

    // ---------------------------------------------------------------
    // Exercise 5: Custom Queries
    // ---------------------------------------------------------------

    @Transactional(readOnly = true)
    public Optional<Employee> findByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public List<Employee> findByNameContaining(String keyword) {
        return employeeRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Transactional(readOnly = true)
    public List<Employee> findByDepartmentId(Long deptId) {
        return employeeRepository.findByDeptId(deptId);
    }

    @Transactional(readOnly = true)
    public List<Employee> findAllWithDepartment() {
        return employeeRepository.findAllWithDepartment();
    }

    /** Bulk update all employees from oldDept to newDept. */
    public int bulkUpdateDepartment(Long oldDeptId, Long newDeptId) {
        log.info("Bulk update: dept {} → {}", oldDeptId, newDeptId);
        return employeeRepository.bulkUpdateDepartment(oldDeptId, newDeptId);
    }

    // ---------------------------------------------------------------
    // Exercise 6: Pagination and Sorting
    // ---------------------------------------------------------------

    /**
     * Paginated and sorted employee list.
     *
     * @param page     0-based page number
     * @param size     number of records per page
     * @param sortBy   field to sort by (e.g. "name")
     * @param sortDir  "asc" or "desc"
     */
    @Transactional(readOnly = true)
    public Page<Employee> findPaginated(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return employeeRepository.findAll(pageable);
    }

    /**
     * Paginated employees by department with sorting.
     */
    @Transactional(readOnly = true)
    public Page<Employee> findByDepartmentPaginated(Long deptId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        return employeeRepository.findByDepartmentId(deptId, pageable);
    }

    /**
     * Paginated search by name keyword.
     */
    @Transactional(readOnly = true)
    public Page<Employee> searchByName(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        return employeeRepository.findByNameContainingIgnoreCase(keyword, pageable);
    }

    // ---------------------------------------------------------------
    // Exercise 8: Projections
    // ---------------------------------------------------------------

    @Transactional(readOnly = true)
    public List<EmployeeNameEmailProjection> findAllNameEmailProjection() {
        return employeeRepository.findAllProjectedBy();
    }

    @Transactional(readOnly = true)
    public List<EmployeeSummaryDTO> findAllAsSummaryDTO() {
        return employeeRepository.findAllAsSummaryDTO();
    }

    @Transactional(readOnly = true)
    public <T> T findByIdProjected(Long id, Class<T> projectionType) {
        return employeeRepository.findById(id, projectionType);
    }

    // ---------------------------------------------------------------
    // Exercise 10: Hibernate Batch Processing
    // ---------------------------------------------------------------

    /**
     * Batch insert employees in chunks of batchSize.
     *
     * Without batching: N INSERT statements sent to DB individually.
     * With batching: statements are grouped and sent in one round-trip
     * (controlled by hibernate.jdbc.batch_size in application.properties).
     *
     * entityManager.flush() — sends pending SQL to DB.
     * entityManager.clear() — detaches all entities from persistence context,
     *                          preventing OutOfMemoryError on large datasets.
     *
     * @param employees list of employees to insert
     * @param batchSize how many to flush/clear at a time
     */
    public void batchInsert(List<Employee> employees, int batchSize) {
        log.info("Starting batch insert of {} employees", employees.size());
        for (int i = 0; i < employees.size(); i++) {
            employeeRepository.save(employees.get(i));
            if ((i + 1) % batchSize == 0) {
                entityManager.flush();
                entityManager.clear();
                log.debug("Flushed and cleared batch at index {}", i + 1);
            }
        }
        // Flush any remaining
        entityManager.flush();
        entityManager.clear();
        log.info("Batch insert complete");
    }

    /**
     * Demonstrates batch insert by creating N sample employees.
     */
    public void demoBatchInsert(int count) {
        Department dept = departmentRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Department 1 not found"));

        List<Employee> batch = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            batch.add(Employee.builder()
                    .name("BatchEmployee_" + i)
                    .email("batch" + i + "@example.com")
                    .department(dept)
                    .build());
        }
        batchInsert(batch, 10); // flush every 10 records
    }
}
