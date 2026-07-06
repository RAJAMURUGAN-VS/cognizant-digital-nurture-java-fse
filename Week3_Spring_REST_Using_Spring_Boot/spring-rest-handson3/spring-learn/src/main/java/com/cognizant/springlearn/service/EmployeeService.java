package com.cognizant.springlearn.service;

import com.cognizant.springlearn.dao.EmployeeDao;
import com.cognizant.springlearn.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * EmployeeService — business logic layer for employee operations.
 *
 * @Service — changed from @Component to @Service as per hands-on requirement.
 *   @Service is a specialisation of @Component; it marks this class as a
 *   service-layer bean and is semantically more descriptive.
 *
 * @Transactional — defined on service methods as per hands-on requirement.
 *   Ensures each method runs within a transaction context.
 *   In this demo (no DB), it enables Spring's transaction proxy mechanism
 *   and is the correct pattern for service methods in Spring applications.
 *
 * Architecture: EmployeeController → EmployeeService → EmployeeDao
 */
@Service
public class EmployeeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);

    @Autowired
    private EmployeeDao employeeDao;

    /**
     * Returns all employees.
     * Invokes employeeDao.getAllEmployees() and returns the result.
     *
     * @Transactional — wraps this method in a Spring transaction.
     * readOnly=true  — signals no write operations; allows optimisations.
     *
     * @return List of all employees loaded from employee.xml
     */
    @Transactional(readOnly = true)
    public List<Employee> getAllEmployees() {
        LOGGER.info("START getAllEmployees");
        List<Employee> employees = employeeDao.getAllEmployees();
        LOGGER.debug("getAllEmployees() returning {} employees", employees.size());
        LOGGER.info("END getAllEmployees");
        return employees;
    }

    /**
     * Returns a single employee by id.
     *
     * @param id employee id
     * @return Employee or null if not found
     */
    @Transactional(readOnly = true)
    public Employee getEmployeeById(int id) {
        LOGGER.info("START getEmployeeById({})", id);
        Employee employee = employeeDao.getEmployeeById(id);
        LOGGER.debug("Returning: {}", employee);
        LOGGER.info("END getEmployeeById");
        return employee;
    }
}
