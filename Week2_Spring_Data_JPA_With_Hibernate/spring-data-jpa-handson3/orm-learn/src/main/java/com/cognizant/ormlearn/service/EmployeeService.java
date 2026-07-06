package com.cognizant.ormlearn.service;

import com.cognizant.ormlearn.model.Employee;
import com.cognizant.ormlearn.repository.EmployeeCriteriaRepository;
import com.cognizant.ormlearn.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * EmployeeService — service layer for all employee operations.
 *
 * Covers Hands-on 2, 4, 5, 6.
 */
@Service
public class EmployeeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeCriteriaRepository criteriaRepository;

    // ---------------------------------------------------------------
    // Basic CRUD
    // ---------------------------------------------------------------

    @Transactional
    public Employee get(int id) {
        LOGGER.info("Start get({})", id);
        return employeeRepository.findById(id).get();
    }

    @Transactional
    public void save(Employee employee) {
        LOGGER.info("Start save");
        employeeRepository.save(employee);
        LOGGER.info("End save, id={}", employee.getId());
    }

    // ---------------------------------------------------------------
    // Hands-on 2: HQL — permanent employees
    // ---------------------------------------------------------------

    /**
     * Basic HQL (may trigger N+1 queries for department/skills if EAGER removed).
     */
    @Transactional
    public List<Employee> getAllPermanentEmployees() {
        LOGGER.info("Start getAllPermanentEmployees");
        return employeeRepository.getAllPermanentEmployees();
    }

    /**
     * Optimised HQL with LEFT JOIN FETCH — single SQL query.
     */
    @Transactional
    public List<Employee> getAllPermanentEmployeesOptimised() {
        LOGGER.info("Start getAllPermanentEmployeesOptimised");
        return employeeRepository.getAllPermanentEmployeesOptimised();
    }

    // ---------------------------------------------------------------
    // Hands-on 4: HQL aggregate — AVG salary
    // ---------------------------------------------------------------

    /** Average salary across all employees. */
    @Transactional(readOnly = true)
    public double getAverageSalary() {
        LOGGER.info("Start getAverageSalary");
        return employeeRepository.getAverageSalary();
    }

    /** Average salary for a specific department. */
    @Transactional(readOnly = true)
    public double getAverageSalaryByDepartment(int departmentId) {
        LOGGER.info("Start getAverageSalaryByDepartment({})", departmentId);
        return employeeRepository.getAverageSalaryByDepartment(departmentId);
    }

    // ---------------------------------------------------------------
    // Hands-on 5: Native Query
    // ---------------------------------------------------------------

    /** All employees via native SQL (SELECT * FROM employee). */
    @Transactional(readOnly = true)
    public List<Employee> getAllEmployeesNative() {
        LOGGER.info("Start getAllEmployeesNative");
        return employeeRepository.getAllEmployeesNative();
    }

    // ---------------------------------------------------------------
    // Hands-on 6: Criteria Query
    // ---------------------------------------------------------------

    /** All employees via Criteria API (no WHERE clause). */
    @Transactional(readOnly = true)
    public List<Employee> findAllCriteria() {
        return criteriaRepository.findAll();
    }

    /** Employees with salary > minSalary (Criteria API). */
    @Transactional(readOnly = true)
    public List<Employee> findBySalaryGreaterThan(double minSalary) {
        return criteriaRepository.findBySalaryGreaterThan(minSalary);
    }

    /** Permanent employees (Criteria API). */
    @Transactional(readOnly = true)
    public List<Employee> findPermanentEmployees() {
        return criteriaRepository.findPermanentEmployees();
    }

    /** Dynamic multi-criteria search (Criteria API — the Amazon filter scenario). */
    @Transactional(readOnly = true)
    public List<Employee> findByDynamicCriteria(Double minSalary, Boolean permanent, Integer deptId) {
        return criteriaRepository.findByDynamicCriteria(minSalary, permanent, deptId);
    }

    /** Search employees by name keyword (Criteria API). */
    @Transactional(readOnly = true)
    public List<Employee> findByNameContaining(String keyword) {
        return criteriaRepository.findByNameContaining(keyword);
    }
}
