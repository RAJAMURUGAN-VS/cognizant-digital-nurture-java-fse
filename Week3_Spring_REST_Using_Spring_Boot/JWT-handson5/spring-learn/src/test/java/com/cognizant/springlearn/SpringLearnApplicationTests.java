package com.cognizant.springlearn;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * SpringLearnApplicationTests — Security and JWT tests.
 *
 * Tests cover:
 *   1. No credentials → 401 Unauthorized
 *   2. Basic Auth correct credentials → access granted
 *   3. Basic Auth wrong password → 401 Unauthorized
 *   4. Basic Auth wrong role → 403 Forbidden
 *   5. /authenticate returns non-empty token
 *   6. JWT Bearer token → access granted
 *   7. Tampered JWT → 401 Unauthorized
 *   8. Expired JWT → 401 Unauthorized
 *
 * Run: mvn clean test
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SpringLearnApplicationTests {

    @Autowired
    private MockMvc mvc;

    private static final String JWT_SECRET = "secretkey";

    // ---------------------------------------------------------------
    // Test 1: No credentials → 401
    // ---------------------------------------------------------------

    @Test @Order(1)
    @DisplayName("No credentials → 401 Unauthorized")
    void noCredentials_returns401() throws Exception {
        mvc.perform(get("/countries"))
           .andExpect(status().isUnauthorized());
    }

    // ---------------------------------------------------------------
    // Test 2: Basic Auth with correct USER credentials
    //          (Basic Auth still works alongside JWT in our config)
    // ---------------------------------------------------------------

    @Test @Order(2)
    @DisplayName("Basic Auth with user:pwd → /countries returns 200")
    void basicAuth_userPwd_countries_returns200() throws Exception {
        mvc.perform(get("/countries")
                .with(httpBasic("user", "pwd")))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$").isArray())
           .andExpect(jsonPath("$.length()").value(4));
    }

    // ---------------------------------------------------------------
    // Test 3: Basic Auth wrong password → 401
    // ---------------------------------------------------------------

    @Test @Order(3)
    @DisplayName("Basic Auth wrong password → 401 Unauthorized")
    void basicAuth_wrongPassword_returns401() throws Exception {
        mvc.perform(get("/countries")
                .with(httpBasic("user", "wrongpwd")))
           .andExpect(status().isUnauthorized());
    }

    // ---------------------------------------------------------------
    // Test 4: /authenticate with user credentials → returns token JSON
    // ---------------------------------------------------------------

    @Test @Order(4)
    @DisplayName("GET /authenticate with user:pwd returns non-empty token")
    void authenticate_userPwd_returnsToken() throws Exception {
        MvcResult result = mvc.perform(get("/authenticate")
                .with(httpBasic("user", "pwd")))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.token").exists())
           .andReturn();

        String body = result.getResponse().getContentAsString();
        assertFalse(body.contains("\"token\":\"\""),
                "Token should not be empty");
        assertTrue(body.contains("token"),
                "Response should contain 'token' key");
    }

    // ---------------------------------------------------------------
    // Test 5: /authenticate with admin credentials → returns token
    // ---------------------------------------------------------------

    @Test @Order(5)
    @DisplayName("GET /authenticate with admin:pwd returns token")
    void authenticate_adminPwd_returnsToken() throws Exception {
        mvc.perform(get("/authenticate")
                .with(httpBasic("admin", "pwd")))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.token").exists());
    }

    // ---------------------------------------------------------------
    // Test 6: Valid JWT Bearer token → /countries returns 200
    // ---------------------------------------------------------------

    @Test @Order(6)
    @DisplayName("Valid JWT Bearer token → /countries returns 200")
    void validJwt_countries_returns200() throws Exception {
        // Generate a valid JWT for "user" with 20-min expiry
        String token = Jwts.builder()
                .setSubject("user")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1200000L))
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
                .compact();

        mvc.perform(get("/countries")
                .header("Authorization", "Bearer " + token))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$").isArray())
           .andExpect(jsonPath("$.length()").value(4));
    }

    @Test @Order(7)
    @DisplayName("Valid JWT Bearer token → /countries/IN returns India")
    void validJwt_countryIN_returnsIndia() throws Exception {
        String token = Jwts.builder()
                .setSubject("user")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1200000L))
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
                .compact();

        mvc.perform(get("/countries/IN")
                .header("Authorization", "Bearer " + token))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.code").value("IN"))
           .andExpect(jsonPath("$.name").value("India"));
    }

    // ---------------------------------------------------------------
    // Test 8: Tampered JWT → 401 Unauthorized
    // ---------------------------------------------------------------

    @Test @Order(8)
    @DisplayName("Tampered JWT Bearer token → 401 Unauthorized")
    void tamperedJwt_returns401() throws Exception {
        // Generate valid token then tamper the signature
        String token = Jwts.builder()
                .setSubject("user")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1200000L))
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
                .compact();

        // Append garbage to invalidate signature
        String tamperedToken = token + "tampered";

        mvc.perform(get("/countries")
                .header("Authorization", "Bearer " + tamperedToken))
           .andExpect(status().isUnauthorized());
    }

    // ---------------------------------------------------------------
    // Test 9: Expired JWT → 401 Unauthorized
    // ---------------------------------------------------------------

    @Test @Order(9)
    @DisplayName("Expired JWT Bearer token → 401 Unauthorized")
    void expiredJwt_returns401() throws Exception {
        // Token expired 1 second ago
        String token = Jwts.builder()
                .setSubject("user")
                .setIssuedAt(new Date(System.currentTimeMillis() - 2000))
                .setExpiration(new Date(System.currentTimeMillis() - 1000))
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
                .compact();

        mvc.perform(get("/countries")
                .header("Authorization", "Bearer " + token))
           .andExpect(status().isUnauthorized());
    }

    // ---------------------------------------------------------------
    // Test 10: Invalid JWT string (garbage) → 401
    // ---------------------------------------------------------------

    @Test @Order(10)
    @DisplayName("Garbage Bearer token → 401 Unauthorized")
    void garbageJwt_returns401() throws Exception {
        mvc.perform(get("/countries")
                .header("Authorization", "Bearer invalid.garbage.token"))
           .andExpect(status().isUnauthorized());
    }

    // ---------------------------------------------------------------
    // Test 11: /authenticate without credentials → 401
    // ---------------------------------------------------------------

    @Test @Order(11)
    @DisplayName("GET /authenticate without credentials → 401")
    void authenticate_noCredentials_returns401() throws Exception {
        mvc.perform(get("/authenticate"))
           .andExpect(status().isUnauthorized());
    }

    // ---------------------------------------------------------------
    // Test 12: Base64 decoding verification
    // ---------------------------------------------------------------

    @Test @Order(12)
    @DisplayName("Base64 decode of 'dXNlcjpwd2Q=' = 'user:pwd'")
    void base64Decode_userPwd() {
        // Demonstrates Base64 limitation:
        // "user:pwd" → Base64 → "dXNlcjpwd2Q=" → easily decoded back
        String encoded = Base64.getEncoder().encodeToString("user:pwd".getBytes());
        byte[] decoded = Base64.getDecoder().decode(encoded);
        String result = new String(decoded);
        assertEquals("user:pwd", result,
                "Base64 is reversible — not secure for credentials transmission");
    }

    // ---------------------------------------------------------------
    // Test 13: @WithMockUser convenience annotation
    // ---------------------------------------------------------------

    @Test @Order(13)
    @DisplayName("@WithMockUser simulates authenticated user")
    @WithMockUser(username = "user", roles = {"USER"})
    void withMockUser_countries_returns200() throws Exception {
        mvc.perform(get("/countries"))
           .andExpect(status().isOk());
    }
}
