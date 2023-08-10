package org.formation.service;

import java.time.Instant;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.formation.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderService {

	@Autowired
	OrderRepository orderRepository;


	@Autowired
	ObjectMapper mapper;
	
	public Order createOrder(Order order)  {
		order.setDate(Instant.now());
		order.setStatus(OrderStatus.PENDING);
		order = orderRepository.save(order);

		return order;
	}
}
