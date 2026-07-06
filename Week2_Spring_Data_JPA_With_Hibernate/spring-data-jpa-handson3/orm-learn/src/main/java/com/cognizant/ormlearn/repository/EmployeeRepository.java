package com.cognizant.ormlearn.repository;

import com.cognizant.ormlearn.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * EmployeeRepository — JPA repository with HQL and Native Query methods.
 *
 * ---------------------------------------------------------------
 * HQL vs JPQL:
 *   JPQL  — Java Persistence Query Language (JPA standard/subset).
 *   HQL   — Hibernate Query Language (superset of JPQL).
 *   Both  — use entity class names and field names, NOT table/column names.
 *   Key difference: HQL supports INSERT; JPQL does not.
 *
 * @Query annotation — used to define custom JPQL/HQL or native SQL.
 *   nativeQuery=false (default) → JPQL/HQL (entity + field names)
 *   nativeQuery=true            → raw SQL (table + column names)
 * ---------------------------------------------------------------
 *
 * Hands-on 2: getAllPermanentEmployees() — HQL with 'fetch' optimisation.
 * Hands-on 4: getAverageSalary()        — HQL aggregate function AVG().
 * Hands-on 5: getAllEmployeesNative()    — Native SQL query.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    // ---------------------------------------------------------------
    // Hands-on 2a: Basic HQL — permanent employees
    // (triggers multiple SQL queries due to EAGER on Skill/Department)
    // ---------------------------------------------------------------

    /**
     * HQL: SELECT from the Employee entity (not table) where permanent = 1.
     * NOTE: 'Employee' refers to the Java class, 'permanent' is the Java field.
     * Without 'fetch', skill list will be fetched lazily (multiple queries).
     */
    @Query(value = "SELECT e FROM Employee e WHERE e.permanent = 1")
    List<Employee> getAllPermanentEmployees();

    // ---------------------------------------------------------------
    // Hands-on 2b: Optimised HQL with 'fetch'
    // ---------------------------------------------------------------

    /**
     * Optimised HQL using LEFT JOIN FETCH.
     *
     * 'join fetch' instructs Hibernate to:
     *   1. JOIN the related table in SQL.
     *   2. POPULATE the Java bean fields (department, skillList).
     *
     * Without 'fetch': JOIN executes but bean fields remain null/lazy.
     * With 'fetch':    JOIN executes AND bean is populated — SINGLE QUERY.
     *
     * Result: one SQL query with LEFT OUTER JOINs instead of N+1 queries.
     */
    @Query(value = "SELECT DISTINCT e FROM Employee e "
                 + "LEFT JOIN FETCH e.department d "
                 + "LEFT JOIN FETCH e.skillList "
                 + "WHERE e.permanent = 1")
    List<Employee> getAllPermanentEmployeesOptimised();

    // ---------------------------------------------------------------
    // Hands-on 4: HQL aggregate — average salary
    // ---------------------------------------------------------------

    /**
     * Hands-on 4a: Average salary across ALL employees.
     * AVG() is a JPQL/HQL aggregate function — equivalent to SQL AVG().
     */
    @Query(value = "SELECT AVG(e.salary) FROM Employee e")
    double getAverageSalary();

    /**
     * Hands-on 4b: Average salary filtered by department id.
     *
     * :id     — named parameter bound by @Param("id").
     * e.department.id — navigating the @ManyToOne relationship in HQL
     *                   (no JOIN needed — Hibernate resolves it automatically).
     *
     * @Param("id") — binds the method parameter to the :id placeholder in the query.
     */
    @Query(value = "SELECT AVG(e.salary) FROM Employee e WHERE e.department.id = :id")
    double getAverageSalaryByDepartment(@Param("id") int departmentId);

    // ---------------------------------------------------------------
    // Hands-on 5: Native SQL query
    // ---------------------------------------------------------------

    /**
     * Native Query — raw SQL sent directly to MySQL without Hibernate translation.
     *
     * nativeQuery = true: Spring Data passes the SQL string directly to JDBC.
     * Uses TABLE name (employee) and COLUMN names (em_*), not entity/field names.
     *
     * When to use:
     *   - Complex vendor-specific SQL not expressible in JPQL/HQL.
     *   - Performance-critical queries where HQL translation overhead matters.
     * Avoid when possible — native queries are not portable across databases.
     */
    @Query(value = "SELECT * FROM employee", nativeQuery = true)
    List<Employee> getAllEmployeesNative();
}
