package org.formation.service;

import java.time.Instant;

import org.formation.domain.Order;
import org.formation.domain.OrderRepository;
import org.formation.domain.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderService {

	@Autowired
	OrderRepository orderRepository;
	
	
	public Order createOrder(Order order) {	
		order.setDate(Instant.now());
		order.setStatus(OrderStatus.PENDING);
		return orderRepository.save(order);
	}
}
