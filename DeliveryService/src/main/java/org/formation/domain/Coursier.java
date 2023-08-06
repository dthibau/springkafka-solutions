package org.formation.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Coursier {

	@Id
	private Long id;
	
	@Embedded
	private Position position;
	
	@OneToOne(optional = true)
	@JsonBackReference
	Livraison livraison;
	
	public void move(double latitude, double longitude) {
		position.setLatitude(latitude);
		position.setLongitude(longitude);
	}


	
}
