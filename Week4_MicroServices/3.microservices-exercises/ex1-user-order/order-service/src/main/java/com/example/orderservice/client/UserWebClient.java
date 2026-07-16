package com.example.orderservice.client;

import com.example.orderservice.dto.UserDto;
import com.example.orderservice.exception.UserServiceUnavailableException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * Alternative to UserFeignClient — same job, using reactive WebClient
 * (Spring WebFlux) with a blocking call at the edge, as requested by the
 * exercise ("Communicate ... using WebClient or OpenFeign").
 */
@Component
@RequiredArgsConstructor
public class UserWebClient {

    private final WebClient userServiceWebClient;

    public UserDto getUserById(Long id) {
        try {
            return userServiceWebClient.get()
                    .uri("/api/users/{id}", id)
                    .retrieve()
                    .bodyToMono(UserDto.class)
                    .block();
        } catch (WebClientResponseException.NotFound e) {
            return null;
        } catch (Exception e) {
            throw new UserServiceUnavailableException("User service is unavailable: " + e.getMessage());
        }
    }
}
