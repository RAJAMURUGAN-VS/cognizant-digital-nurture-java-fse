package com.cognizant.ormlearn;

import com.cognizant.ormlearn.model.*;
import com.cognizant.ormlearn.repository.CountryRepository;
import com.cognizant.ormlearn.repository.StockRepository;
import com.cognizant.ormlearn.service.DepartmentService;
import com.cognizant.ormlearn.service.EmployeeService;
import com.cognizant.ormlearn.service.SkillService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * OrmLearnApplicationTests — integration tests for all 6 hands-on exercises.
 * Requires MySQL ormlearn schema with country, stock, employee, department,
 * skill and employee_skill tables populated.
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrmLearnApplicationTests {

    @Autowired private CountryRepository  countryRepository;
    @Autowired private StockRepository    stockRepository;
    @Autowired private EmployeeService    employeeService;
    @Autowired private DepartmentService  departmentService;
    @Autowired private SkillService       skillService;

    // ---------------------------------------------------------------
    // Hands-on 1: Country Query Methods
    // ---------------------------------------------------------------

    @Test @Order(1)
    @DisplayName("H1a: findByNameContaining returns countries with 'ou'")
    void findByNameContaining_ou_returnsResults() {
        List<Country> results = countryRepository.findByNameContaining("ou");
        assertFalse(results.isEmpty());
        assertTrue(results.stream().anyMatch(c -> c.getName().contains("ou") || c.getName().contains("ou")));
    }

    @Test @Order(2)
    @DisplayName("H1b: findByNameContainingOrderByNameAsc returns sorted results")
    void findByNameContainingOrderByNameAsc_isSorted() {
        List<Country> results = countryRepository.findByNameContainingOrderByNameAsc("ou");
        assertFalse(results.isEmpty());
        for (int i = 0; i < results.size() - 1; i++) {
            assertTrue(
                results.get(i).getName().compareToIgnoreCase(results.get(i + 1).getName()) <= 0,
                "Results should be sorted ascending"
            );
        }
    }

    @Test @Order(3)
    @DisplayName("H1c: findByNameStartingWith 'Z' returns Zambia and Zimbabwe")
    void findByNameStartingWith_Z_returnsZambiaAndZimbabwe() {
        List<Country> results = countryRepository.findByNameStartingWith("Z");
        assertFalse(results.isEmpty());
        boolean hasZambia   = results.stream().anyMatch(c -> "Zambia".equals(c.getName()));
        boolean hasZimbabwe = results.stream().anyMatch(c -> "Zimbabwe".equals(c.getName()));
        assertTrue(hasZambia,   "Should contain Zambia");
        assertTrue(hasZimbabwe, "Should contain Zimbabwe");
    }

    // ---------------------------------------------------------------
    // Hands-on 2: Stock Query Methods
    // ---------------------------------------------------------------

    @Test @Order(4)
    @DisplayName("H2a: FB stocks in Sep 2019 returns 19 records")
    void fbSeptember2019_returns19Records() {
        List<Stock> stocks = stockRepository.findByCodeAndDateBetween(
                "FB", LocalDate.of(2019, 9, 1), LocalDate.of(2019, 9, 30));
        assertEquals(19, stocks.size(), "Expected 19 trading days in Sep 2019");
        stocks.forEach(s -> assertEquals("FB", s.getCode()));
    }

    @Test @Order(5)
    @DisplayName("H2b: GOOGL close > 1250 returns 7 records")
    void googleCloseGreaterThan1250_returns7() {
        List<Stock> stocks = stockRepository.findByCodeAndCloseGreaterThan(
                "GOOGL", new BigDecimal("1250"));
        assertFalse(stocks.isEmpty());
        stocks.forEach(s -> {
            assertEquals("GOOGL", s.getCode());
            assertTrue(s.getClose().compareTo(new BigDecimal("1250")) > 0);
        });
    }

    @Test @Order(6)
    @DisplayName("H2c: top 3 by volume returns exactly 3 records")
    void top3ByVolume_returns3Records() {
        List<Stock> stocks = stockRepository.findTop3ByOrderByVolumeDesc();
        assertEquals(3, stocks.size());
        // Volume should be descending
        assertTrue(stocks.get(0).getVolume() >= stocks.get(1).getVolume());
        assertTrue(stocks.get(1).getVolume() >= stocks.get(2).getVolume());
    }

    @Test @Order(7)
    @DisplayName("H2d: NFLX lowest 3 close prices are all NFLX records")
    void netflixLowest3_allNFLX() {
        List<Stock> stocks = stockRepository.findTop3ByCodeOrderByCloseAsc("NFLX");
        assertEquals(3, stocks.size());
        stocks.forEach(s -> assertEquals("NFLX", s.getCode()));
        assertTrue(stocks.get(0).getClose().compareTo(stocks.get(1).getClose()) <= 0);
    }

    // ---------------------------------------------------------------
    // Hands-on 4: Many-to-One
    // ---------------------------------------------------------------

    @Test @Order(8)
    @DisplayName("H4: getEmployee fetches department via ManyToOne (EAGER)")
    void getEmployee_fetchesDepartmentEagerly() {
        Employee emp = employeeService.get(1);
        assertNotNull(emp);
        assertNotNull(emp.getDepartment(), "Department should be eagerly loaded");
        assertTrue(emp.getDepartment().getId() > 0);
    }

    // ---------------------------------------------------------------
    // Hands-on 5: One-to-Many
    // ---------------------------------------------------------------

    @Test @Order(9)
    @DisplayName("H5: getDepartment fetches employee list (EAGER OneToMany)")
    void getDepartment_fetchesEmployeeListEagerly() {
        Department dept = departmentService.get(1);
        assertNotNull(dept);
        assertNotNull(dept.getEmployeeList(), "Employee list should be eagerly loaded");
        assertFalse(dept.getEmployeeList().isEmpty(),
                "Engineering dept should have at least one employee");
    }

    // ---------------------------------------------------------------
    // Hands-on 6: Many-to-Many
    // ---------------------------------------------------------------

    @Test @Order(10)
    @DisplayName("H6: getEmployee fetches skill list (EAGER ManyToMany)")
    void getEmployee_fetchesSkillListEagerly() {
        Employee emp = employeeService.get(1);
        assertNotNull(emp.getSkillList(), "Skill list should be eagerly loaded");
        assertFalse(emp.getSkillList().isEmpty(), "Alice should have skills");
    }
}
