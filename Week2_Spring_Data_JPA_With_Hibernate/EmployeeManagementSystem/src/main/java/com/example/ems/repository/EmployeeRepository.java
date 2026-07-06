package com.example.ems.repository;

import com.example.ems.entity.Employee;
import com.example.ems.projection.EmployeeNameEmailProjection;
import com.example.ems.projection.EmployeeSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * EmployeeRepository — Exercise 3, 4, 5, 6, 8.
 *
 * Exercise 3: Extends JpaRepository<Employee, Long> — provides:
 *   save(), findById(), findAll(), deleteById(), count(), etc.
 *
 * Exercise 5: Custom query methods:
 *   a) Derived query methods (from method name)
 *   b) @Query annotation (JPQL / HQL)
 *   c) Named queries (defined on Employee entity with @NamedQuery)
 *
 * Exercise 6: Pagination and sorting via Pageable.
 * Exercise 8: Projections.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // ---------------------------------------------------------------
    // Exercise 3 & 5a: Derived Query Methods
    // Spring Data generates SQL from the method name automatically.
    // ---------------------------------------------------------------

    /** SELECT * FROM employee WHERE name = ? */
    Optional<Employee> findByName(String name);

    /** SELECT * FROM employee WHERE email = ? */
    Optional<Employee> findByEmail(String email);

    /** SELECT * FROM employee WHERE name LIKE '%keyword%' */
    List<Employee> findByNameContainingIgnoreCase(String keyword);

    /** SELECT * FROM employee WHERE department.name = ? (joins department) */
    List<Employee> findByDepartmentName(String departmentName);

    /** SELECT * FROM employee WHERE department.id = ? */
    List<Employee> findByDepartmentId(Long departmentId);

    /** SELECT * FROM employee WHERE name STARTING WITH prefix */
    List<Employee> findByNameStartingWith(String prefix);

    // ---------------------------------------------------------------
    // Exercise 5b: @Query annotation (JPQL)
    // ---------------------------------------------------------------

    /**
     * JPQL — fetch employees with department in a single JOIN FETCH query.
     * DISTINCT avoids duplicate rows from the JOIN.
     */
    @Query("SELECT DISTINCT e FROM Employee e LEFT JOIN FETCH e.department")
    List<Employee> findAllWithDepartment();

    /**
     * JPQL search by email (case-insensitive).
     * :email — named parameter bound by @Param.
     */
    @Query("SELECT e FROM Employee e WHERE LOWER(e.email) = LOWER(:email)")
    Optional<Employee> findByEmailIgnoreCase(@Param("email") String email);

    /**
     * JPQL — find employees in a specific department by department id.
     * Demonstrates navigation through @ManyToOne: e.department.id
     */
    @Query("SELECT e FROM Employee e WHERE e.department.id = :deptId")
    List<Employee> findByDeptId(@Param("deptId") Long deptId);

    /**
     * JPQL bulk update — update department for all employees in old dept.
     * @Modifying — required for UPDATE/DELETE @Query methods.
     * clearAutomatically = true — clears persistence context after update.
     */
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Employee e SET e.department.id = :newDeptId WHERE e.department.id = :oldDeptId")
    int bulkUpdateDepartment(@Param("oldDeptId") Long oldDeptId,
                              @Param("newDeptId") Long newDeptId);

    // ---------------------------------------------------------------
    // Exercise 5c: Named Queries (defined on Employee entity)
    // Spring Data finds them by convention: EntityName.methodName
    // ---------------------------------------------------------------

    /** Uses @NamedQuery "Employee.findByDepartmentName" defined on entity. */
    List<Employee> findByDepartmentName_Named(@Param("deptName") String deptName);

    // ---------------------------------------------------------------
    // Exercise 6: Pagination and Sorting
    // ---------------------------------------------------------------

    /**
     * Paginated list of all employees.
     * Pageable carries: page number, page size, sort order.
     * Returns Page<Employee> with metadata: totalElements, totalPages, etc.
     */
    Page<Employee> findAll(Pageable pageable);

    /**
     * Paginated employees filtered by department id.
     */
    Page<Employee> findByDepartmentId(Long departmentId, Pageable pageable);

    /**
     * Paginated employees whose name contains keyword — combines search + pagination.
     */
    Page<Employee> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

    // ---------------------------------------------------------------
    // Exercise 8: Projections
    // ---------------------------------------------------------------

    /**
     * Interface-based projection — returns only name and email columns.
     * Spring Data generates: SELECT e.name, e.email FROM employee e
     */
    List<EmployeeNameEmailProjection> findAllProjectedBy();

    /**
     * Class-based projection using JPQL constructor expression.
     * Only id, name, departmentName are fetched.
     */
    @Query("SELECT new com.example.ems.projection.EmployeeSummaryDTO(e.id, e.name, d.name) "
         + "FROM Employee e LEFT JOIN e.department d")
    List<EmployeeSummaryDTO> findAllAsSummaryDTO();

    /**
     * Dynamic projection — caller chooses the projection type at runtime.
     * E.g.: findById(1L, EmployeeNameEmailProjection.class)
     */
    <T> T findById(Long id, Class<T> type);
}
