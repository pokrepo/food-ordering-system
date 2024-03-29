package com.food.ordering.service.domain.ports.output.repository.message.publisher.restaurantapproval;

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;

public interface OrderPayedRestaurantRequestMessagePublisher extends DomainEventPublisher<OrderPaidEvent> {
}
