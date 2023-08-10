package org.formation.event;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderEvent {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;
	@NotNull
	private long orderId;
	private OrderEventType type;
	@Column(length = 10000)
	private String payload;
}
