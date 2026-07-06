package com.cognizant.ormlearn.service;

import com.cognizant.ormlearn.model.Employee;
import com.cognizant.ormlearn.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * EmployeeService — service layer for Employee operations.
 * Hands-on 4, 5, 6.
 *
 * @Transactional ensures Hibernate session is open during the entire method,
 * preventing LazyInitializationException when accessing lazy-loaded associations.
 */
@Service
public class EmployeeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Fetch employee by id.
     * The Hibernate session remains open during this method — lazy collections
     * can be accessed safely within @Transactional scope.
     */
    @Transactional
    public Employee get(int id) {
        LOGGER.info("Start get(id={})", id);
        return employeeRepository.findById(id).get();
    }

    /**
     * Persist or update an employee.
     * save() issues INSERT if id=0, UPDATE otherwise.
     */
    @Transactional
    public void save(Employee employee) {
        LOGGER.info("Start save");
        employeeRepository.save(employee);
        LOGGER.info("End save, employee={}", employee);
    }
}
