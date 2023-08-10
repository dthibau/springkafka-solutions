package org.formation.service;

import java.util.List;
import java.util.stream.Collectors;

import org.formation.domain.Order;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class TicketDto {
	@Min(1)
    long orderId;
    TicketProductRequest[] products;
    
    public TicketDto(Order order) {
    	this.orderId = order.getId();
    	List<TicketProductRequest> list = order.getOrderItems().stream().map(TicketProductRequest::new).toList();
    	products = new TicketProductRequest[list.size()];
    	list.toArray(products);
    	
   }
}
