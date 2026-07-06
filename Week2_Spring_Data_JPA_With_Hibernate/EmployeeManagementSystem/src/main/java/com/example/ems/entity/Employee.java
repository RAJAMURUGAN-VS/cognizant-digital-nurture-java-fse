package com.example.ems.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Employee entity — Exercise 2, 3, 4, 5, 6, 7, 10.
 *
 * Exercise 2  : @Entity, @Table, @Id, @GeneratedValue, @ManyToOne.
 * Exercise 7  : @EntityListeners + auditing fields (@CreatedBy etc.).
 * Exercise 10 : @DynamicInsert / @DynamicUpdate — Hibernate includes only
 *               non-null / changed columns in INSERT/UPDATE SQL, improving
 *               performance for wide tables.
 *
 * Named Queries (Exercise 5):
 *   Employee.findByDepartmentName — fetch employees by department name.
 *   Employee.findByEmail          — fetch employee by email.
 */
@Entity
@Table(name = "employee", indexes = {
    @Index(name = "idx_employee_email",        columnList = "email"),
    @Index(name = "idx_employee_department_id", columnList = "department_id")
})
@NamedQueries({
    @NamedQuery(
        name  = "Employee.findByDepartmentName",
        query = "SELECT e FROM Employee e WHERE e.department.name = :deptName"
    ),
    @NamedQuery(
        name  = "Employee.findByEmail",
        query = "SELECT e FROM Employee e WHERE e.email = :email"
    )
})
@EntityListeners(AuditingEntityListener.class)
@DynamicInsert   // Exercise 10: only include non-null columns in INSERT
@DynamicUpdate   // Exercise 10: only include changed columns in UPDATE
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    /**
     * Exercise 2: Many employees belong to one department.
     * @ManyToOne — Hibernate LEFT OUTER JOINs department on employee fetch.
     * FetchType.LAZY avoids loading department when not needed.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    // ---------------------------------------------------------------
    // Exercise 7: Auditing fields
    // ---------------------------------------------------------------

    /**
     * @CreatedBy — automatically set to the current principal name on insert.
     * Requires AuditorAware bean (see AuditConfig.java).
     */
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    /**
     * @LastModifiedBy — updated on every save.
     */
    @LastModifiedBy
    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    /**
     * @CreatedDate — timestamp of initial insert (not updatable).
     */
    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    /**
     * @LastModifiedDate — timestamp of last update.
     */
    @LastModifiedDate
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;
}
