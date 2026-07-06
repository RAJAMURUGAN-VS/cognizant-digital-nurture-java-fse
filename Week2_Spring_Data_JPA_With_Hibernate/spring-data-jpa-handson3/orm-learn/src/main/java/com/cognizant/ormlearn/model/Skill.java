package com.cognizant.ormlearn.model;

import javax.persistence.*;
import java.util.Set;

/**
 * Skill entity.
 * Hands-on 2,6: Many-to-Many with Employee (inverse/non-owning side).
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

    /** Inverse side of Many-to-Many — join table owned by Employee. */
    @ManyToMany(mappedBy = "skillList")
    private Set<Employee> employeeList;

    public Skill() {}

    public int getId()                                      { return id; }
    public void setId(int id)                               { this.id = id; }
    public String getName()                                 { return name; }
    public void setName(String name)                        { this.name = name; }
    public Set<Employee> getEmployeeList()                  { return employeeList; }
    public void setEmployeeList(Set<Employee> employeeList) { this.employeeList = employeeList; }

    @Override
    public String toString() {
        return "Skill{id=" + id + ", name='" + name + "'}";
    }
}
