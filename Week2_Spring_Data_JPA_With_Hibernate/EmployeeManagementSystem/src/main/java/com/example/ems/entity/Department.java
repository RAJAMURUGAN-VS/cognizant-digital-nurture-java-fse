package com.example.ems.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Department entity — Exercise 2, 3, 4, 5, 6, 10.
 *
 * Exercise 2  : @Entity, @Table, @Id, @GeneratedValue, @OneToMany.
 * Exercise 10 : @BatchSize — Hibernate batch fetches the employee list
 *               in batches of 10 instead of one query per department.
 *               Reduces N+1 queries when loading multiple departments.
 *
 * Named Queries (Exercise 5):
 *   Department.findByName — finds department by exact name.
 */
@Entity
@Table(name = "department")
@NamedQueries({
    @NamedQuery(
        name  = "Department.findByName",
        query = "SELECT d FROM Department d WHERE d.name = :name"
    ),
    @NamedQuery(
        name  = "Department.findAll",
        query = "SELECT d FROM Department d ORDER BY d.name"
    )
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    /**
     * One department has many employees.
     * mappedBy = "department" — Employee owns the FK (department_id column).
     * cascade = ALL           — persist/remove employees with department.
     * orphanRemoval = true    — delete orphaned employees.
     *
     * Exercise 10: @BatchSize(size=10) — batch-fetch employees in groups of 10
     *              to reduce the number of SQL SELECT queries.
     */
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 10)
    @Builder.Default
    private List<Employee> employees = new ArrayList<>();

    // Helper method to maintain bidirectional consistency
    public void addEmployee(Employee employee) {
        employees.add(employee);
        employee.setDepartment(this);
    }

    public void removeEmployee(Employee employee) {
        employees.remove(employee);
        employee.setDepartment(null);
    }
}
