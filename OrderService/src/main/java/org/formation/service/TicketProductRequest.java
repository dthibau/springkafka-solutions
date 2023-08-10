package org.formation.service;

import org.formation.domain.OrderItem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TicketProductRequest {

	@NotNull
	private String reference;
	@Min(1)
	private int quantity;

	public TicketProductRequest(OrderItem orderItem) {
		this.reference = orderItem.getRefProduct();
		this.quantity = orderItem.getQuantity();
	}
	
}
