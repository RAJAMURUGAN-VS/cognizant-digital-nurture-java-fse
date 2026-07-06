package com.example.ems.repository;

import com.example.ems.entity.Department;
import com.example.ems.projection.DepartmentSummaryProjection;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * DepartmentRepository — Exercise 3, 4, 5, 6, 8.
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    // ---------------------------------------------------------------
    // Exercise 3 & 5a: Derived Query Methods
    // ---------------------------------------------------------------

    Optional<Department> findByName(String name);

    List<Department> findByNameContainingIgnoreCase(String keyword);

    boolean existsByName(String name);

    // ---------------------------------------------------------------
    // Exercise 5b: @Query annotation
    // ---------------------------------------------------------------

    /**
     * JPQL — fetch departments with their employee list in a single query.
     */
    @Query("SELECT DISTINCT d FROM Department d LEFT JOIN FETCH d.employees")
    List<Department> findAllWithEmployees();

    /**
     * Uses @NamedQuery "Department.findByName" defined on entity.
     */
    @Query(name = "Department.findByName")
    Optional<Department> findByNameNamed(@Param("name") String name);

    // ---------------------------------------------------------------
    // Exercise 6: Sorting (Pageable with Sort)
    // ---------------------------------------------------------------

    /** Returns departments sorted by the given Sort descriptor. */
    List<Department> findAll(Sort sort);

    // ---------------------------------------------------------------
    // Exercise 8: Projections
    // ---------------------------------------------------------------

    /**
     * Interface-based projection — only id and name columns fetched.
     */
    List<DepartmentSummaryProjection> findAllProjectedBy();
}
