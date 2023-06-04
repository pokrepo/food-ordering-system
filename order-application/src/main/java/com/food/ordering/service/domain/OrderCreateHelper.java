package com.food.ordering.service.domain;

import com.food.ordering.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.service.domain.mapper.OrderDataMapper;
import com.food.ordering.service.domain.ports.output.repository.CustomerRepository;
import com.food.ordering.service.domain.ports.output.repository.OrderRepository;
import com.food.ordering.service.domain.ports.output.repository.RestaurantRepository;
import com.food.ordering.system.order.service.domain.OrderDomainService;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@Slf4j
public class OrderCreateHelper {

    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderDataMapper orderDataMapper;

    public OrderCreateHelper(OrderDomainService orderDomainService, OrderRepository orderRepository, CustomerRepository customerRepository, RestaurantRepository restaurantRepository, OrderDataMapper orderDataMapper) {
        this.orderDomainService = orderDomainService;
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.restaurantRepository = restaurantRepository;
        this.orderDataMapper = orderDataMapper;
    }

    @Transactional
    public OrderCreatedEvent persistOrder(CreateOrderCommand createOrderCommand) {
        checkCustomer(createOrderCommand.getCustomerId());
        Restaurant restaurant = checkRestaurant(createOrderCommand.getRestaurantId());
        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        OrderCreatedEvent orderCreatedEvent = orderDomainService.validateAndInitiateOrder(order, restaurant);
        saveOrder(order);
        log.info("Order created with id: {}", order.getId().getValue());
        return orderCreatedEvent;
    }

    private Restaurant checkRestaurant(UUID restaurantId) {
        return restaurantRepository.findRestaurantinformation(restaurantId).orElseThrow(() -> {
            log.warn("Could not find restaurant with restaurant id: {}", restaurantId);
            return new OrderDomainException("Could not find restaurant with restaurant id: " + restaurantId);
        });
    }

    private void checkCustomer(UUID customerId) {
        customerRepository.findCustomer(customerId).orElseThrow(() -> {
            log.warn("Could not find customer with customer id: {}", customerId);
            return new OrderDomainException("Could not find customer with customer id: " + customerId);
        });
    }

    private Order saveOrder(Order order) {
        Order orderResult = orderRepository.save(order);
        if (orderResult == null) {
            log.error("Could not save order");
            throw new OrderDomainException("Could not save order");
        }
        log.info("Order is save with id: {}", orderResult.getId().getValue());
        return orderResult;
    }
}
