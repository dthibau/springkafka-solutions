package org.formation.controller;

import jakarta.validation.Valid;
import org.formation.domain.Ticket;
import org.formation.domain.TicketRepository;
import org.formation.domain.TicketStatus;
import org.formation.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

	@Autowired
	TicketRepository ticketRepository;

	@Autowired
	TicketService ticketService;

	@GetMapping
	public Iterable<Ticket> findAll() {
		return ticketRepository.findAll();
	}
	@GetMapping(path = "/{id}")
	public Ticket findById(@PathVariable long id) {
		return ticketRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@GetMapping(path = "/orders/{orderId}")
	public Ticket findByOrderId(@PathVariable long orderId) {
		return ticketRepository.findByOrderId(orderId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Ticket save(@Valid @RequestBody OrderDto orderDto) {
		return ticketService.createTicket(orderDto);
	}

	@PatchMapping(path = "/{id}/{status}")
	public Ticket updateStatus(@PathVariable long id, @PathVariable TicketStatus status) {	
		return ticketService.updateStatus(id, status);
	}
}
