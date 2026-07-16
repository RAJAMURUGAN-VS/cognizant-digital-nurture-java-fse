package com.example.loadbalancedgateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class LoadBalancingGatewayApplicationTests {

    @Autowired
    private RouteLocator routeLocator;

    @Autowired
    private ServiceInstanceListSupplier serviceInstanceListSupplier;

    @Test
    void contextLoads() {
        assertThat(routeLocator).isNotNull();
    }

    @Test
    void loadBalancedRouteIsRegistered() {
        StepVerifier.create(routeLocator.getRoutes())
                .expectNextMatches(route -> route.getId().equals("load_balanced_route"))
                .verifyComplete();
    }

    @Test
    void exampleServiceHasTwoInstancesConfigured() {
        StepVerifier.create(serviceInstanceListSupplier.get())
                .expectNextMatches(instances -> instances.size() == 2)
                .verifyComplete();
    }
}
