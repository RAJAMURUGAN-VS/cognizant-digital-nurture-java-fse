package com.example.ems.projection;

/**
 * Exercise 8: Interface-based projection.
 *
 * Spring Data JPA generates a proxy that implements this interface.
 * Only 'name' and 'email' columns are fetched from the database —
 * the full Employee entity is NOT loaded.
 *
 * Usage in repository:
 *   List<EmployeeNameEmailProjection> findBy();
 *
 * Benefits:
 *   - Less data transferred from DB (only selected columns).
 *   - Cleaner API — callers only see the fields they need.
 *   - No entity lifecycle overhead (entities are not tracked by Hibernate).
 */
public interface EmployeeNameEmailProjection {

    /** Maps to Employee.name */
    String getName();

    /** Maps to Employee.email */
    String getEmail();

    /**
     * @Value — SpEL expression combining two fields.
     * Spring Data evaluates this at query time.
     * No corresponding column — computed from name + email.
     */
    @org.springframework.beans.factory.annotation.Value("#{target.name + ' <' + target.email + '>'}")
    String getNameWithEmail();
}
