package com.example.resourceserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for the Resource Server.
 *
 * NOTE ON THE ORIGINAL EXERCISE SHEET: the sheet's ResourceServerConfig
 * extends WebSecurityConfigurerAdapter and calls
 * http.authorizeRequests()....and().oauth2ResourceServer().jwt(). Both
 * WebSecurityConfigurerAdapter and the .and() chaining style were removed
 * in Spring Security 6 (used by Spring Boot 3), replaced by a
 * SecurityFilterChain @Bean using the lambda DSL, as below.
 */
@Configuration
@EnableWebSecurity
public class ResourceServerConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> {})
            );

        return http.build();
    }
}
