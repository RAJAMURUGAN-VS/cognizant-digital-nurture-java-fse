package com.cognizant.ormlearn.repository;

import com.cognizant.ormlearn.model.Employee;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * EmployeeCriteriaRepository — demonstrates Criteria Query API.
 *
 * Hands-on 6: Criteria Query
 *
 * WHY Criteria Query?
 *   In scenarios like Amazon's filter panel, the WHERE clause is dynamic —
 *   different users select different filter combinations at runtime.
 *   HQL is a static string; you can't easily add/remove conditions.
 *   Criteria Query builds the WHERE clause PROGRAMMATICALLY.
 *
 * Key Criteria API objects:
 *   EntityManager    — JPA entry point; provides CriteriaBuilder.
 *   CriteriaBuilder  — factory for creating query components (predicates, expressions).
 *   CriteriaQuery<T> — represents the SELECT query structure.
 *   Root<T>          — represents the FROM clause (the entity being queried).
 *   Predicate        — represents a WHERE condition (cb.equal, cb.greaterThan etc.).
 *   TypedQuery<T>    — the executable query with a known result type.
 *
 * Reference: https://howtodoinjava.com/hibernate/hibernate-criteria-queries-tutorial/
 */
@Repository
public class EmployeeCriteriaRepository {

    /**
     * @PersistenceContext injects the JPA EntityManager managed by Spring.
     * EntityManager is the gateway to Hibernate's session.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Find all employees — no WHERE clause, equivalent to:
     *   SELECT e FROM Employee e
     */
    public List<Employee> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
        Root<Employee> root = cq.from(Employee.class);
        cq.select(root);
        TypedQuery<Employee> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    /**
     * Find employees whose salary is greater than the given threshold.
     * Equivalent HQL: SELECT e FROM Employee e WHERE e.salary > :minSalary
     *
     * Criteria API:
     *   cb.greaterThan(root.get("salary"), minSalary)
     *   → WHERE em_salary > ?
     */
    public List<Employee> findBySalaryGreaterThan(double minSalary) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
        Root<Employee> root = cq.from(Employee.class);

        // Build predicate: salary > minSalary
        Predicate salaryPredicate = cb.greaterThan(root.get("salary"), minSalary);
        cq.select(root).where(salaryPredicate);

        return entityManager.createQuery(cq).getResultList();
    }

    /**
     * Find permanent employees only.
     * Equivalent HQL: SELECT e FROM Employee e WHERE e.permanent = true
     */
    public List<Employee> findPermanentEmployees() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
        Root<Employee> root = cq.from(Employee.class);

        Predicate permanentPredicate = cb.equal(root.get("permanent"), true);
        cq.select(root).where(permanentPredicate);

        return entityManager.createQuery(cq).getResultList();
    }

    /**
     * Dynamic search — the Amazon filter scenario.
     *
     * Parameters are all optional (null = not filtered).
     * Only non-null parameters add a WHERE condition.
     *
     * This demonstrates the main benefit of Criteria Query:
     * the WHERE clause is built at RUNTIME based on what the user selected.
     *
     * @param minSalary  optional minimum salary filter
     * @param permanent  optional permanent status filter
     * @param deptId     optional department id filter
     */
    public List<Employee> findByDynamicCriteria(Double minSalary,
                                                 Boolean permanent,
                                                 Integer deptId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
        Root<Employee> root = cq.from(Employee.class);

        // Accumulate predicates based on which filters are provided
        List<Predicate> predicates = new ArrayList<>();

        if (minSalary != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("salary"), minSalary));
        }
        if (permanent != null) {
            predicates.add(cb.equal(root.get("permanent"), permanent));
        }
        if (deptId != null) {
            // Navigate the @ManyToOne relationship: employee.department.id
            predicates.add(cb.equal(root.get("department").get("id"), deptId));
        }

        // Combine all predicates with AND
        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        cq.select(root).orderBy(cb.asc(root.get("name")));

        TypedQuery<Employee> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    /**
     * Find employees whose name contains the given keyword (case-insensitive).
     * Equivalent HQL: SELECT e FROM Employee e WHERE LOWER(e.name) LIKE LOWER('%keyword%')
     *
     * cb.like() + cb.lower() + "%" wrapping = LIKE '%keyword%'
     */
    public List<Employee> findByNameContaining(String keyword) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
        Root<Employee> root = cq.from(Employee.class);

        Predicate namePredicate = cb.like(
                cb.lower(root.get("name")),
                "%" + keyword.toLowerCase() + "%"
        );
        cq.select(root).where(namePredicate);

        return entityManager.createQuery(cq).getResultList();
    }
}
