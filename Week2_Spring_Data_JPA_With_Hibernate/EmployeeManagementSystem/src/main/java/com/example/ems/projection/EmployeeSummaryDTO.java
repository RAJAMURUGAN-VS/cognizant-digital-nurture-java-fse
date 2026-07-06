package com.example.ems.projection;

/**
 * Exercise 8: Class-based projection (DTO / Value Object).
 *
 * Unlike interface-based projections, this is a plain Java class.
 * Spring Data JPA uses a JPQL constructor expression to populate it:
 *
 *   SELECT new com.example.ems.projection.EmployeeSummaryDTO(e.id, e.name, d.name)
 *   FROM Employee e LEFT JOIN e.department d
 *
 * Benefits of class-based projections:
 *   - Full control over the DTO structure.
 *   - Can add business logic / computed fields.
 *   - Immutable (all fields set via constructor).
 */
public class EmployeeSummaryDTO {

    private final Long   id;
    private final String name;
    private final String departmentName;

    /**
     * Constructor used by the JPQL constructor expression.
     * Parameter order must match the SELECT clause.
     */
    public EmployeeSummaryDTO(Long id, String name, String departmentName) {
        this.id             = id;
        this.name           = name;
        this.departmentName = departmentName;
    }

    public Long   getId()             { return id; }
    public String getName()           { return name; }
    public String getDepartmentName() { return departmentName; }

    @Override
    public String toString() {
        return "EmployeeSummaryDTO{id=" + id + ", name='" + name
               + "', dept='" + departmentName + "'}";
    }
}
