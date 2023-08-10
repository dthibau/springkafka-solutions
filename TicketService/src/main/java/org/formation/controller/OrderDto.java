package org.formation.controller;

import org.formation.domain.ProductRequest;

import com.fasterxml.jackson.annotation.JsonAlias;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
	@Min(1)
	@JsonAlias({ "id", "orderId" })
	long orderId;

	@JsonAlias({ "products", "orderItems" })
	ProductRequest[] products;
}
