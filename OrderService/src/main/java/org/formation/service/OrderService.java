package org.formation.service;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;

@Service
@Transactional
@Log
public class OrderService {
	@Value("${app.payment-in-channel}")
	String requestPaymentChannel;
	@Autowired
	OrderRepository orderRepository;
	@Autowired
	OrderEventRepository eventRepository;
	@Autowired
	ObjectMapper mapper;

	@Resource
	ReplyingKafkaTemplate<Long, PaymentInformation, String> replyingKafkaTemplate;
	
	@SneakyThrows(JsonProcessingException.class)
	public Order createOrder(Order order)  {
		order.setDate(Instant.now());
		order.setStatus(OrderStatus.PENDING);
		final Order ret = orderRepository.save(order);
		OrderEvent event = OrderEvent.builder().type(OrderEventType.ORDER_CREATED).orderId(order.getId()).payload(mapper.writeValueAsString(order)).build();
		eventRepository.save(event);

		order.getPaymentInformation().setAmount(order.getTotal());
		ProducerRecord<Long, PaymentInformation> record = new ProducerRecord<Long, PaymentInformation>(requestPaymentChannel, order.getId(), order.getPaymentInformation());
		replyingKafkaTemplate.sendAndReceive(record).whenComplete((reply, ex) -> {
			OrderEvent event2 = null;
			if (ex != null) {
				log.info("Error in Request/Response");
				ret.setStatus(OrderStatus.REJECTED);
				event2 = OrderEvent.builder().type(OrderEventType.ORDER_CANCELLED).orderId(ret.getId()).payload(ex.getMessage()).build();
			} else {
				log.info("GET a transactionId " + reply.value());
				ret.getPaymentInformation().setTransactionId(reply.value());
				ret.setStatus(OrderStatus.APPROVED);
				try {
					event2 = OrderEvent.builder().type(OrderEventType.ORDER_PAID).orderId(ret.getId()).payload(mapper.writeValueAsString(ret)).build();
				} catch (JsonProcessingException e) {
					throw new RuntimeException(e);
				}

			}
			orderRepository.save(ret);
			eventRepository.save(event2);
		});
		return order;
	}
}
