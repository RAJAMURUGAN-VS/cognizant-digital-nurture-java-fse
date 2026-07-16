package com.example.orderservice.service;

import com.example.orderservice.client.UserFeignClient;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.dto.UserDto;
import com.example.orderservice.entity.Order;
import com.example.orderservice.exception.OrderNotFoundException;
import com.example.orderservice.exception.UserServiceUnavailableException;
import com.example.orderservice.repository.OrderRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    // Feign is used as the primary inter-service call; UserWebClient (autowired
    // elsewhere) demonstrates the WebClient alternative for the same endpoint.
    private final UserFeignClient userFeignClient;

    @Override
    public OrderResponse createOrder(OrderRequest request) {
        // Validate the user exists in User Service before creating the order
        UserDto user = fetchUser(request.getUserId());

        Order order = Order.builder()
                .userId(request.getUserId())
                .productName(request.getProductName())
                .quantity(request.getQuantity())
                .price(request.getPrice())
                .build();

        Order saved = orderRepository.save(order);
        return toResponse(saved, user);
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        UserDto user = fetchUser(order.getUserId());
        return toResponse(order, user);
    }

    @Override
    public List<OrderResponse> getOrdersByUser(Long userId) {
        UserDto user = fetchUser(userId);
        return orderRepository.findByUserId(userId).stream()
                .map(order -> toResponse(order, user))
                .toList();
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(order -> toResponse(order, fetchUserSafely(order.getUserId())))
                .toList();
    }

    private UserDto fetchUser(Long userId) {
        try {
            UserDto user = userFeignClient.getUserById(userId);
            if (user == null) {
                throw new UserServiceUnavailableException("User " + userId + " not found in User Service");
            }
            return user;
        } catch (FeignException.NotFound e) {
            throw new UserServiceUnavailableException("User " + userId + " not found in User Service");
        } catch (FeignException e) {
            throw new UserServiceUnavailableException("User service is unavailable: " + e.getMessage());
        }
    }

    // Used for list endpoints where a single unreachable user shouldn't fail the whole list
    private UserDto fetchUserSafely(Long userId) {
        try {
            return userFeignClient.getUserById(userId);
        } catch (Exception e) {
            return null;
        }
    }

    private OrderResponse toResponse(Order order, UserDto user) {
        return OrderResponse.builder()
                .id(order.getId())
                .productName(order.getProductName())
                .quantity(order.getQuantity())
                .price(order.getPrice())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .user(user)
                .build();
    }
}
