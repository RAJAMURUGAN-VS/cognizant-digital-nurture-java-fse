package com.example.ems;

import com.example.ems.entity.Department;
import com.example.ems.entity.Employee;
import com.example.ems.projection.EmployeeNameEmailProjection;
import com.example.ems.projection.EmployeeSummaryDTO;
import com.example.ems.repository.DepartmentRepository;
import com.example.ems.repository.EmployeeRepository;
import com.example.ems.service.DepartmentService;
import com.example.ems.service.EmployeeService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests covering all 10 exercises.
 * Uses H2 in-memory database — no external setup needed.
 * Run: mvn test
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EmployeeManagementSystemTests {

    @Autowired private EmployeeService    employeeService;
    @Autowired private DepartmentService  departmentService;
    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private DepartmentRepository departmentRepository;

    // ---------------------------------------------------------------
    // Exercise 2 & 3: Entity and Repository tests
    // ---------------------------------------------------------------

    @Test @Order(1)
    @DisplayName("Ex2&3: findAll returns seeded employees")
    void findAll_returnsSeededEmployees() {
        List<Employee> employees = employeeService.findAll();
        assertFalse(employees.isEmpty(), "Should have seeded employees");
    }

    @Test @Order(2)
    @DisplayName("Ex2&3: findAll departments returns seeded departments")
    void findAllDepartments_returnsSeeded() {
        List<Department> departments = departmentService.findAll();
        assertFalse(departments.isEmpty());
    }

    // ---------------------------------------------------------------
    // Exercise 4: CRUD
    // ---------------------------------------------------------------

    @Test @Order(3)
    @DisplayName("Ex4: create and findById employee")
    void createAndFindEmployee() {
        Department dept = departmentService.findById(1L).orElseThrow();
        Employee emp = Employee.builder()
                .name("Test User")
                .email("test@example.com")
                .department(dept)
                .build();
        Employee saved = employeeService.create(emp);
        assertNotNull(saved.getId());

        Optional<Employee> found = employeeService.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Test User", found.get().getName());

        // Cleanup
        employeeService.delete(saved.getId());
    }

    @Test @Order(4)
    @DisplayName("Ex4: update employee name")
    void updateEmployee() {
        Employee existing = employeeService.findAll().get(0);
        String originalName = existing.getName();

        Employee updated = Employee.builder()
                .name("Updated Name")
                .email(existing.getEmail())
                .department(existing.getDepartment())
                .build();
        Employee result = employeeService.update(existing.getId(), updated);
        assertEquals("Updated Name", result.getName());

        // Restore
        updated.setName(originalName);
        employeeService.update(existing.getId(), updated);
    }

    @Test @Order(5)
    @DisplayName("Ex4: delete employee removes it")
    void deleteEmployee() {
        Department dept = departmentService.findById(1L).orElseThrow();
        Employee emp = Employee.builder()
                .name("Delete Me")
                .email("delete@example.com")
                .department(dept)
                .build();
        Employee saved = employeeService.create(emp);
        Long id = saved.getId();

        employeeService.delete(id);
        assertFalse(employeeService.findById(id).isPresent());
    }

    // ---------------------------------------------------------------
    // Exercise 5: Custom Query Methods
    // ---------------------------------------------------------------

    @Test @Order(6)
    @DisplayName("Ex5: findByEmail returns correct employee")
    void findByEmail_returnsEmployee() {
        Optional<Employee> emp = employeeService.findByEmail("alice@example.com");
        assertTrue(emp.isPresent());
        assertEquals("Alice Johnson", emp.get().getName());
    }

    @Test @Order(7)
    @DisplayName("Ex5: findByNameContaining returns matches")
    void findByNameContaining_returnsMatches() {
        List<Employee> results = employeeService.findByNameContaining("Alice");
        assertFalse(results.isEmpty());
        results.forEach(e -> assertTrue(
                e.getName().toLowerCase().contains("alice")));
    }

    @Test @Order(8)
    @DisplayName("Ex5: findAllWithDepartment uses JOIN FETCH")
    void findAllWithDepartment_loadsDepartment() {
        List<Employee> employees = employeeService.findAllWithDepartment();
        assertFalse(employees.isEmpty());
        // Department should be populated
        employees.forEach(e -> assertNotNull(e.getDepartment()));
    }

    @Test @Order(9)
    @DisplayName("Ex5: findByDepartmentName (derived query)")
    void findByDepartmentName_returnsResults() {
        List<Employee> employees = employeeRepository.findByDepartmentName("Engineering");
        assertFalse(employees.isEmpty());
    }

    // ---------------------------------------------------------------
    // Exercise 6: Pagination and Sorting
    // ---------------------------------------------------------------

    @Test @Order(10)
    @DisplayName("Ex6: findPaginated returns correct page size")
    void findPaginated_returnsCorrectPageSize() {
        Page<Employee> page = employeeService.findPaginated(0, 2, "name", "asc");
        assertEquals(2, page.getContent().size());
        assertTrue(page.getTotalElements() >= 2);
        assertNotNull(page.getTotalPages());
    }

    @Test @Order(11)
    @DisplayName("Ex6: findPaginated is sorted by name ascending")
    void findPaginated_isSortedAscending() {
        Page<Employee> page = employeeService.findPaginated(0, 10, "name", "asc");
        List<Employee> content = page.getContent();
        for (int i = 0; i < content.size() - 1; i++) {
            assertTrue(
                content.get(i).getName().compareToIgnoreCase(content.get(i + 1).getName()) <= 0
            );
        }
    }

    @Test @Order(12)
    @DisplayName("Ex6: Sort departments by name descending")
    void sortDepartmentsByNameDesc() {
        List<Department> departments = departmentService.findAllSortedByName("desc");
        assertFalse(departments.isEmpty());
        for (int i = 0; i < departments.size() - 1; i++) {
            assertTrue(
                departments.get(i).getName().compareToIgnoreCase(
                        departments.get(i + 1).getName()) >= 0
            );
        }
    }

    // ---------------------------------------------------------------
    // Exercise 7: Auditing
    // ---------------------------------------------------------------

    @Test @Order(13)
    @DisplayName("Ex7: createdDate and createdBy are populated on save")
    void auditingFieldsPopulatedOnSave() {
        Department dept = departmentService.findById(1L).orElseThrow();
        Employee emp = Employee.builder()
                .name("Audit Test")
                .email("audit@example.com")
                .department(dept)
                .build();
        Employee saved = employeeService.create(emp);

        assertNotNull(saved.getCreatedDate(),   "createdDate should be set");
        assertNotNull(saved.getCreatedBy(),     "createdBy should be set");
        assertEquals("system", saved.getCreatedBy());

        // Cleanup
        employeeService.delete(saved.getId());
    }

    // ---------------------------------------------------------------
    // Exercise 8: Projections
    // ---------------------------------------------------------------

    @Test @Order(14)
    @DisplayName("Ex8: interface projection returns name and email only")
    void interfaceProjection_returnsNameAndEmail() {
        List<EmployeeNameEmailProjection> projections = employeeService.findAllNameEmailProjection();
        assertFalse(projections.isEmpty());
        projections.forEach(p -> {
            assertNotNull(p.getName());
            assertNotNull(p.getEmail());
            assertNotNull(p.getNameWithEmail()); // @Value computed field
            assertTrue(p.getNameWithEmail().contains("<"));
        });
    }

    @Test @Order(15)
    @DisplayName("Ex8: DTO projection returns id, name, departmentName")
    void dtoProjection_returnsCorrectFields() {
        List<EmployeeSummaryDTO> dtos = employeeService.findAllAsSummaryDTO();
        assertFalse(dtos.isEmpty());
        dtos.forEach(dto -> {
            assertNotNull(dto.getId());
            assertNotNull(dto.getName());
            // departmentName may be null if employee has no dept
        });
    }

    @Test @Order(16)
    @DisplayName("Ex8: department summary projection returns id and name")
    void departmentSummaryProjection_returnsIdAndName() {
        var projections = departmentService.findAllSummary();
        assertFalse(projections.isEmpty());
        projections.forEach(p -> {
            assertNotNull(p.getId());
            assertNotNull(p.getName());
        });
    }

    // ---------------------------------------------------------------
    // Exercise 10: Batch Processing
    // ---------------------------------------------------------------

    @Test @Order(17)
    @DisplayName("Ex10: batchInsert inserts 20 employees successfully")
    void batchInsert_insertsEmployees() {
        long before = employeeRepository.count();
        employeeService.demoBatchInsert(20);
        long after  = employeeRepository.count();
        assertEquals(before + 20, after, "Should have inserted 20 batch employees");
    }
}
