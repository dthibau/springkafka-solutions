package org.formation.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Position {

	private double latitude;
	private double longitude;
	
	public void move(double latitude, double longitude) {
		this.latitude += latitude;
		this.longitude += longitude;
	}
	
}
