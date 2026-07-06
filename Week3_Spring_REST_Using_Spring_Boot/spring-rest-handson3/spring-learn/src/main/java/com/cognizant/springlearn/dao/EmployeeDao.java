package com.cognizant.springlearn.dao;

import com.cognizant.springlearn.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * EmployeeDao — Data Access Object for Employee data.
 *
 * Architecture: Controller → Service → DAO → Spring XML Config
 *
 * @Repository — marks this as a Spring-managed DAO bean.
 *   Also enables Spring's exception translation for persistence exceptions.
 *
 * Design:
 *   EMPLOYEE_LIST is a static variable populated ONCE in the constructor
 *   from employee.xml. This simulates a database read that is cached in
 *   memory — avoids re-reading the XML file on every request.
 *
 * In a real application, this DAO would use JPA/Hibernate to read from
 * a database instead of Spring XML.
 */
@Repository
public class EmployeeDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeDao.class);

    /**
     * Static employee list — populated once from employee.xml.
     * Static so it is shared across all instances of EmployeeDao.
     */
    private static ArrayList<Employee> EMPLOYEE_LIST;

    /**
     * Constructor — loads employee list from employee.xml.
     * Spring calls this constructor when it creates the EmployeeDao bean.
     * The employee list is read from XML and cached in EMPLOYEE_LIST.
     *
     * ClassPathXmlApplicationContext loads beans from the classpath.
     * "employeeList" bean id refers to the ArrayList defined in employee.xml.
     */
    public EmployeeDao() {
        LOGGER.info("START EmployeeDao constructor — loading employees from employee.xml");

        ApplicationContext context =
                new ClassPathXmlApplicationContext("employee.xml");

        // Cast the ArrayList<Employee> from the XML context
        @SuppressWarnings("unchecked")
        ArrayList<Employee> list =
                (ArrayList<Employee>) context.getBean("employeeList", ArrayList.class);

        EMPLOYEE_LIST = list;

        LOGGER.debug("Loaded {} employees into EMPLOYEE_LIST", EMPLOYEE_LIST.size());
        ((ClassPathXmlApplicationContext) context).close();

        LOGGER.info("END EmployeeDao constructor");
    }

    /**
     * Returns the complete employee list.
     * This is the data that the REST GET /employees endpoint returns.
     *
     * @return ArrayList of all Employee objects loaded from employee.xml
     */
    public ArrayList<Employee> getAllEmployees() {
        LOGGER.info("START getAllEmployees");
        LOGGER.debug("Returning {} employees", EMPLOYEE_LIST.size());
        LOGGER.info("END getAllEmployees");
        return EMPLOYEE_LIST;
    }

    /**
     * Returns a single employee by id.
     * Used by GET /employees/{id} endpoint.
     *
     * @param id employee id
     * @return Employee or null if not found
     */
    public Employee getEmployeeById(int id) {
        LOGGER.info("START getEmployeeById({})", id);
        Employee found = EMPLOYEE_LIST.stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElse(null);
        LOGGER.debug("Found: {}", found);
        LOGGER.info("END getEmployeeById");
        return found;
    }
}
