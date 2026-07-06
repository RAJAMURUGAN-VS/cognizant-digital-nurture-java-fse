package com.cognizant.springlearn.service;

import com.cognizant.springlearn.dao.EmployeeDao;
import com.cognizant.springlearn.model.Employee;
import com.cognizant.springlearn.service.exception.EmployeeNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * EmployeeService — service layer for Employee CRUD REST operations.
 */
@Service
public class EmployeeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);

    @Autowired
    private EmployeeDao employeeDao;

    @Transactional(readOnly = true)
    public List<Employee> getAllEmployees() {
        LOGGER.info("START getAllEmployees");
        List<Employee> list = employeeDao.getAllEmployees();
        LOGGER.info("END getAllEmployees — returning {}", list.size());
        return list;
    }

    @Transactional(readOnly = true)
    public Employee getEmployeeById(int id) throws EmployeeNotFoundException {
        LOGGER.info("START getEmployeeById({})", id);
        Employee e = employeeDao.getEmployeeById(id);
        LOGGER.info("END getEmployeeById");
        return e;
    }

    @Transactional
    public Employee addEmployee(Employee employee) {
        LOGGER.info("START addEmployee: {}", employee);
        employeeDao.addEmployee(employee);
        LOGGER.info("END addEmployee — assigned id={}", employee.getId());
        return employee;
    }

    @Transactional
    public void updateEmployee(Employee employee) throws EmployeeNotFoundException {
        LOGGER.info("START updateEmployee: {}", employee);
        employeeDao.updateEmployee(employee);
        LOGGER.info("END updateEmployee");
    }

    @Transactional
    public void deleteEmployee(int id) throws EmployeeNotFoundException {
        LOGGER.info("START deleteEmployee({})", id);
        employeeDao.deleteEmployee(id);
        LOGGER.info("END deleteEmployee");
    }
}
