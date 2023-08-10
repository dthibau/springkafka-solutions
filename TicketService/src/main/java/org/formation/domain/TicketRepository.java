package org.formation.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

	public Optional<Ticket> findByOrderId(Long orderId);
}
