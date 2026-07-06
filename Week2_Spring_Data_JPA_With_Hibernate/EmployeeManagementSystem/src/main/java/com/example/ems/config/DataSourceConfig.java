package com.example.ems.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * DataSourceConfig — Exercise 9: Customised DataSource configuration.
 *
 * Demonstrates:
 *   1. Using @ConfigurationProperties to bind application.properties to a bean.
 *   2. Explicitly creating a DataSource bean (overriding auto-configuration).
 *   3. How to wire multiple data sources (primary + secondary pattern).
 *
 * Spring Boot Auto-Configuration:
 *   Spring Boot reads spring.datasource.* and auto-creates a DataSource.
 *   When we define our own DataSource bean, auto-configuration backs off.
 *
 * Externalised Configuration (Exercise 9):
 *   Connection details come from application.properties — not hard-coded.
 *   Change the DB without recompiling: just update the .properties file.
 *
 * For a SECOND data source, you would:
 *   1. Add a second DataSourceProperties bean (e.g. @ConfigurationProperties("app.datasource.secondary")).
 *   2. Create a second EntityManagerFactory and TransactionManager.
 *   3. Use @Qualifier to inject the right one.
 *   (Not fully implemented here — H2 in-memory doesn't need two sources,
 *    but the pattern is demonstrated below.)
 */
@Configuration
public class DataSourceConfig {

    /**
     * Binds app.datasource.primary.* from application.properties.
     * Allows externalised configuration — change DB URL without code changes.
     */
    @Bean
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties primaryDataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * @Primary — marks this as the default DataSource when multiple exist.
     * Spring Boot's JPA auto-configuration uses this DataSource.
     *
     * initializeSchema() creates a DataSource from the bound properties.
     */
    @Bean
    @Primary
    public DataSource primaryDataSource() {
        return primaryDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }
}
