package org.formation.service;

import java.util.concurrent.TimeUnit;

import org.formation.event.OrderEvent;
import org.formation.event.OrderEventRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.extern.java.Log;

@Service
@Log
public class EventService {
	@Value("${app.order-channel}")
    private String ORDER_CHANNEL;
    private final OrderEventRepository eventRepository;
    private final KafkaTemplate<Long, OrderEvent> kafkaTemplate;

    public EventService(OrderEventRepository eventRepository, KafkaTemplate<Long, OrderEvent> kafkaTemplate ) {
        this.eventRepository = eventRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelay = 10l, timeUnit = TimeUnit.SECONDS)
    public void relayEvents() {
        eventRepository.findAll()
                .forEach(event -> {
                    log.info("Sending event "+event);
                    kafkaTemplate.send(ORDER_CHANNEL, event.getOrderId(), event).whenComplete((result, error) -> {
                        if (error != null) {
                            log.severe("Error sending event "+event);
                        } else {
                            log.info("Event "+event+" sent");
                            eventRepository.delete(event);
                        }
                    });
                });
    }
}
