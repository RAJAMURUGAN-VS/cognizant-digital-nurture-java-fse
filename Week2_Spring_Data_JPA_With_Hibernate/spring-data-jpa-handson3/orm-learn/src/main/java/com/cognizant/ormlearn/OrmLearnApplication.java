package com.cognizant.ormlearn;

import com.cognizant.ormlearn.model.Employee;
import com.cognizant.ormlearn.model.quiz.Attempt;
import com.cognizant.ormlearn.model.quiz.AttemptOption;
import com.cognizant.ormlearn.model.quiz.AttemptQuestion;
import com.cognizant.ormlearn.service.AttemptService;
import com.cognizant.ormlearn.service.DepartmentService;
import com.cognizant.ormlearn.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * OrmLearnApplication — main class for all 6 hands-on exercises.
 *
 * Hands-on 2 : HQL permanent employees (basic + optimised with fetch)
 * Hands-on 3 : Quiz attempt detail (HQL with nested fetch chain)
 * Hands-on 4 : HQL AVG aggregate — average salary
 * Hands-on 5 : Native Query — SELECT * FROM employee
 * Hands-on 6 : Criteria Query — dynamic filter (Amazon filter scenario)
 */
@SpringBootApplication
public class OrmLearnApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrmLearnApplication.class);

    private static EmployeeService   employeeService;
    private static DepartmentService departmentService;
    private static AttemptService    attemptService;

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(OrmLearnApplication.class, args);
        LOGGER.info("Inside main");

        employeeService   = context.getBean(EmployeeService.class);
        departmentService = context.getBean(DepartmentService.class);
        attemptService    = context.getBean(AttemptService.class);

        // Hands-on 2
        testGetAllPermanentEmployees();
        testGetAllPermanentEmployeesOptimised();

        // Hands-on 3
        testGetAttemptDetails();

        // Hands-on 4
        testGetAverageSalary();
        testGetAverageSalaryByDepartment();

        // Hands-on 5
        testGetAllEmployeesNative();

        // Hands-on 6
        testCriteriaQuery();
    }

    // ================================================================
    // HANDS-ON 2: HQL — Get all permanent employees
    // ================================================================

    /**
     * Basic HQL without fetch optimisation.
     * Observe the logs — multiple SQL queries fired for skills per employee.
     */
    private static void testGetAllPermanentEmployees() {
        LOGGER.info("Start testGetAllPermanentEmployees");
        List<Employee> employees = employeeService.getAllPermanentEmployees();
        LOGGER.debug("Permanent Employees: {}", employees);
        employees.forEach(e -> LOGGER.debug("Skills: {}", e.getSkillList()));
        LOGGER.info("End testGetAllPermanentEmployees");
    }

    /**
     * Optimised HQL with LEFT JOIN FETCH.
     * Observe: only ONE SQL query fires — all data loaded in a single round-trip.
     */
    private static void testGetAllPermanentEmployeesOptimised() {
        LOGGER.info("Start testGetAllPermanentEmployeesOptimised");
        List<Employee> employees = employeeService.getAllPermanentEmployeesOptimised();
        LOGGER.debug("Permanent Employees (optimised): {}", employees);
        employees.forEach(e -> {
            LOGGER.debug("  Employee: {} | Department: {} | Skills: {}",
                    e.getName(), e.getDepartment(), e.getSkillList());
        });
        LOGGER.info("End testGetAllPermanentEmployeesOptimised");
    }

    // ================================================================
    // HANDS-ON 3: Quiz attempt details
    // ================================================================

    /**
     * Fetches and displays quiz attempt detail for user id=1, attempt id=1.
     *
     * Output format:
     *   What is the extension of the hyper text markup language file?
     *   1) .xhtm       0.0     false
     *   2) .ht         0.0     false
     *   3) .html       1.0     true
     *   4) .htmx       0.0     false
     */
    private static void testGetAttemptDetails() {
        LOGGER.info("Start testGetAttemptDetails");

        Attempt attempt = attemptService.getAttempt(1, 1);

        if (attempt == null) {
            LOGGER.warn("No attempt found for userId=1, attemptId=1");
            return;
        }

        LOGGER.info("User: {} | AttemptedOn: {}",
                attempt.getUser().getUsername(), attempt.getAttemptedOn());

        for (AttemptQuestion aq : attempt.getAttemptQuestions()) {
            // Print question text
            LOGGER.info("{}", aq.getQuestion().getText());

            // Print each option with score and selection status
            int optionNum = 1;
            for (AttemptOption ao : aq.getAttemptOptions()) {
                LOGGER.info("  {}) {:<15} {}     {}",
                        optionNum++,
                        ao.getOption().getText(),
                        ao.getOption().isCorrect()
                                ? aq.getQuestion().getScore()
                                : "0.0",
                        ao.isSelected());
            }
        }

        LOGGER.info("End testGetAttemptDetails");
    }

    // ================================================================
    // HANDS-ON 4: HQL aggregate — AVG salary
    // ================================================================

    private static void testGetAverageSalary() {
        LOGGER.info("Start testGetAverageSalary");
        double avg = employeeService.getAverageSalary();
        LOGGER.debug("Average salary (all employees): {}", avg);
        LOGGER.info("End testGetAverageSalary");
    }

    private static void testGetAverageSalaryByDepartment() {
        LOGGER.info("Start testGetAverageSalaryByDepartment");
        // Test for department id=1 (Engineering)
        double avg = employeeService.getAverageSalaryByDepartment(1);
        LOGGER.debug("Average salary for department id=1: {}", avg);
        LOGGER.info("End testGetAverageSalaryByDepartment");
    }

    // ================================================================
    // HANDS-ON 5: Native Query
    // ================================================================

    private static void testGetAllEmployeesNative() {
        LOGGER.info("Start testGetAllEmployeesNative");
        List<Employee> employees = employeeService.getAllEmployeesNative();
        LOGGER.debug("Employees (native query): count={}", employees.size());
        employees.forEach(e -> LOGGER.debug("  {}", e));
        LOGGER.info("End testGetAllEmployeesNative");
    }

    // ================================================================
    // HANDS-ON 6: Criteria Query — dynamic filter
    // ================================================================

    private static void testCriteriaQuery() {
        LOGGER.info("Start testCriteriaQuery");

        // 6a: All employees (no filter)
        LOGGER.debug("--- All employees (Criteria, no filter) ---");
        employeeService.findAllCriteria()
                .forEach(e -> LOGGER.debug("  {}", e));

        // 6b: Salary > 70000
        LOGGER.debug("--- Employees with salary > 70000 ---");
        employeeService.findBySalaryGreaterThan(70000)
                .forEach(e -> LOGGER.debug("  {}", e));

        // 6c: Permanent employees only
        LOGGER.debug("--- Permanent employees ---");
        employeeService.findPermanentEmployees()
                .forEach(e -> LOGGER.debug("  {}", e));

        // 6d: Dynamic filter — permanent=true AND dept=1 (Amazon filter scenario)
        LOGGER.debug("--- Dynamic: permanent=true, dept=1 ---");
        employeeService.findByDynamicCriteria(null, true, 1)
                .forEach(e -> LOGGER.debug("  {}", e));

        // 6e: Dynamic filter — salary >= 80000 (no dept, no permanent filter)
        LOGGER.debug("--- Dynamic: salary >= 80000 ---");
        employeeService.findByDynamicCriteria(80000.0, null, null)
                .forEach(e -> LOGGER.debug("  {}", e));

        // 6f: Name contains "a"
        LOGGER.debug("--- Name contains 'a' ---");
        employeeService.findByNameContaining("a")
                .forEach(e -> LOGGER.debug("  {}", e));

        LOGGER.info("End testCriteriaQuery");
    }
}
