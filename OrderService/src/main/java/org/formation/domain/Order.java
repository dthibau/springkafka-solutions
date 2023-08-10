package org.formation.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "t_order")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private Instant date;

	private float discount;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	@Embedded
	private PaymentInformation paymentInformation;

	@Embedded
	private DeliveryInformation deliveryInformation;

	@OneToMany(cascade = CascadeType.ALL)
	List<OrderItem> orderItems = new ArrayList<>();

}
