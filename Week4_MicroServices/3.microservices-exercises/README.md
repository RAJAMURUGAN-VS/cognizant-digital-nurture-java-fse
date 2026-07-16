# Microservices with Spring Boot 3.0 — 4 Exercises

Java 17 + Spring Boot 3.2.5 + Spring Cloud 2023.0.1. Every service builds and runs standalone
with **H2 in-memory DB** by default, so nothing needs to be installed to try it out. Commented
MySQL config is included where relevant — switch it on when you want persistence.

Each exercise is a separate, independent Maven project (no shared parent), so you can zip/run
them individually if your assignment only needs one at a time.

```
microservices-exercises/
├── ex1-user-order/              Exercise 1: User + Order services (REST, WebClient, OpenFeign, JPA)
│   ├── user-service/            port 8081
│   └── order-service/           port 8082
├── ex2-inventory-discovery/     Exercise 2: Eureka + Config Server + Product/Inventory
│   ├── eureka-server/           port 8761
│   ├── config-server/           port 8888
│   ├── product-service/         port 8083
│   └── inventory-service/       port 8084
├── ex3-api-gateway/             Exercise 3: Spring Cloud Gateway
│   ├── api-gateway/             port 8080
│   ├── customer-service/        port 8091
│   └── billing-service/         port 8092
└── ex4-payment-circuitbreaker/  Exercise 4: Resilience4j Circuit Breaker
    └── payment-service/         port 8085
```

## How to run any service

```bash
cd <service-folder>
mvn spring-boot:run
```

Requires JDK 17+ and Maven 3.8+. First build of each service will download its dependencies
from Maven Central (needs internet access once).

---

## Exercise 1 — User & Order Management

```bash
cd ex1-user-order/user-service   && mvn spring-boot:run     # port 8081
cd ex1-user-order/order-service  && mvn spring-boot:run     # port 8082 (in a second terminal)
```

- `order-service` validates the `userId` on every order by calling `user-service`.
- Two inter-service clients are included, both fully wired — pick whichever your assignment wants:
  - `UserFeignClient` (OpenFeign, declarative) — used by default in `OrderServiceImpl`.
  - `UserWebClient` (Spring WebFlux, reactive) — same call, alternative implementation.
- Data layer: Spring Data JPA, H2 by default; MySQL block is commented in `application.yml`.

Try it:
```bash
curl -X POST localhost:8081/api/users -H "Content-Type: application/json" \
  -d '{"name":"Karthik","email":"karthik@example.com","phone":"9999999999","address":"Chennai"}'

curl -X POST localhost:8082/api/orders -H "Content-Type: application/json" \
  -d '{"userId":1,"productName":"Laptop","quantity":1,"price":55000}'

curl localhost:8082/api/orders/1
```

---

## Exercise 2 — Inventory Management with Service Discovery

Start in this order (each depends on the previous being up):

```bash
cd ex2-inventory-discovery/eureka-server   && mvn spring-boot:run   # 8761 — start first
cd ex2-inventory-discovery/config-server   && mvn spring-boot:run   # 8888 — start second
cd ex2-inventory-discovery/product-service && mvn spring-boot:run   # 8083
cd ex2-inventory-discovery/inventory-service && mvn spring-boot:run # 8084
```

- **Eureka** (`eureka-server`): service registry. Dashboard at http://localhost:8761
- **Config Server** (`config-server`): serves centralized config from
  `src/main/resources/config-repo/*.yml` using the `native` profile (no Git server needed).
  `product-service` and `inventory-service` fetch their port/logging/business config from here
  via `bootstrap.yml` (`spring.cloud.config.uri`).
- `inventory-service` calls `product-service` using **OpenFeign resolved through Eureka**
  (`@FeignClient(name = "product-service")` — no hardcoded URL, unlike Exercise 1).

Try it:
```bash
curl -X POST localhost:8083/api/products -H "Content-Type: application/json" \
  -d '{"name":"Wireless Mouse","sku":"WM-001","price":799,"stockQuantity":50}'

curl -X POST localhost:8084/api/inventory/adjust -H "Content-Type: application/json" \
  -d '{"productId":1,"delta":-5}'

curl localhost:8084/api/inventory/1
```

Change `config-repo/product-service.yml`'s `low-stock-threshold`, restart config-server, then
`POST localhost:8083/actuator/refresh` on product-service to pick up the new value without a
full restart.

---

## Exercise 3 — API Gateway

```bash
cd ex3-api-gateway/customer-service && mvn spring-boot:run   # 8091
cd ex3-api-gateway/billing-service  && mvn spring-boot:run   # 8092
cd ex3-api-gateway/api-gateway      && mvn spring-boot:run   # 8080
```

All external traffic goes through the gateway on **8080**:

| Public route              | Rewritten to                | Backend           |
|----------------------------|------------------------------|--------------------|
| `/api/customers/**`        | `/customers/**`               | customer-service:8091 |
| `/api/billing/**`          | `/invoices/**`                 | billing-service:8092  |

Features implemented, all per-route in `application.yml`:
- **Path rewriting** — `RewritePath` filter strips `/api` and remaps to the backend's real path.
- **Rate limiting** — `RequestRateLimiter` filter backed by a custom in-memory token-bucket
  `RateLimiter` bean (`InMemoryRateLimiter`), so it works with **no Redis required**. Swap to
  Spring Cloud Gateway's built-in `RedisRateLimiter` in production (dependency is stubbed out,
  commented, in `pom.xml`). Limited by `X-API-KEY` header if present, else by caller IP.
- **Caching** — `LocalResponseCache` filter (Caffeine-backed, Spring Cloud Gateway 4.1+) caches
  GET responses for 30s per route.

Try it:
```bash
curl localhost:8080/api/customers        # -> forwarded to customer-service /customers
curl localhost:8080/api/billing/101      # -> forwarded to billing-service /invoices/101

# hit it fast repeatedly to see 429s once the token bucket (burst 10, refill 5/s) is drained
for i in $(seq 1 15); do curl -s -o /dev/null -w "%{http_code}\n" localhost:8080/api/customers; done
```

---

## Exercise 4 — Resilient Payment Service (Circuit Breaker)

```bash
cd ex4-payment-circuitbreaker/payment-service && mvn spring-boot:run   # 8085
```

`payment-service` calls a **simulated slow/flaky third-party gateway** (`/third-party/charge`,
same app, in-process — see `ThirdPartyGatewaySimulatorController`) which:
- sleeps 4-6s on ~40% of calls (exceeds the 2s WebClient timeout)
- throws on ~20% of calls
- succeeds quickly otherwise

`PaymentServiceImpl.processPayment()` wraps that call with Resilience4j's `@CircuitBreaker`
(instance `paymentGateway`, configured in `application.yml`):
- Opens after ≥50% failure/slow-call rate over the last 10 calls (min. 5 calls sampled)
- Stays OPEN 10s, then allows 3 trial calls (HALF_OPEN) before deciding to re-close
- On failure/timeout/OPEN-rejection, `paymentFallback()` returns a graceful
  `FALLBACK_QUEUED` response instead of a 5xx
- `CircuitBreakerEventListener` logs every state transition, failure, slow-call, and rejected
  call — the log lines double as your "monitoring" trail; `/actuator/circuitbreakerevents` and
  `/actuator/metrics` expose the same data for a dashboard (Grafana/Prometheus via
  `micrometer-registry-prometheus`, already on the classpath).

Try it (run several times in a row to see it trip):
```bash
for i in $(seq 1 15); do
  curl -s -X POST localhost:8085/api/payments -H "Content-Type: application/json" \
    -d '{"orderId":'"$i"',"amount":499.00}'
  echo
done

curl localhost:8085/actuator/health          # shows circuitbreaker health indicator
curl localhost:8085/actuator/circuitbreakerevents
```

---

## Notes

- All services default to **H2 in-memory** so everything runs immediately without setting up
  MySQL/PostgreSQL. Each `application.yml` has a commented MySQL block — uncomment it and point
  it at your local database if your assignment requires real persistence.
- Lombok is used throughout (`@Data`, `@Builder`, `@RequiredArgsConstructor`) — make sure
  annotation processing is enabled in your IDE (IntelliJ: Settings → Build → Compiler →
  Annotation Processors → Enable).
