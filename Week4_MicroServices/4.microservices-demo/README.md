# Bank Microservices Demo — Eureka + API Gateway

This project implements the hands-on exercise: two bank microservices
(`account`, `loan`), a Eureka Discovery Server, a `greet-service`, and a
Spring Cloud API Gateway with a global filter that logs every incoming
request.

## Projects included

| Project                  | Port | Purpose                                              |
|---------------------------|------|-------------------------------------------------------|
| `eureka-discovery-server` | 8761 | Service registry (Eureka)                             |
| `account-service`         | 8080 | `GET /accounts/{number}` — dummy account details       |
| `loan-service`            | 8081 | `GET /loans/{number}` — dummy loan details             |
| `greet-service`           | 8082 | `GET /greet` — returns "Hello World"                   |
| `api-gateway`             | 9090 | Spring Cloud Gateway, routes by service name, logs all requests via `LogFilter` |

Each project is a **standalone Maven project** (own `pom.xml`), exactly as
in the original exercise — you can import each one into Eclipse/IntelliJ
individually, or build them from the command line.

- Java: 17
- Spring Boot: 3.2.5
- Spring Cloud: 2023.0.1 (release train "Leyton")

## Build order

Each service builds independently:

```bash
cd eureka-discovery-server && mvn clean package
cd ../account-service       && mvn clean package
cd ../loan-service          && mvn clean package
cd ../greet-service         && mvn clean package
cd ../api-gateway           && mvn clean package
```

## Run order (important — start Eureka first)

Open 5 terminals (or 5 Eclipse launch configs), one per service, and start
them in this order, waiting for each to fully start before launching the
next:

1. **Eureka Discovery Server**
   ```bash
   cd eureka-discovery-server
   mvn spring-boot:run
   ```
   Verify at http://localhost:8761 — the "Instances currently registered
   with Eureka" list should be empty.

2. **Account Service**
   ```bash
   cd account-service
   mvn spring-boot:run
   ```
   Test directly: http://localhost:8080/accounts/00987987973432

3. **Loan Service**
   ```bash
   cd loan-service
   mvn spring-boot:run
   ```
   Test directly: http://localhost:8081/loans/H00987987972342

4. **Greet Service**
   ```bash
   cd greet-service
   mvn spring-boot:run
   ```
   Test directly: http://localhost:8082/greet → `Hello World`

5. **API Gateway**
   ```bash
   cd api-gateway
   mvn spring-boot:run
   ```

After all five are up, refresh http://localhost:8761 — you should see
`ACCOUNT-SERVICE`, `LOAN-SERVICE`, `GREET-SERVICE`, and `API-GATEWAY` all
registered.

## Test the API Gateway routing + logging filter

With everything running, call the greet service **through** the gateway:

```
http://localhost:9090/greet-service/greet
```

Expected response body: `Hello World`

You can also route to the other two services the same way:

```
http://localhost:9090/account-service/accounts/00987987973432
http://localhost:9090/loan-service/loans/H00987987972342
```

In the **api-gateway console**, you'll see a log line for every request,
produced by `LogFilter` (`com.cognizant.apigateway.filter.LogFilter`),
e.g.:

```
INFO ... c.c.apigateway.filter.LogFilter : Incoming request -> Method: GET, Path: /greet-service/greet, Remote Address: /127.0.0.1:xxxxx
```

## Sample dummy responses

**Account** — `GET /accounts/{number}`
```json
{ "number": "00987987973432", "type": "savings", "balance": 234343 }
```

**Loan** — `GET /loans/{number}`
```json
{ "number": "H00987987972342", "type": "car", "loan": 400000, "emi": 3258, "tenure": 18 }
```

## Notes

- All three business services (`account`, `loan`, `greet`) register
  themselves with Eureka using `spring.application.name`, which is also the
  service ID used by the gateway's discovery locator
  (`spring.cloud.gateway.discovery.locator.enabled=true`), so routes to
  `/{service-name}/**` are created automatically — no manual route
  configuration is needed in `application.properties`.
- `eureka.instance.prefer-ip-address=true` is set on each client so Eureka
  advertises the IP address rather than the hostname, which avoids DNS
  resolution issues on most local setups.
- If you see "port already in use" when starting a second service, make
  sure each project's `application.properties` still has its own distinct
  `server.port` (as listed in the table above).
