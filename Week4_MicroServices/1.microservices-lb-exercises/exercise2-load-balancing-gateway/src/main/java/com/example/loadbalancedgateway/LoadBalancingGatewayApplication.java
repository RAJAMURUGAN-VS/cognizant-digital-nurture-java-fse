package com.example.loadbalancedgateway;

import com.example.loadbalancedgateway.config.LoadBalancerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;

/**
 * Exercise 2: Load Balancing in an API Gateway
 *
 * Routes requests matching /loadbalanced/** to the logical service
 * "example-service" using the "lb://" scheme, which is resolved by
 * Spring Cloud LoadBalancer across the instances configured in
 * application.properties (via SimpleDiscoveryClient) using a custom
 * random load-balancing strategy defined in LoadBalancerConfiguration.
 */
@SpringBootApplication
@LoadBalancerClient(name = "example-service", configuration = LoadBalancerConfiguration.class)
public class LoadBalancingGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoadBalancingGatewayApplication.class, args);
    }
}
