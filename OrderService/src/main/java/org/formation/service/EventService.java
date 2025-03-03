package org.formation.service;

import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.formation.domain.Order;
import org.formation.domain.OrderRepository;
import org.formation.domain.OrderStatus;
import org.formation.domain.PaymentInformation;
import org.formation.event.OrderEvent;
import org.formation.event.OrderEventRepository;
import org.formation.event.OrderEventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import lombok.extern.java.Log;
import org.springframework.transaction.annotation.Transactional;


@Service
@Log
public class EventService {
    @Value("${app.payment-in-channel}")
    String REQUEST_PAYMENT_CHANNEL;
    @Value("${app.order-channel}")
    String ORDER_CHANNEL="orders";

    @Autowired
    private OrderEventRepository eventRepository;
    @Autowired
    OrderRepository orderRepository;
    @Resource
    private KafkaTemplate<Long, OrderEvent> kafkaTemplate;
    @Resource
    ReplyingKafkaTemplate<Long, PaymentInformation, String> replyingKafkaTemplate;
    @Autowired
    ObjectMapper mapper;


    @Scheduled(fixedDelay = 1l, timeUnit = TimeUnit.MINUTES)
    @Transactional("kafkaTransactionManager")
    public void relayEvents() {
        eventRepository.findAll()
                .forEach(event -> {
                    log.info("Sending event "+event);
                    kafkaTemplate.executeInTransaction(kafkaTemplate -> {
                        kafkaTemplate.send(ORDER_CHANNEL, event.getOrderId(), event).whenComplete((result, error) -> {
                            if (error != null) {
                                log.severe("Error sending event "+event);
                            } else {
                                log.info("Event "+event+" sent");
                                eventRepository.delete(event);
                            }
                        });
                        if ( event.getType() == OrderEventType.ORDER_CREATED) {
                           requestPayment(event.getPayload());
                        }
                        return true;
                    });
                });
    }

    @SneakyThrows
    public void requestPayment(String payload)  {

        Order order = mapper.readValue(payload, Order.class);
        ProducerRecord<Long, PaymentInformation> record = new ProducerRecord<Long, PaymentInformation>(REQUEST_PAYMENT_CHANNEL, order.getId(), order.getPaymentInformation());

        log.info("Requesting payment "+order.getPaymentInformation());

        replyingKafkaTemplate.sendAndReceive(record).whenComplete((reply, ex) -> {
            OrderEvent event = null;
            if (ex != null) {
                log.info("Payment rejected");
                order.setStatus(OrderStatus.REJECTED);
                event = OrderEvent.builder().type(OrderEventType.ORDER_CANCELLED).orderId(order.getId()).payload(ex.getMessage()).build();
            } else {
                log.info("Payment approved");
                order.setStatus(OrderStatus.APPROVED);
                try {
                    event = OrderEvent.builder().type(OrderEventType.ORDER_PAID).orderId(order.getId()).payload(mapper.writeValueAsString(order)).build();
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

            }
            orderRepository.save(order);
            eventRepository.save(event);
        });
    }
}
