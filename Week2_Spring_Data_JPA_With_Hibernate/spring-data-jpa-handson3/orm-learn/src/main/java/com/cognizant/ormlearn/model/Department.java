package com.cognizant.ormlearn.model;

import javax.persistence.*;
import java.util.Set;

/**
 * Department entity.
 *
 * Hands-on 2: The EAGER fetch on employeeList is intentionally REMOVED here.
 * The optimised HQL uses 'left join fetch' in the query instead, which avoids
 * multiple SELECT queries for each department's employee list.
 *
 * See EmployeeRepository for the optimised @Query with fetch keyword.
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
     * Hands-on 2 optimisation:
     * REMOVED FetchType.EAGER — fetch is handled by 'left join fetch' in HQL.
     * Default FetchType.LAZY avoids loading employees when not needed.
     */
    @OneToMany(mappedBy = "department")
    private Set<Employee> employeeList;

    public Department() {}

    public int getId()                                           { return id; }
    public void setId(int id)                                    { this.id = id; }
    public String getName()                                      { return name; }
    public void setName(String name)                             { this.name = name; }
    public Set<Employee> getEmployeeList()                       { return employeeList; }
    public void setEmployeeList(Set<Employee> employeeList)      { this.employeeList = employeeList; }

    @Override
    public String toString() {
        return "Department{id=" + id + ", name='" + name + "'}";
    }
}
