package org.formation.service;

import java.time.Instant;

import org.formation.domain.Order;
import org.formation.domain.OrderRepository;
import org.formation.domain.OrderStatus;
import org.formation.domain.PaymentInformation;
import org.formation.event.OrderEvent;
import org.formation.event.OrderEventRepository;
import org.formation.event.OrderEventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.java.Log;

@Service
@Transactional
@Log
public class OrderService {

	@Autowired
	OrderRepository orderRepository;
	@Autowired
	OrderEventRepository eventRepository;
	@Autowired
	ObjectMapper mapper;

	@Resource
	ReplyingKafkaTemplate<Long, PaymentInformation, String> replyingKafkaTemplate;
	
	@SneakyThrows(JsonProcessingException.class)
	@Transactional("transactionManager")
	public Order createOrder(Order order)  {
		order.setDate(Instant.now());
		order.setStatus(OrderStatus.PENDING);
		order.getPaymentInformation().setAmount(order.getTotal());
		final Order ret = orderRepository.save(order);
		OrderEvent event = OrderEvent.builder().type(OrderEventType.ORDER_CREATED).orderId(order.getId()).payload(mapper.writeValueAsString(order)).build();
		eventRepository.save(event);

		return order;
	}
}
