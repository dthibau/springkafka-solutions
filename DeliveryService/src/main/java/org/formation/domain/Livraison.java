package org.formation.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Livraison {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private long orderId;

	private long ticketId;
	
	@Enumerated
	LivraisonStatus status;
	
	@OneToOne(optional = true, mappedBy = "livraison")
	@JsonManagedReference
	Coursier coursier;
	
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
