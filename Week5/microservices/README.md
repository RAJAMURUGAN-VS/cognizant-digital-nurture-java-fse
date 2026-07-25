# Microservices Hands-On (Spring Boot + Spring Cloud)

This package contains 5 independent Maven/Spring Boot projects that implement the
"Enterprise Application / Microservices" hands-on exercise:

```
microservices/
├── account/                  Account microservice   (port 8080)
├── loan/                     Loan microservice       (port 8081)
├── eureka-discovery-server/  Eureka registry          (port 8761)
├── greet-service/            "Hello World" service    (port 8082)
└── api-gateway/              Spring Cloud Gateway     (port 9090)
```

Stack: Spring Boot 3.2.5, Spring Cloud 2023.0.1 (Leyton), Java 17.

## 1. Account & Loan microservices

- `account`: `GET /accounts/{number}` → `{ "number": "...", "type": "savings", "balance": 234343 }`
- `loan`: `GET /loans/{number}` → `{ "number": "...", "type": "car", "loan": 400000, "emi": 3258, "tenure": 18 }`

Both are plain `spring-boot-starter-web` services with no backend/database — the
data returned is a dummy/hard-coded response, exactly as in the exercise.

Build each one individually:

```bash
cd microservices/account
mvn clean package
mvn spring-boot:run          # or: java -jar target/account-0.0.1-SNAPSHOT.jar
```

```bash
cd microservices/loan
mvn clean package
mvn spring-boot:run
```

Test in a browser or curl:

```
http://localhost:8080/accounts/00987987973432
http://localhost:8081/loans/H00987987972342
```

> Note: `loan` is preconfigured with `server.port=8081` so it can run alongside
> `account` (port 8080) without a "bind address already in use" error — this is
> the exact fix called out in the exercise.

## 2. Eureka Discovery Server

```bash
cd microservices/eureka-discovery-server
mvn clean package
mvn spring-boot:run
```

Open `http://localhost:8761` — you'll see the Eureka dashboard with an empty
"Instances currently registered with Eureka" list until services join.

`account` and `loan` are already wired up with:
- `spring-cloud-starter-netflix-eureka-client`
- `@EnableDiscoveryClient`
- `spring.application.name` (`account-service` / `loan-service`)
- `eureka.client.service-url.defaultZone=http://localhost:8761/eureka/`

### Recommended startup order
1. Start `eureka-discovery-server` first and wait until it's fully up.
2. Start `account` — refresh `http://localhost:8761`, you should see `ACCOUNT-SERVICE` listed.
3. Start `loan` — refresh again, you should see `LOAN-SERVICE` listed too.

## 3. Greet service + API Gateway (with global logging filter)

```bash
cd microservices/greet-service
mvn clean package
mvn spring-boot:run
```

Test directly: `http://localhost:8082/greet` → `Hello World`

Then start the gateway:

```bash
cd microservices/api-gateway
mvn clean package
mvn spring-boot:run
```

The gateway auto-discovers registered services via Eureka
(`spring.cloud.gateway.discovery.locator.enabled=true`, lower-cased service ids),
so `greet-service` is reachable through it at:

```
http://localhost:9090/greet-service/greet
```

`LogFilter` is a `GlobalFilter` registered as a `@Component`, so every request
that passes through the gateway is logged, e.g.:

```
Incoming request path -> /greet-service/greet
```

### Full startup order for this part
1. `eureka-discovery-server` (8761)
2. `greet-service` (8082) — confirm it shows up on the Eureka dashboard
3. `api-gateway` (9090) — confirm it registers too
4. Hit `http://localhost:9090/greet-service/greet` and check the api-gateway
   console for the `LogFilter` output.

## Importing into an IDE (Eclipse / IntelliJ / STS)

Each folder under `microservices/` is a standalone Maven project — import them
as **existing Maven projects** one at a time (`File > Import > Maven > Existing
Maven Projects`), pointing to each project's folder.

## Notes / deviations from the manual click-through in the exercise

- The exercise generates each project via https://start.spring.io in a browser;
  since this environment has no network access, the equivalent generated
  `pom.xml`, main application class, and `application.properties` were written
  by hand — the dependencies and configuration match what Spring Initializr
  would have produced (Spring Boot DevTools, Spring Web, Eureka Discovery
  Client/Server, Spring Cloud Gateway).
- Ports were assigned explicitly (8080/8081/8082/8761/9090) so all five
  services can run at the same time on one machine without clashing, matching
  the "server.port already in use" troubleshooting step in the exercise.
