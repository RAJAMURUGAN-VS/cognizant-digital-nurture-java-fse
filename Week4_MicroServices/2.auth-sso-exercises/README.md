# Sample Centralized Authentication and SSO Exercises

Complete, standalone Spring Boot 3 / Spring Security 6 projects for each exercise
from the hands-on sheet, modernized to actually compile and run on Boot 3
(the sheet's original code targets Boot 2 / Spring Security 5 APIs that were
removed).

| Folder | Exercise | Port |
|---|---|---|
| `exercise1-centralized-auth-oauth2-oidc` | OAuth2/OIDC login (`oauth2Login()`) | 8090 |
| `exercise2-authorization-and-resource-server/authorization-server` | Issues JWTs (client_credentials + authorization_code) | 9000 |
| `exercise2-authorization-and-resource-server/resource-server` | Validates JWTs, protects `/secure` | 8091 |
| `exercise3-jwt-secure-communication` | Hand-rolled JWT login + filter | 8092 |

## Prerequisites

- JDK 17+
- Maven 3.8+
- Internet access to Maven Central for the first build

## What changed vs. the original exercise sheet, and why

The sheet's code is written against Spring Boot 2 / Spring Security 5 and an
ancient jjwt release. None of it compiles as-is on Boot 3. Concretely:

1. **`WebSecurityConfigurerAdapter` is gone.** Spring Security 5.7 deprecated
   it and Spring Security 6 (Boot 3) removed it entirely. Every exercise's
   `SecurityConfig`/`ResourceServerConfig` is rewritten as a `SecurityFilterChain`
   `@Bean` using the lambda DSL instead of `http.authorizeRequests()...and()...`.
2. **`javax.servlet.*` → `jakarta.servlet.*`.** Exercise 3's `JwtTokenFilter`
   extends `OncePerRequestFilter`, whose package moved under Jakarta EE 9+ /
   Servlet 6, which Boot 3 requires.
3. **jjwt 0.9.1 → 0.12.5.** The sheet's version is over 6 years old, split
   into a single unmaintained jar, and its `signWith(SignatureAlgorithm, String)`
   overload is deprecated (and unsafe with short secrets). Exercise 3 uses the
   current `jjwt-api`/`jjwt-impl`/`jjwt-jackson` artifacts and builds a proper
   `SecretKey` via `Keys.hmacShaKeyFor(...)`.
4. **Exercise 2 only had Resource Server code despite the title mentioning
   "Authorization Servers" too.** Added a minimal, real Authorization Server
   (`spring-security-oauth2-authorization-server`) as a sibling project so the
   Resource Server actually has a token issuer to validate against locally,
   instead of a dangling placeholder `issuer-uri`.
5. Added a small JUnit test class to every module so `mvn test` actually
   verifies routes/filters/beans wire up, not just that the config *looks*
   right.

## Running Exercise 1 (OAuth2/OIDC login)

Before running, register a real OAuth client (e.g. in Google Cloud Console →
APIs & Services → Credentials) and put its client-id/secret into
`src/main/resources/application.yml`, replacing `YOUR_CLIENT_ID` /
`YOUR_CLIENT_SECRET`. The redirect URI to register there is:
```
http://localhost:8090/login/oauth2/code/my-client
```

```bash
cd exercise1-centralized-auth-oauth2-oidc
mvn spring-boot:run
```

Visit `http://localhost:8090/user` in a browser — you'll be redirected to
Google's login, then back to see your principal. `GET /user/claims` shows the
decoded ID token claims (name, email, etc.).

## Running Exercise 2 (Authorization Server + Resource Server)

Start both, in two terminals:

```bash
cd exercise2-authorization-and-resource-server/authorization-server
mvn spring-boot:run       # port 9000
```
```bash
cd exercise2-authorization-and-resource-server/resource-server
mvn spring-boot:run       # port 8091
```

Get a token via `client_credentials` (machine-to-machine, no browser needed):

```bash
curl -X POST http://localhost:9000/oauth2/token \
  -u resource-server-client:resource-server-secret \
  -d "grant_type=client_credentials&scope=secure.read"
```

Copy the `access_token` from the response, then call the protected endpoint:

```bash
curl http://localhost:8091/secure \
  -H "Authorization: Bearer <access_token>"
# -> This is a secure endpoint
```

There's also an `authorization_code` client (`my-client` / `my-client-secret`,
demo login `user`/`password`) registered on the Authorization Server for
exercising the interactive browser login flow, mirroring Exercise 1's flow but
against your own local Authorization Server instead of Google.

## Running Exercise 3 (hand-rolled JWT auth)

```bash
cd exercise3-jwt-secure-communication
mvn spring-boot:run
```

Log in (any non-blank username/password — swap in a real credential check
before using this beyond local experimentation) to get a token:

```bash
curl -X POST http://localhost:8092/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"alice","password":"password123"}'
```

Then call the protected endpoint with the returned token:

```bash
curl http://localhost:8092/secure \
  -H "Authorization: Bearer <token>"
# -> Hello, alice! This is a JWT-secured endpoint.
```

## Testing & packaging

Each module is independently buildable:

```bash
mvn clean test      # run the included JUnit tests
mvn clean package   # produce a runnable jar in target/
java -jar target/*.jar
```
