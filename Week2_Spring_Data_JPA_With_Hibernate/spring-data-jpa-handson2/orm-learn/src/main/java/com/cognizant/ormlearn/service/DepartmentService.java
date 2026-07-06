package com.cognizant.ormlearn.service;

import com.cognizant.ormlearn.model.Department;
import com.cognizant.ormlearn.repository.DepartmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * DepartmentService — service layer for Department operations.
 * Hands-on 4, 5.
 */
@Service
public class DepartmentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentService.class);

    @Autowired
    private DepartmentRepository departmentRepository;

    /**
     * Fetch department by id.
     * With FetchType.EAGER on employeeList, the employee set is also loaded.
     */
    @Transactional
    public Department get(int id) {
        LOGGER.info("Start get(id={})", id);
        return departmentRepository.findById(id).get();
    }

    /**
     * Persist or update a department.
     */
    @Transactional
    public void save(Department department) {
        LOGGER.info("Start save");
        departmentRepository.save(department);
        LOGGER.info("End save, department={}", department);
    }
}
