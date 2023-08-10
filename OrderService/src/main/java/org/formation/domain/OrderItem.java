package org.formation.domain;

import java.time.Instant;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String refProduct;
	
	private float price;
	
	private int quantity;
	
	@Transient
	public float getTotal() {
		return price * quantity;
	}
	
}
