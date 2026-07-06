package com.cognizant.springlearn.dao;

import com.cognizant.springlearn.model.Department;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

/**
 * DepartmentDao — Data Access Object for Department data.
 *
 * @Repository — Spring-managed DAO bean.
 *
 * DEPARTMENT_LIST is a static variable populated ONCE in the constructor
 * from the "departmentList" bean in employee.xml.
 *
 * REST endpoint: GET /departments → calls DepartmentService → DepartmentDao
 */
@Repository
public class DepartmentDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentDao.class);

    /**
     * Static department list — cached from employee.xml at startup.
     */
    private static ArrayList<Department> DEPARTMENT_LIST;

    /**
     * Constructor — loads department list from employee.xml.
     * Populates DEPARTMENT_LIST from the "departmentList" ArrayList bean.
     */
    public DepartmentDao() {
        LOGGER.info("START DepartmentDao constructor — loading departments from employee.xml");

        ApplicationContext context =
                new ClassPathXmlApplicationContext("employee.xml");

        @SuppressWarnings("unchecked")
        ArrayList<Department> list =
                (ArrayList<Department>) context.getBean("departmentList", ArrayList.class);

        DEPARTMENT_LIST = list;

        LOGGER.debug("Loaded {} departments into DEPARTMENT_LIST", DEPARTMENT_LIST.size());
        ((ClassPathXmlApplicationContext) context).close();

        LOGGER.info("END DepartmentDao constructor");
    }

    /**
     * Returns the complete department list.
     *
     * @return ArrayList of all Department objects loaded from employee.xml
     */
    public ArrayList<Department> getAllDepartments() {
        LOGGER.info("START getAllDepartments");
        LOGGER.debug("Returning {} departments", DEPARTMENT_LIST.size());
        LOGGER.info("END getAllDepartments");
        return DEPARTMENT_LIST;
    }

    /**
     * Returns a single department by id.
     *
     * @param id department id
     * @return Department or null if not found
     */
    public Department getDepartmentById(int id) {
        LOGGER.info("START getDepartmentById({})", id);
        Department found = DEPARTMENT_LIST.stream()
                .filter(d -> d.getId() == id)
                .findFirst()
                .orElse(null);
        LOGGER.debug("Found: {}", found);
        LOGGER.info("END getDepartmentById");
        return found;
    }
}
