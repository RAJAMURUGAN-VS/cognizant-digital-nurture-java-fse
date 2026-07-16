package com.example.orderservice.client;

import com.example.orderservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * OpenFeign declarative client for User Service.
 * "name" + "url" is used here (no Eureka) so this project runs standalone.
 * In exercise 2 (with Eureka) you would drop the "url" attribute and Feign
 * would resolve "user-service" from the service registry instead.
 */
@FeignClient(name = "user-service", url = "${user-service.url}")
public interface UserFeignClient {

    @GetMapping("/api/users/{id}")
    UserDto getUserById(@PathVariable("id") Long id);
}
