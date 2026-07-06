package com.cognizant.ormlearn.repository;

import com.cognizant.ormlearn.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * EmployeeRepository — Spring Data JPA repository for Employee.
 * Hands-on 3, 4, 5, 6.
 *
 * JpaRepository<Employee, Integer> provides:
 *   findById(int), save(Employee), deleteById(int), findAll(), etc.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
