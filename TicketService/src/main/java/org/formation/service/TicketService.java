package org.formation.service;

import java.util.Arrays;

import org.formation.controller.OrderDto;
import org.formation.domain.Ticket;
import org.formation.domain.TicketRepository;
import org.formation.domain.TicketStatus;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class TicketService {
    private final TicketRepository ticketRepository;
    public TicketService(TicketRepository ticketRepository) {
        super();
        this.ticketRepository = ticketRepository;
    }

    public Ticket createTicket(OrderDto orderDto) {
        Ticket ticket = new Ticket();
        ticket.setOrderId(orderDto.getOrderId());
        ticket.setProductRequests(Arrays.asList(orderDto.getProducts()));
        ticket.setStatus(TicketStatus.PENDING);
        return ticketRepository.save(ticket);
    }
    
    public Ticket updateStatus(long id, TicketStatus status) {
    	Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		ticket.setStatus(status);
		return ticketRepository.save(ticket);
    }
}
