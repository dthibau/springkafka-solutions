package org.formation.service;

import java.time.Instant;

import org.formation.domain.Order;
import org.formation.domain.OrderRepository;
import org.formation.domain.OrderStatus;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderService {
	private final OrderRepository orderRepository;
	private final RestTemplate restTemplate;

	public OrderService(OrderRepository orderRepository, RestTemplateBuilder restTemplateBuilder) {
		this.orderRepository = orderRepository;
		this.restTemplate = restTemplateBuilder.rootUri("http://localhost:8082").build();
	}
	
	public Order createOrder(Order order) {	
		order.setDate(Instant.now());
		order.setStatus(OrderStatus.PENDING);

		order = orderRepository.save(order);
		
		restTemplate.postForEntity("/api/tickets", new TicketDto(order), Object.class);

		return order;
	}
}
