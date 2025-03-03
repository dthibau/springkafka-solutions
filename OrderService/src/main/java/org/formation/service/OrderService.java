package org.formation.service;

import java.time.Duration;
import java.time.Instant;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.java.Log;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.formation.domain.*;
import org.formation.event.OrderEvent;
import org.formation.event.OrderEventRepository;
import org.formation.event.OrderEventType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional("transactionManager")
@Log
public class OrderService {


	@Resource
	ReplyingKafkaTemplate<Long, PaymentInformation, String> replyingKafkaTemplate;

	private final OrderRepository orderRepository;
	private final OrderEventRepository eventRepository;
    private final ObjectMapper mapper;

	public OrderService(OrderRepository orderRepository, OrderEventRepository eventRepository, ObjectMapper mapper) {
		this.orderRepository = orderRepository;
		this.eventRepository = eventRepository;
		this.mapper = mapper;
	}

	@SneakyThrows(JsonProcessingException.class)
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
