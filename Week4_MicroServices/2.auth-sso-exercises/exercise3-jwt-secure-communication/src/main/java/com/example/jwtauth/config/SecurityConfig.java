package com.example.jwtauth.config;

import com.example.jwtauth.security.JwtTokenFilter;
import com.example.jwtauth.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * NOTE ON THE ORIGINAL EXERCISE SHEET: the sheet's SecurityConfig extends
 * WebSecurityConfigurerAdapter and calls http.authorizeRequests()...
 * .addFilterBefore(...) — removed in Spring Security 6 (Spring Boot 3) in
 * favor of a SecurityFilterChain @Bean with the lambda DSL, used below.
 * Two other additions beyond the sheet: CSRF is disabled and the session
 * is set to STATELESS, both standard for a pure bearer-token JWT API
 * (with sessions enabled, Spring Security would otherwise still expect
 * a CSRF token / session cookie for state-changing requests).
 */
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtTokenProvider jwtTokenProvider) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/auth/login").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(new JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
