package com.cognizant.springlearn.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * SecurityConfig — Spring Security configuration class.
 *
 * ---------------------------------------------------------------
 * Step 1 (Basic Security):
 *   @EnableWebSecurity restricts ALL endpoints.
 *   Without credentials → HTTP 401 Unauthorized.
 *   Spring auto-generates a password in logs; use with "user:generated-password".
 *
 * Step 2 (Users and Roles):
 *   configure(AuthenticationManagerBuilder) — defines two in-memory users:
 *     user  / pwd  — ROLE_USER  (can access /countries)
 *     admin / pwd  — ROLE_ADMIN (can access /authenticate only)
 *   configure(HttpSecurity) — URL authorization rules:
 *     /countries   → ROLE_USER only
 *     /authenticate → ROLE_USER or ROLE_ADMIN
 *
 *   Limitation: Basic Auth sends credentials Base64-encoded on EVERY request.
 *   Base64 is NOT encryption — it can be decoded easily.
 *   Solution: JWT — send credentials ONCE, receive a token, use token thereafter.
 *
 * Step 3 (JWT):
 *   addFilter(JwtAuthorizationFilter) — intercepts ALL requests.
 *   Validates Bearer token in Authorization header.
 *   anyRequest().authenticated() — all endpoints require valid JWT.
 *
 * PasswordEncoder:
 *   BCrypt hashes passwords with a salt — one-way hash, cannot be reversed.
 *   Required by Spring Security when using inMemoryAuthentication.
 * ---------------------------------------------------------------
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);

    // ---------------------------------------------------------------
    // Step 2: Define in-memory users with roles
    // ---------------------------------------------------------------

    /**
     * Configures in-memory authentication with two users.
     *
     * auth.inMemoryAuthentication() — stores users in memory (no DB needed).
     * passwordEncoder().encode("pwd") — BCrypt-hashes the plain text password.
     * .roles("USER") — assigns ROLE_USER (Spring prefixes "ROLE_" automatically).
     *
     * NOTE: In production, use a database (UserDetailsService + JPA).
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        LOGGER.info("START configure(AuthenticationManagerBuilder)");
        auth.inMemoryAuthentication()
            .withUser("admin")
                .password(passwordEncoder().encode("pwd"))
                .roles("ADMIN")
            .and()
            .withUser("user")
                .password(passwordEncoder().encode("pwd"))
                .roles("USER");
        LOGGER.info("END configure(AuthenticationManagerBuilder) — users: admin(ADMIN), user(USER)");
    }

    /**
     * BCryptPasswordEncoder bean.
     * BCrypt is a one-way hash algorithm — passwords cannot be reversed.
     * Spring Security uses this to compare incoming password with stored hash.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        LOGGER.info("Start passwordEncoder()");
        return new BCryptPasswordEncoder();
    }

    // ---------------------------------------------------------------
    // Step 3: URL authorization + JWT filter
    // ---------------------------------------------------------------

    /**
     * Configures HTTP security rules:
     *
     * .csrf().disable()
     *   — disables CSRF protection (not needed for stateless REST APIs).
     *
     * .httpBasic()
     *   — enables HTTP Basic Authentication (used for /authenticate endpoint).
     *   — client sends "Authorization: Basic Base64(user:pwd)" header.
     *
     * .antMatchers("/authenticate").hasAnyRole("USER","ADMIN")
     *   — /authenticate accessible to both USER and ADMIN roles.
     *   — Used with Basic Auth to receive a JWT token.
     *
     * .anyRequest().authenticated()
     *   — all other endpoints require authentication (JWT Bearer token).
     *
     * .addFilter(new JwtAuthorizationFilter(authenticationManager()))
     *   — registers our custom JWT filter in the Spring Security filter chain.
     *   — JwtAuthorizationFilter extends BasicAuthenticationFilter.
     *   — It checks "Authorization: Bearer <token>" on every request.
     *
     * Comment about /countries antMatcher:
     *   The explicit /countries role check is commented out because
     *   anyRequest().authenticated() with JWT covers all endpoints.
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        LOGGER.info("START configure(HttpSecurity)");

        httpSecurity
            .csrf().disable()
            .httpBasic()
            .and()
            .authorizeRequests()
                // Step 2: Uncomment below for role-based access (Basic Auth only)
                // .antMatchers("/countries").hasRole("USER")
                .antMatchers("/authenticate").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
            .and()
            // Step 3: JWT filter — validates Bearer token on all requests
            .addFilter(new JwtAuthorizationFilter(authenticationManager()));

        LOGGER.info("END configure(HttpSecurity)");
    }
}
