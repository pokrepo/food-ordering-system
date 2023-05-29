package com.food.ordering.service.domain.ports.input.service;

import com.food.ordering.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.service.domain.dto.track.TrackOrderQuery;
import com.food.ordering.service.domain.dto.track.TrackOrderResponse;
import jakarta.validation.Valid;

public interface OrderApplicationService {

    CreateOrderResponse createOrder(@Valid CreateOrderCommand createOrderCommand);

    TrackOrderResponse trackOrder(@Valid TrackOrderQuery trackOrderQuery);
}