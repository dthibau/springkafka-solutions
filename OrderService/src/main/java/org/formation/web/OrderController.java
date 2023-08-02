package org.formation.web;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.formation.domain.Order;
import org.formation.domain.OrderRepository;
import org.formation.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

	@Autowired
	OrderService orderService;
	
	@Autowired
	OrderRepository orderRepository;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Order createOrder(@Valid @RequestBody Order order)  {
		
		return orderService.createOrder(order);
	}
	
	@GetMapping
	public List<Order> findAll() {
		return orderRepository.findAll();
	}
}
