package org.formation.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryInformation {


	@Embedded
	@AttributeOverrides({
	@AttributeOverride(name="codePostal",column=@Column(name="PICK_CP")),
	@AttributeOverride(name="rue", column=@Column(name="PICK_RUE")),
	@AttributeOverride(name="ville",column=@Column(name="PICK_VILLE"))})
	Address pickAddress;
	
	@Embedded
	@AttributeOverrides({
	@AttributeOverride(name="codePostal",column=@Column(name="DELIVERY_CP")),
	@AttributeOverride(name="rue",column=@Column(name="DELIVERY_RUE")),
	@AttributeOverride(name="ville",column=@Column(name="DELIVERY_VILLE"))})
	Address deliveryAddress;
}
