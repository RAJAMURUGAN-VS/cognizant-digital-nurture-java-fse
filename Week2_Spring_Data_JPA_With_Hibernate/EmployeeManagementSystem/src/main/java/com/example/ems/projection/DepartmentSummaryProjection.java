package com.example.ems.projection;

/**
 * Exercise 8: Interface-based projection for Department.
 * Returns only department id and name — avoids loading the employee list.
 */
public interface DepartmentSummaryProjection {
    Long   getId();
    String getName();
}
