package com.cognizant.ormlearn.model;

import javax.persistence.*;
import java.util.Set;

/**
 * Skill — JPA entity mapped to the 'skill' table.
 *
 * Hands-on 6: Many-to-Many relationship between Employee and Skill.
 *
 * @ManyToMany(mappedBy = "skillList") — the join table is defined on
 *   the Employee side; Skill is the inverse (non-owning) side.
 *   'mappedBy' value must match the field name in Employee.java.
 */
@Entity
@Table(name = "skill")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sk_id")
    private int id;

    @Column(name = "sk_name")
    private String name;

    /**
     * Inverse side of Many-to-Many.
     * mappedBy = "skillList" refers to the field in Employee.java.
     */
    @ManyToMany(mappedBy = "skillList")
    private Set<Employee> employeeList;

    // ---------------------------------------------------------------
    // Constructors
    // ---------------------------------------------------------------

    public Skill() {}

    // ---------------------------------------------------------------
    // Getters and Setters
    // ---------------------------------------------------------------

    public int getId()                { return id; }
    public void setId(int id)         { this.id = id; }

    public String getName()           { return name; }
    public void setName(String name)  { this.name = name; }

    public Set<Employee> getEmployeeList()                  { return employeeList; }
    public void setEmployeeList(Set<Employee> employeeList) { this.employeeList = employeeList; }

    @Override
    public String toString() {
        return "Skill{id=" + id + ", name='" + name + "'}";
    }
}
