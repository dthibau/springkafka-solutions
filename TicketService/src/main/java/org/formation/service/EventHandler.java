package org.formation.service;

import org.formation.controller.OrderDto;
import org.formation.event.OrderEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.java.Log;

@Service
@Log
public class EventHandler {

    @Autowired
    TicketService ticketService;
    
    @Autowired
    ObjectMapper mapper;

    @KafkaListener(topics="#{'${app.channel.order-channel}'}", id="ticket-service")
    public void handleOrderEvent(@Payload OrderEvent orderEvent, Acknowledgment acknowledgment) throws JsonMappingException, JsonProcessingException {
        switch (orderEvent.getType() ) {
            case "ORDER_PAID":
            	OrderDto orderDto = mapper.readValue(orderEvent.getPayload(), OrderDto.class);
            	log.info("Order paid creating Ticket with "+ orderDto);
                ticketService.createTicket(orderDto);
                acknowledgment.acknowledge();
                break;

        }
    }
}
