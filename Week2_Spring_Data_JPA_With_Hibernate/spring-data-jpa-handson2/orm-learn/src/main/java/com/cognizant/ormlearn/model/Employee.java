package com.cognizant.ormlearn.model;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Employee — JPA entity mapped to the 'employee' table.
 *
 * Hands-on 3 : Basic entity with all fields.
 * Hands-on 4 : @ManyToOne — employee belongs to one department.
 * Hands-on 6 : @ManyToMany — employee has many skills.
 *
 * Key annotations:
 *
 * @ManyToOne + @JoinColumn
 *   - Many employees belong to one department.
 *   - @JoinColumn(name="em_dp_id") maps to the FK column in the employee table.
 *   - Default FetchType for @ManyToOne is EAGER — Hibernate joins department
 *     automatically when fetching an employee.
 *
 * @ManyToMany + @JoinTable
 *   - Employee owns the relationship (join table defined here).
 *   - @JoinTable specifies the join table name and FK columns.
 *   - FetchType.EAGER required to avoid LazyInitializationException outside tx.
 */
@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "em_id")
    private int id;

    @Column(name = "em_name")
    private String name;

    @Column(name = "em_salary")
    private double salary;

    @Column(name = "em_permanent")
    private boolean permanent;

    @Column(name = "em_date_of_birth")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    /**
     * Hands-on 4: Many-to-One — many employees belong to one department.
     *
     * @ManyToOne   — defines the relationship cardinality.
     * @JoinColumn  — specifies the FK column name in the employee table.
     *
     * FetchType is EAGER by default for @ManyToOne per JPA spec.
     * Hibernate will LEFT OUTER JOIN department when fetching employee.
     */
    @ManyToOne
    @JoinColumn(name = "em_dp_id")
    private Department department;

    /**
     * Hands-on 6: Many-to-Many — employee has many skills.
     *
     * @JoinTable defines the join table 'employee_skill':
     *   joinColumns         — FK pointing to THIS entity (employee)
     *   inverseJoinColumns  — FK pointing to the OTHER entity (skill)
     *
     * FetchType.EAGER fetches skill list along with the employee.
     * Without EAGER, accessing skillList outside @Transactional throws
     * LazyInitializationException.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "employee_skill",
        joinColumns        = @JoinColumn(name = "es_em_id"),
        inverseJoinColumns = @JoinColumn(name = "es_sk_id")
    )
    private Set<Skill> skillList = new HashSet<>();

    // ---------------------------------------------------------------
    // Constructors
    // ---------------------------------------------------------------

    public Employee() {}

    // ---------------------------------------------------------------
    // Getters and Setters
    // ---------------------------------------------------------------

    public int getId()                  { return id; }
    public void setId(int id)           { this.id = id; }

    public String getName()             { return name; }
    public void setName(String name)    { this.name = name; }

    public double getSalary()               { return salary; }
    public void setSalary(double salary)    { this.salary = salary; }

    public boolean isPermanent()                    { return permanent; }
    public void setPermanent(boolean permanent)     { this.permanent = permanent; }

    public Date getDateOfBirth()                    { return dateOfBirth; }
    public void setDateOfBirth(Date dateOfBirth)    { this.dateOfBirth = dateOfBirth; }

    public Department getDepartment()                   { return department; }
    public void setDepartment(Department department)    { this.department = department; }

    public Set<Skill> getSkillList()                { return skillList; }
    public void setSkillList(Set<Skill> skillList)  { this.skillList = skillList; }

    @Override
    public String toString() {
        return "Employee{id=" + id +
               ", name='" + name + "'" +
               ", salary=" + salary +
               ", permanent=" + permanent +
               ", dateOfBirth=" + dateOfBirth +
               ", department=" + department +
               "}";
    }
}
