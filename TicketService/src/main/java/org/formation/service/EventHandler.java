package org.formation.service;

import org.formation.controller.OrderDto;
import org.formation.event.OrderEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
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
    
    
    private int nbEvent=0;

    @KafkaListener(topics="#{'${app.channel.order-channel}'}", id="ticket-service")
    public void handleOrderEvent(OrderEvent orderEvent) throws JsonMappingException, JsonProcessingException {
    	log.info("Consuming orderEvent " + (nbEvent++) + " consumed");
        switch (orderEvent.getType() ) {
            case "ORDER_CREATED":
            	OrderDto orderDto = mapper.readValue(orderEvent.getPayload(), OrderDto.class);
                ticketService.createTicket(orderDto);
                break;

        }
    }
}
