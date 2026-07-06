package com.example.ems.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

/**
 * AuditConfig — Exercise 7: Enables JPA Auditing.
 *
 * @EnableJpaAuditing — activates Spring Data JPA auditing.
 *   Spring then automatically populates:
 *     @CreatedDate        — set once at INSERT time.
 *     @LastModifiedDate   — updated at every UPDATE.
 *     @CreatedBy          — set from AuditorAware at INSERT time.
 *     @LastModifiedBy     — updated from AuditorAware at every UPDATE.
 *
 * AuditorAware<String> — Spring calls getCurrentAuditor() to determine
 *   who is making the change. In a real app this would return the
 *   authenticated user's username from Spring Security's SecurityContext.
 *   Here we return a hard-coded value for demonstration.
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditConfig {

    /**
     * AuditorAware bean — provides the current "user" for @CreatedBy
     * and @LastModifiedBy fields.
     *
     * In a real application, replace "system" with:
     *   SecurityContextHolder.getContext()
     *       .getAuthentication().getName()
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        // Returns Optional so Spring knows how to handle absent auditor
        return () -> Optional.of("system");
    }
}
