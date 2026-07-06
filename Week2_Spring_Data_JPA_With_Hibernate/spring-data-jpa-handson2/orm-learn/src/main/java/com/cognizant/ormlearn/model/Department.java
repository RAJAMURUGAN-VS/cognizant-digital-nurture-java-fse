package com.cognizant.ormlearn.model;

import javax.persistence.*;
import java.util.Set;

/**
 * Department — JPA entity mapped to the 'department' table.
 *
 * Hands-on 3,4,5: Many-to-One (Employee → Department)
 *                  One-to-Many (Department → Employees)
 *
 * @OneToMany(mappedBy = "department") — the 'mappedBy' tells Hibernate
 *   that the foreign key lives on the Employee side (em_dp_id column).
 *   This avoids a redundant join table.
 *
 * FetchType.EAGER — fetch employees along with the department in one query.
 *   Default for @OneToMany is LAZY — which causes LazyInitializationException
 *   outside a transaction. Switch to EAGER to fix this (Hands-on 5).
 */
@Entity
@Table(name = "department")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dp_id")
    private int id;

    @Column(name = "dp_name")
    private String name;

    /**
     * Hands-on 5: One-to-Many relationship.
     * mappedBy = "department" refers to the field name in Employee.java.
     * FetchType.EAGER ensures employee list is loaded with the department.
     */
    @OneToMany(mappedBy = "department", fetch = FetchType.EAGER)
    private Set<Employee> employeeList;

    // ---------------------------------------------------------------
    // Constructors
    // ---------------------------------------------------------------

    public Department() {}

    // ---------------------------------------------------------------
    // Getters and Setters
    // ---------------------------------------------------------------

    public int getId()                { return id; }
    public void setId(int id)         { this.id = id; }

    public String getName()           { return name; }
    public void setName(String name)  { this.name = name; }

    public Set<Employee> getEmployeeList()                       { return employeeList; }
    public void setEmployeeList(Set<Employee> employeeList)      { this.employeeList = employeeList; }

    @Override
    public String toString() {
        return "Department{id=" + id + ", name='" + name + "'}";
    }
}
