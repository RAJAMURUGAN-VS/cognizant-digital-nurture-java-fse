package com.cognizant.springlearn.dao;

import com.cognizant.springlearn.model.Employee;
import com.cognizant.springlearn.service.exception.EmployeeNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * EmployeeDao — DAO for Employee CRUD operations backed by employee.xml.
 *
 * EMPLOYEE_LIST is loaded ONCE from employee.xml in the constructor,
 * then mutated in-memory for update/delete operations.
 */
@Repository
public class EmployeeDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeDao.class);

    /** Static list — shared across all requests (in-memory persistence). */
    private static List<Employee> EMPLOYEE_LIST;

    public EmployeeDao() {
        LOGGER.info("START EmployeeDao constructor — loading from employee.xml");
        ApplicationContext ctx = new ClassPathXmlApplicationContext("employee.xml");
        @SuppressWarnings("unchecked")
        ArrayList<Employee> list =
                (ArrayList<Employee>) ctx.getBean("employeeList", ArrayList.class);
        EMPLOYEE_LIST = new ArrayList<>(list);
        ((ClassPathXmlApplicationContext) ctx).close();
        LOGGER.debug("Loaded {} employees", EMPLOYEE_LIST.size());
        LOGGER.info("END EmployeeDao constructor");
    }

    // ---------------------------------------------------------------
    // READ
    // ---------------------------------------------------------------

    public List<Employee> getAllEmployees() {
        LOGGER.info("getAllEmployees: returning {}", EMPLOYEE_LIST.size());
        return new ArrayList<>(EMPLOYEE_LIST);
    }

    public Employee getEmployeeById(int id) throws EmployeeNotFoundException {
        LOGGER.info("getEmployeeById({})", id);
        return EMPLOYEE_LIST.stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    // ---------------------------------------------------------------
    // CREATE
    // ---------------------------------------------------------------

    public void addEmployee(Employee employee) {
        LOGGER.info("addEmployee: {}", employee);
        // Auto-generate next id
        int nextId = EMPLOYEE_LIST.stream()
                .mapToInt(Employee::getId)
                .max()
                .orElse(0) + 1;
        employee.setId(nextId);
        EMPLOYEE_LIST.add(employee);
        LOGGER.debug("Employee added with id={}", nextId);
    }

    // ---------------------------------------------------------------
    // UPDATE
    // ---------------------------------------------------------------

    /**
     * Updates an existing employee in the list.
     * Matches by employee id; replaces the entry.
     * Throws EmployeeNotFoundException if id not found.
     */
    public void updateEmployee(Employee employee) throws EmployeeNotFoundException {
        LOGGER.info("updateEmployee: {}", employee);
        for (int i = 0; i < EMPLOYEE_LIST.size(); i++) {
            if (EMPLOYEE_LIST.get(i).getId().equals(employee.getId())) {
                EMPLOYEE_LIST.set(i, employee);
                LOGGER.debug("Employee id={} updated", employee.getId());
                return;
            }
        }
        throw new EmployeeNotFoundException(employee.getId());
    }

    // ---------------------------------------------------------------
    // DELETE
    // ---------------------------------------------------------------

    /**
     * Removes employee by id from the list.
     * Throws EmployeeNotFoundException if id not found.
     */
    public void deleteEmployee(int id) throws EmployeeNotFoundException {
        LOGGER.info("deleteEmployee({})", id);
        boolean removed = EMPLOYEE_LIST.removeIf(e -> e.getId() == id);
        if (!removed) {
            throw new EmployeeNotFoundException(id);
        }
        LOGGER.debug("Employee id={} deleted", id);
    }
}
