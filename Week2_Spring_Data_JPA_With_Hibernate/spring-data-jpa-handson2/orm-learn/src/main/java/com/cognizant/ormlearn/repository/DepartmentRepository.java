package com.cognizant.ormlearn.repository;

import com.cognizant.ormlearn.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * DepartmentRepository — Spring Data JPA repository for Department.
 * Hands-on 3, 4, 5.
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
}
