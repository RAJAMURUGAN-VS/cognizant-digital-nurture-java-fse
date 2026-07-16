package com.example.oauth2login.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for OAuth2/OIDC login.
 *
 * NOTE ON THE ORIGINAL EXERCISE SHEET: the sheet's SecurityConfig extends
 * WebSecurityConfigurerAdapter and calls http.authorizeRequests(). Both
 * were removed in Spring Security 6 (which Spring Boot 3 uses) in favor
 * of a SecurityFilterChain @Bean and the authorizeHttpRequests() DSL,
 * which is what's used below.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/oauth2/authorization/my-client")
            );

        return http.build();
    }
}
