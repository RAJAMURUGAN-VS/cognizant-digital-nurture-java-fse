package com.cognizant.springlearn;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SpringLearnApplicationTests — tests covering all 6 hands-on exercises.
 *
 * @SpringBootTest loads the full Spring application context.
 * Individual XML context tests use ClassPathXmlApplicationContext directly.
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SpringLearnApplicationTests {

    // ---------------------------------------------------------------
    // Exercise 1: Application context loads
    // ---------------------------------------------------------------

    @Test @Order(1)
    @DisplayName("H1: Spring Boot application context loads without errors")
    void contextLoads() {
        // If this test passes, the Spring Boot application started successfully.
        // Verifies @SpringBootApplication, auto-configuration, embedded Tomcat.
    }

    // ---------------------------------------------------------------
    // Exercise 2: SimpleDateFormat bean from Spring XML
    // ---------------------------------------------------------------

    @Test @Order(2)
    @DisplayName("H2: date-format.xml loads SimpleDateFormat bean")
    void dateFormatBean_loadsFromXml() {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("date-format.xml");

        SimpleDateFormat format =
                context.getBean("dateFormat", SimpleDateFormat.class);

        assertNotNull(format, "dateFormat bean should not be null");
        assertEquals("dd/MM/yyyy", format.toPattern(),
                "Pattern should match constructor-arg in XML");

        ((ClassPathXmlApplicationContext) context).close();
    }

    @Test @Order(3)
    @DisplayName("H2: SimpleDateFormat bean parses '31/12/2018' correctly")
    void dateFormatBean_parsesDateCorrectly() throws ParseException {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("date-format.xml");
        SimpleDateFormat format =
                context.getBean("dateFormat", SimpleDateFormat.class);

        Date date = format.parse("31/12/2018");
        assertNotNull(date);

        // Re-format the parsed date and verify it round-trips correctly
        String formatted = format.format(date);
        assertEquals("31/12/2018", formatted);

        ((ClassPathXmlApplicationContext) context).close();
    }

    // ---------------------------------------------------------------
    // Exercise 4: Country bean from Spring XML (Setter Injection)
    // ---------------------------------------------------------------

    @Test @Order(4)
    @DisplayName("H4: country.xml loads Country bean via setter injection")
    void countryBean_loadsFromXml() {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("country.xml");

        Country country = context.getBean("country", Country.class);

        assertNotNull(country, "country bean should not be null");
        assertEquals("IN", country.getCode(), "Code should be IN (India)");
        assertEquals("India", country.getName(), "Name should be India");

        ((ClassPathXmlApplicationContext) context).close();
    }

    // ---------------------------------------------------------------
    // Exercise 5: Singleton scope (default behaviour)
    // ---------------------------------------------------------------

    @Test @Order(5)
    @DisplayName("H5: singleton scope returns same instance on two getBean() calls")
    void singletonScope_returnsSameInstance() {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("country.xml");

        Country country        = context.getBean("country", Country.class);
        Country anotherCountry = context.getBean("country", Country.class);

        // SINGLETON: both references point to the SAME object
        assertSame(country, anotherCountry,
                "Singleton scope should return the same instance");

        ((ClassPathXmlApplicationContext) context).close();
    }

    // ---------------------------------------------------------------
    // Exercise 6: List of countries from Spring XML
    // ---------------------------------------------------------------

    @Test @Order(6)
    @DisplayName("H6: countryList bean contains exactly 4 countries")
    @SuppressWarnings("unchecked")
    void countryList_containsFourCountries() {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("country.xml");

        List<Country> countries = context.getBean("countryList", List.class);

        assertNotNull(countries, "countryList should not be null");
        assertEquals(4, countries.size(),
                "Should contain exactly 4 countries: IN, US, DE, JP");

        ((ClassPathXmlApplicationContext) context).close();
    }

    @Test @Order(7)
    @DisplayName("H6: countryList contains IN, US, DE, JP")
    @SuppressWarnings("unchecked")
    void countryList_containsAllFourCodes() {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("country.xml");

        List<Country> countries = context.getBean("countryList", List.class);

        assertTrue(countries.stream().anyMatch(c -> "IN".equals(c.getCode())),
                "Should contain India (IN)");
        assertTrue(countries.stream().anyMatch(c -> "US".equals(c.getCode())),
                "Should contain United States (US)");
        assertTrue(countries.stream().anyMatch(c -> "DE".equals(c.getCode())),
                "Should contain Germany (DE)");
        assertTrue(countries.stream().anyMatch(c -> "JP".equals(c.getCode())),
                "Should contain Japan (JP)");

        ((ClassPathXmlApplicationContext) context).close();
    }

    @Test @Order(8)
    @DisplayName("H6: individual country beans (in, us, de, jp) load correctly")
    void individualCountryBeans_loadCorrectly() {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("country.xml");

        Country india  = context.getBean("in", Country.class);
        Country us     = context.getBean("us", Country.class);
        Country germany= context.getBean("de", Country.class);
        Country japan  = context.getBean("jp", Country.class);

        assertEquals("India",         india.getName());
        assertEquals("United States", us.getName());
        assertEquals("Germany",       germany.getName());
        assertEquals("Japan",         japan.getName());

        ((ClassPathXmlApplicationContext) context).close();
    }
}
