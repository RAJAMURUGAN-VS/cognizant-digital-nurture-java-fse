package com.cognizant.springlearn.service;

import com.cognizant.springlearn.dao.DepartmentDao;
import com.cognizant.springlearn.model.Department;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * DepartmentService — business logic layer for department operations.
 *
 * @Service — Spring-managed service bean.
 * Architecture: DepartmentController → DepartmentService → DepartmentDao
 *
 * Verify in logs that DepartmentService is called when
 * GET /departments REST endpoint is invoked.
 */
@Service
public class DepartmentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentService.class);

    @Autowired
    private DepartmentDao departmentDao;

    /**
     * Returns all departments.
     * Invokes departmentDao.getAllDepartments() and returns the result.
     *
     * @Transactional — wraps in a Spring transaction (readOnly).
     *
     * @return List of all departments loaded from employee.xml
     */
    @Transactional(readOnly = true)
    public List<Department> getAllDepartments() {
        LOGGER.info("START getAllDepartments");
        List<Department> departments = departmentDao.getAllDepartments();
        LOGGER.debug("getAllDepartments() returning {} departments", departments.size());
        LOGGER.info("END getAllDepartments");
        return departments;
    }

    /**
     * Returns a single department by id.
     *
     * @param id department id
     * @return Department or null if not found
     */
    @Transactional(readOnly = true)
    public Department getDepartmentById(int id) {
        LOGGER.info("START getDepartmentById({})", id);
        Department dept = departmentDao.getDepartmentById(id);
        LOGGER.debug("Returning: {}", dept);
        LOGGER.info("END getDepartmentById");
        return dept;
    }
}
