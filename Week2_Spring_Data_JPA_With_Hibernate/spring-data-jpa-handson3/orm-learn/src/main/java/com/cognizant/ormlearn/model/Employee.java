package com.cognizant.ormlearn.model;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Employee entity.
 *
 * Hands-on 2: FetchType.EAGER is REMOVED from skillList.
 *   - The optimised HQL uses 'left join fetch e.skillList' instead.
 *   - This way a SINGLE SQL query retrieves employees + departments + skills.
 *   - Without fetch in HQL, skillList would be null (LazyInitializationException).
 *
 * Hands-on 6: Criteria Query — EmployeeCriteriaRepository uses CriteriaBuilder
 *   to build dynamic WHERE clauses on this entity.
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
     * Many-to-One — EAGER by JPA default.
     * Hibernate LEFT OUTER JOINs department when fetching employee.
     */
    @ManyToOne
    @JoinColumn(name = "em_dp_id")
    private Department department;

    /**
     * Many-to-Many — LAZY by default (EAGER removed for optimisation).
     * Populated only when HQL includes 'left join fetch e.skillList'.
     */
    @ManyToMany
    @JoinTable(
        name = "employee_skill",
        joinColumns        = @JoinColumn(name = "es_em_id"),
        inverseJoinColumns = @JoinColumn(name = "es_sk_id")
    )
    private Set<Skill> skillList = new HashSet<>();

    public Employee() {}

    public int getId()                              { return id; }
    public void setId(int id)                       { this.id = id; }
    public String getName()                         { return name; }
    public void setName(String name)                { this.name = name; }
    public double getSalary()                       { return salary; }
    public void setSalary(double salary)            { this.salary = salary; }
    public boolean isPermanent()                    { return permanent; }
    public void setPermanent(boolean permanent)     { this.permanent = permanent; }
    public Date getDateOfBirth()                    { return dateOfBirth; }
    public void setDateOfBirth(Date dateOfBirth)    { this.dateOfBirth = dateOfBirth; }
    public Department getDepartment()               { return department; }
    public void setDepartment(Department department){ this.department = department; }
    public Set<Skill> getSkillList()                { return skillList; }
    public void setSkillList(Set<Skill> skillList)  { this.skillList = skillList; }

    @Override
    public String toString() {
        return "Employee{id=" + id + ", name='" + name + "', salary=" + salary
               + ", permanent=" + permanent + ", dept=" + department + "}";
    }
}
