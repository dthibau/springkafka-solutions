package org.formation.service;

import java.time.Instant;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.formation.domain.*;
import org.formation.event.OrderEvent;
import org.formation.event.OrderEventRepository;
import org.formation.event.OrderEventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderService {

	@Autowired
	OrderRepository orderRepository;
	@Autowired
	OrderEventRepository eventRepository;

	@Autowired
	ObjectMapper mapper;
	
	@SneakyThrows(JsonProcessingException.class)
	public Order createOrder(Order order)  {
		order.setDate(Instant.now());
		order.setStatus(OrderStatus.PENDING);
		order = orderRepository.save(order);
		OrderEvent event = OrderEvent.builder().type(OrderEventType.ORDER_CREATED).orderId(order.getId()).payload(mapper.writeValueAsString(order)).build();
		eventRepository.save(event);
		return order;
	}
}
