package com.cognizant.springlearn.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * JwtAuthorizationFilter — Spring Security filter for JWT validation.
 *
 * Extends BasicAuthenticationFilter — inserted into Spring's filter chain.
 * This filter intercepts ALL incoming HTTP requests BEFORE they reach
 * any controller.
 *
 * JWT Authorization Flow:
 *   1. Client calls GET /authenticate with Basic Auth (user:pwd).
 *   2. AuthenticationController validates credentials, generates JWT.
 *   3. Client receives JWT token in response: {"token":"eyJ..."}
 *   4. For all subsequent requests, client includes:
 *      Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
 *   5. THIS FILTER intercepts the request, extracts Bearer token,
 *      validates signature using "secretkey", extracts username.
 *   6. If valid: sets authenticated user in SecurityContextHolder.
 *   7. If missing/invalid: request proceeds unauthenticated (Spring handles 401).
 *
 * Why a filter and not a controller?
 *   A controller handles ONE specific URL.
 *   A filter intercepts ALL requests — perfect for cross-cutting security logic.
 *
 * JWT Structure (for reference):
 *   Header.Payload.Signature
 *   Each part is Base64Url encoded.
 *   Signature = HMACSHA256(header + "." + payload, "secretkey")
 *   If someone tampers with the token, signature verification fails.
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

    private static final String JWT_SECRET = "secretkey";
    private static final String BEARER_PREFIX = "Bearer ";

    /**
     * Constructor — passes AuthenticationManager to parent class.
     * Called from SecurityConfig.configure(HttpSecurity).
     */
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        LOGGER.info("Start JwtAuthorizationFilter constructor");
        LOGGER.debug("AuthenticationManager: {}", authenticationManager);
    }

    // ---------------------------------------------------------------
    // doFilterInternal — called for every HTTP request
    // ---------------------------------------------------------------

    /**
     * Intercepts every HTTP request.
     *
     * Logic:
     *   1. Read "Authorization" header.
     *   2. If null or doesn't start with "Bearer " → pass through (no JWT auth).
     *      - This allows /authenticate (which uses Basic Auth) to pass through.
     *   3. If starts with "Bearer " → validate JWT token.
     *   4. If valid → set authenticated user in SecurityContextHolder.
     *   5. Pass request to next filter/controller via chain.doFilter().
     *
     * Test commands:
     *   # With valid token (replace TOKEN with value from /authenticate):
     *   curl -s -H "Authorization: Bearer TOKEN" http://localhost:8083/countries
     *
     *   # With tampered token → 401 Unauthorized
     *   curl -s -H "Authorization: Bearer invalid.token.here" http://localhost:8083/countries
     */
    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain)
            throws IOException, ServletException {

        LOGGER.info("Start doFilterInternal — URI: {}", req.getRequestURI());

        String header = req.getHeader("Authorization");
        LOGGER.debug("Authorization header: {}", header);

        // If no Bearer token, pass request through without JWT authentication
        // (e.g. /authenticate uses Basic Auth and is handled separately)
        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            LOGGER.debug("No Bearer token found — passing through filter chain");
            chain.doFilter(req, res);
            return;
        }

        // Validate the Bearer JWT token
        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        if (authentication != null) {
            // Token valid — set authentication in Spring Security context
            SecurityContextHolder.getContext().setAuthentication(authentication);
            LOGGER.debug("JWT valid — user '{}' authenticated", authentication.getName());
        } else {
            LOGGER.warn("JWT validation failed — request will be rejected");
        }

        chain.doFilter(req, res);
        LOGGER.info("End doFilterInternal");
    }

    // ---------------------------------------------------------------
    // getAuthentication — parse and validate JWT
    // ---------------------------------------------------------------

    /**
     * Parses and validates the JWT token from the Authorization header.
     *
     * Steps:
     *   1. Extract token string after "Bearer ".
     *   2. Parse using Jwts.parser().setSigningKey("secretkey").
     *      - Verifies HMACSHA256 signature using "secretkey".
     *      - If token was tampered, SignatureException is thrown.
     *      - If token expired, ExpiredJwtException is thrown.
     *   3. Extract subject (username) from token payload.
     *   4. Return UsernamePasswordAuthenticationToken with username.
     *      - Spring Security uses this to identify the authenticated user.
     *      - Third parameter (new ArrayList<>()) = empty authorities list.
     *
     * Returns null if:
     *   - Token is null.
     *   - Token signature is invalid (tampered).
     *   - Token has expired.
     *
     * @param request incoming HTTP request
     * @return authentication token or null if JWT is invalid
     */
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (token != null) {
            try {
                // Parse JWT — verify signature and extract claims
                Jws<Claims> jws = Jwts.parser()
                        .setSigningKey(JWT_SECRET)
                        .parseClaimsJws(token.replace(BEARER_PREFIX, ""));

                // Extract username from 'sub' (subject) claim
                String user = jws.getBody().getSubject();
                LOGGER.debug("JWT subject (user): {}", user);

                if (user != null) {
                    // Create Spring Security authentication token
                    // Parameters: principal, credentials, authorities
                    return new UsernamePasswordAuthenticationToken(
                            user, null, new ArrayList<>());
                }

            } catch (JwtException ex) {
                // Invalid signature, expired token, malformed JWT etc.
                LOGGER.warn("JWT validation failed: {}", ex.getMessage());
                return null;
            }

            return null;
        }
        return null;
    }
}
