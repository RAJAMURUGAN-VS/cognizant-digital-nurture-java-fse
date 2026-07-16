# Sample Microservices Load Balancing Exercises

Three standalone Spring Boot 3 / Spring Cloud gateway projects, one per exercise
from the hands-on sheet. Each folder is a complete, independently buildable
Maven project (own `pom.xml`, own port, own tests).

| Folder | Exercise | Port |
|---|---|---|
| `exercise1-edge-service-routing-filtering` | Routing + custom logging GlobalFilter | 8080 |
| `exercise2-load-balancing-gateway` | `lb://` routing + custom RandomLoadBalancer | 8081 |
| `exercise3-resilience-patterns-gateway` | CircuitBreaker gateway filter + fallback | 8082 |

## Prerequisites

- JDK 17+
- Maven 3.8+ (or use the Maven wrapper if you add one)
- Internet access to Maven Central for the first build (to download
  Spring Boot / Spring Cloud / Resilience4j dependencies)

## Running an exercise

```bash
cd exercise1-edge-service-routing-filtering
mvn spring-boot:run
```

Then, in another terminal:

```bash
curl -v http://localhost:8080/example/anything
```

You should see the LoggingFilter print the incoming request and response
status in the application logs.

### Exercise 2 — load balancing

```bash
cd exercise2-load-balancing-gateway
mvn spring-boot:run
```

```bash
curl -v http://localhost:8081/loadbalanced/anything
```

Requests are load-balanced (via the custom `RandomLoadBalancer` bean) across
the two static instances configured for `example-service` in
`application.properties` (`localhost:9001` and `localhost:9002`). Stand up two
throwaway HTTP servers on those ports to see requests distributed between
them, e.g.:

```bash
python3 -m http.server 9001
python3 -m http.server 9002
```

### Exercise 3 — resilience patterns

```bash
cd exercise3-resilience-patterns-gateway
mvn spring-boot:run
```

```bash
curl -v http://localhost:8082/resilient/anything
```

If the downstream call fails or times out enough times (per the
`exampleCircuitBreaker` settings — 10-call sliding window, 50% failure
threshold), the circuit opens and requests are routed to `/fallback`
instead. Check breaker state live at:

```bash
curl http://localhost:8082/actuator/circuitbreakers
```

## Notes on deviations from the original exercise sheet

- **Exercise 3** originally listed `resilience4j-spring-boot2`. This bundle
  targets Spring Boot 3, so `resilience4j-spring-boot3` plus
  `spring-cloud-starter-circuitbreaker-reactor-resilience4j` are used instead
  (the Boot-2 artifact is not compatible with Boot 3 / Jakarta EE).
- **Exercise 2** adds a `spring.cloud.discovery.client.simple.instances.*`
  block so the `lb://example-service` route has concrete instances to
  balance across without requiring a Eureka/Consul server — useful for
  running the exercise standalone.
- Each project includes a small JUnit test class so you can `mvn test` to
  confirm the routes/beans wire up correctly, beyond just eyeballing the
  config.

## Packaging

Each module can be independently packaged into a runnable jar:

```bash
mvn clean package
java -jar target/*.jar
```
