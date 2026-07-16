package com.example.resiliencegateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.http.ResponseEntity;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ResilienceGatewayApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private RouteLocator routeLocator;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
        assertThat(routeLocator).isNotNull();
    }

    @Test
    void resilientRouteIsRegistered() {
        StepVerifier.create(routeLocator.getRoutes())
                .expectNextMatches(route -> route.getId().equals("resilient_route"))
                .verifyComplete();
    }

    @Test
    void fallbackEndpointRespondsWhenCircuitOpens() {
        ResponseEntity<String> response =
                restTemplate.getForEntity("http://localhost:" + port + "/fallback", String.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).contains("unavailable");
    }
}
