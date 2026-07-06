package com.cognizant.ormlearn;

import com.cognizant.ormlearn.model.Employee;
import com.cognizant.ormlearn.model.quiz.Attempt;
import com.cognizant.ormlearn.service.AttemptService;
import com.cognizant.ormlearn.service.EmployeeService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * OrmLearnApplicationTests — integration tests for all 6 hands-on exercises.
 * Requires MySQL ormlearn schema with payroll and quiz tables populated.
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrmLearnApplicationTests {

    @Autowired private EmployeeService employeeService;
    @Autowired private AttemptService  attemptService;

    // ---------------------------------------------------------------
    // Hands-on 2: HQL permanent employees
    // ---------------------------------------------------------------

    @Test @Order(1)
    @DisplayName("H2a: getAllPermanentEmployees returns only permanent employees")
    void getAllPermanentEmployees_returnsOnlyPermanent() {
        List<Employee> employees = employeeService.getAllPermanentEmployees();
        assertFalse(employees.isEmpty(), "Should return at least one permanent employee");
        employees.forEach(e -> assertTrue(e.isPermanent(),
                "All returned employees should be permanent"));
    }

    @Test @Order(2)
    @DisplayName("H2b: getAllPermanentEmployeesOptimised returns same results with skills loaded")
    void getAllPermanentEmployeesOptimised_loadsSkills() {
        List<Employee> employees = employeeService.getAllPermanentEmployeesOptimised();
        assertFalse(employees.isEmpty());
        // Skills should be populated (not null) due to LEFT JOIN FETCH
        employees.forEach(e -> assertNotNull(e.getSkillList(),
                "Skill list should be populated by JOIN FETCH"));
    }

    // ---------------------------------------------------------------
    // Hands-on 3: Quiz attempt details
    // ---------------------------------------------------------------

    @Test @Order(3)
    @DisplayName("H3: getAttempt returns attempt with all nested data for user=1 attempt=1")
    void getAttempt_returnsFullDetail() {
        Attempt attempt = attemptService.getAttempt(1, 1);
        assertNotNull(attempt, "Attempt should not be null");
        assertNotNull(attempt.getUser(), "User should be loaded");
        assertEquals(1, attempt.getUser().getId());
        assertFalse(attempt.getAttemptQuestions().isEmpty(),
                "Attempt should have questions");

        attempt.getAttemptQuestions().forEach(aq -> {
            assertNotNull(aq.getQuestion(), "Each attempt question should have a question");
            assertNotNull(aq.getAttemptOptions(), "Each attempt question should have options");
            assertFalse(aq.getAttemptOptions().isEmpty());
        });
    }

    // ---------------------------------------------------------------
    // Hands-on 4: HQL aggregate
    // ---------------------------------------------------------------

    @Test @Order(4)
    @DisplayName("H4a: getAverageSalary returns positive value")
    void getAverageSalary_returnsPositive() {
        double avg = employeeService.getAverageSalary();
        assertTrue(avg > 0, "Average salary should be > 0");
    }

    @Test @Order(5)
    @DisplayName("H4b: getAverageSalaryByDepartment returns positive for dept 1")
    void getAverageSalaryByDept_returnsPositive() {
        double avg = employeeService.getAverageSalaryByDepartment(1);
        assertTrue(avg > 0, "Average salary for dept 1 should be > 0");
    }

    // ---------------------------------------------------------------
    // Hands-on 5: Native query
    // ---------------------------------------------------------------

    @Test @Order(6)
    @DisplayName("H5: getAllEmployeesNative returns employees via native SQL")
    void getAllEmployeesNative_returnsEmployees() {
        List<Employee> employees = employeeService.getAllEmployeesNative();
        assertNotNull(employees);
        assertFalse(employees.isEmpty(), "Native query should return employees");
    }

    // ---------------------------------------------------------------
    // Hands-on 6: Criteria Query
    // ---------------------------------------------------------------

    @Test @Order(7)
    @DisplayName("H6a: findAllCriteria returns all employees")
    void findAllCriteria_returnsAll() {
        List<Employee> all = employeeService.findAllCriteria();
        assertFalse(all.isEmpty());
    }

    @Test @Order(8)
    @DisplayName("H6b: findBySalaryGreaterThan filters correctly")
    void findBySalaryGreaterThan_filtersCorrectly() {
        List<Employee> filtered = employeeService.findBySalaryGreaterThan(70000);
        filtered.forEach(e -> assertTrue(e.getSalary() > 70000,
                "All employees should have salary > 70000"));
    }

    @Test @Order(9)
    @DisplayName("H6c: findPermanentEmployees returns only permanent")
    void findPermanentEmployees_returnsOnlyPermanent() {
        List<Employee> permanent = employeeService.findPermanentEmployees();
        permanent.forEach(e -> assertTrue(e.isPermanent()));
    }

    @Test @Order(10)
    @DisplayName("H6d: findByDynamicCriteria with all nulls returns all employees")
    void findByDynamicCriteria_nullFilters_returnsAll() {
        List<Employee> all = employeeService.findByDynamicCriteria(null, null, null);
        assertFalse(all.isEmpty());
    }

    @Test @Order(11)
    @DisplayName("H6e: findByDynamicCriteria with permanent=true filters correctly")
    void findByDynamicCriteria_permanentFilter_returnsOnlyPermanent() {
        List<Employee> results = employeeService.findByDynamicCriteria(null, true, null);
        results.forEach(e -> assertTrue(e.isPermanent()));
    }

    @Test @Order(12)
    @DisplayName("H6f: findByNameContaining with 'a' returns matches")
    void findByNameContaining_returnsMatches() {
        List<Employee> results = employeeService.findByNameContaining("a");
        results.forEach(e -> assertTrue(
                e.getName().toLowerCase().contains("a"),
                "Name should contain 'a'"));
    }
}
