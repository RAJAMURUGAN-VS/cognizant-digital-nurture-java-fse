package com.cognizant.springlearn.controller;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * AuthenticationController — REST controller for JWT token generation.
 *
 * JWT Process Flow:
 *   Step 1: Client sends credentials to /authenticate via HTTP Basic Auth.
 *           Authorization: Basic Base64("user:pwd") = "Basic dXNlcjpwd2Q="
 *   Step 2: Server validates credentials (Spring Security handles this).
 *           Controller reads Authorization header, decodes username,
 *           generates JWT, returns {"token": "eyJ..."}
 *   Step 3: Client uses JWT for all subsequent requests:
 *           Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
 *   Step 4: JwtAuthorizationFilter validates the Bearer token on each request.
 *
 * Why is Basic Auth limited?
 *   Every request sends "user:pwd" Base64-encoded — easy to decode.
 *   JWT solves this: credentials sent ONCE, token used thereafter.
 *   Token has expiry (20 min) — limits damage if intercepted.
 *   Token is signed — cannot be forged without "secretkey".
 *
 * curl commands:
 *   # Get token (Basic Auth):
 *   curl -s -u user:pwd http://localhost:8083/authenticate
 *
 *   # Use token (JWT Bearer):
 *   curl -s -H "Authorization: Bearer <token>" http://localhost:8083/countries
 */
@RestController
public class AuthenticationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

    private static final String JWT_SECRET  = "secretkey";
    private static final long   JWT_EXPIRY  = 1200000L; // 20 minutes in ms

    // ---------------------------------------------------------------
    // GET /authenticate — decode Basic Auth, generate and return JWT
    // ---------------------------------------------------------------

    /**
     * Authenticates the user and returns a JWT token.
     *
     * @RequestHeader("Authorization") — Spring reads the Authorization header
     * from the HTTP request and injects its value as the authHeader parameter.
     *
     * The header value looks like:
     *   "Basic dXNlcjpwd2Q="
     *   (Base64 encoding of "user:pwd")
     *
     * Steps:
     *   1. Log the Authorization header (shows Basic + Base64 value).
     *   2. Decode the Base64 to extract the username.
     *   3. Generate a JWT signed with HMACSHA256 and "secretkey".
     *   4. Return {"token": "eyJ..."}.
     *
     * Security config allows USER and ADMIN roles to call this endpoint.
     *
     * @return Map<String, String> → serialised as {"token":"eyJ..."}
     */
    @GetMapping("/authenticate")
    public Map<String, String> authenticate(
            @RequestHeader("Authorization") String authHeader) {

        LOGGER.info("START authenticate");
        LOGGER.debug("Authorization header received: {}", authHeader);

        // Step 1: Decode Basic Auth header to get username
        String user = getUser(authHeader);
        LOGGER.debug("Decoded username: {}", user);

        // Step 2: Generate JWT for the authenticated user
        String token = generateJwt(user);
        LOGGER.debug("Generated JWT token: {}", token);

        // Step 3: Return token in response map
        Map<String, String> map = new HashMap<>();
        map.put("token", token);

        LOGGER.info("END authenticate — token generated for user: {}", user);
        return map;
    }

    // ---------------------------------------------------------------
    // Private: decode Basic Auth header to get username
    // ---------------------------------------------------------------

    /**
     * Decodes the Basic Authentication header to extract the username.
     *
     * HTTP Basic Auth format:
     *   Authorization: Basic Base64("username:password")
     *
     * Steps:
     *   1. Remove "Basic " prefix to get Base64-encoded credentials.
     *   2. Decode Base64 → byte array → String ("user:pwd").
     *   3. Extract text before ":" → username.
     *
     * Example:
     *   authHeader = "Basic dXNlcjpwd2Q="
     *   encodedCredentials = "dXNlcjpwd2Q="
     *   decoded = "user:pwd"
     *   username = "user"
     *
     * Base64 demonstration:
     *   "user:pwd" → Base64 encode → "dXNlcjpwd2Q="
     *   "admin:pwd" → Base64 encode → "YWRtaW46cHdk"
     *   Decode at: https://www.base64decode.net/
     *
     * @param authHeader value of Authorization header ("Basic ...")
     * @return decoded username
     */
    private String getUser(String authHeader) {
        LOGGER.info("START getUser");
        LOGGER.debug("authHeader: {}", authHeader);

        // Remove "Basic " prefix (7 characters)
        String encodedCredentials = authHeader.substring("Basic ".length());
        LOGGER.debug("Base64 encoded credentials: {}", encodedCredentials);

        // Decode Base64 → byte[] → String
        byte[] decodedBytes = Base64.getDecoder().decode(encodedCredentials);
        String decodedCredentials = new String(decodedBytes);
        LOGGER.debug("Decoded credentials: {}", decodedCredentials);

        // Extract username (text before ":")
        String username = decodedCredentials.substring(0, decodedCredentials.indexOf(":"));
        LOGGER.debug("Username extracted: {}", username);

        LOGGER.info("END getUser — user: {}", username);
        return username;
    }

    // ---------------------------------------------------------------
    // Private: generate signed JWT token
    // ---------------------------------------------------------------

    /**
     * Generates a JWT token for the given username.
     *
     * JWT Structure (3 parts separated by "."):
     *   Header:    {"alg":"HS256"} → Base64Url encoded
     *   Payload:   {"sub":"user","iat":...,"exp":...} → Base64Url encoded
     *   Signature: HMACSHA256(header + "." + payload, "secretkey")
     *
     * Explanation of claims:
     *   setSubject(user)  — "sub" claim: who the token is about (username).
     *   setIssuedAt(now)  — "iat" claim: when token was issued.
     *   setExpiration(+20min) — "exp" claim: when token expires.
     *   signWith(HS256, "secretkey") — signs using HMAC-SHA256.
     *     Any tampering changes the signature → JwtAuthorizationFilter rejects it.
     *
     * Token expiry: 20 minutes (1,200,000 milliseconds).
     * After expiry, client must re-authenticate at /authenticate.
     *
     * Verify at: https://jwt.io/ — paste token to see decoded header/payload.
     *
     * @param user the authenticated username to embed in the token
     * @return compact JWT string (e.g. "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyI...")
     */
    private String generateJwt(String user) {
        LOGGER.info("START generateJwt for user: {}", user);

        JwtBuilder builder = Jwts.builder();

        // Set the subject (username) in the token payload
        builder.setSubject(user);

        // Set token issue time as current time
        builder.setIssuedAt(new Date());

        // Set token expiry as 20 minutes from now
        builder.setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRY));

        // Sign the token with HMACSHA256 and the secret key
        // The secret key must be kept confidential on the server
        builder.signWith(SignatureAlgorithm.HS256, JWT_SECRET);

        // Compact builds the final JWT string: header.payload.signature
        String token = builder.compact();

        LOGGER.debug("JWT token generated: {}", token);
        LOGGER.info("END generateJwt — token expires in {} ms", JWT_EXPIRY);

        return token;
    }
}
