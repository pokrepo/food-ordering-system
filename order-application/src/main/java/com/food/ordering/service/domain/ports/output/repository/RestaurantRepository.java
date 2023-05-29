package com.food.ordering.service.domain.ports.output.repository;

import com.food.ordering.system.order.service.domain.entity.Restaurant;

import java.util.Optional;
import java.util.UUID;

public interface RestaurantRepository {

    Optional<Restaurant> findRestaurantinformation(UUID restaurantId);
}
