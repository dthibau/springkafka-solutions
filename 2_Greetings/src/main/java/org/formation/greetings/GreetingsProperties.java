package org.formation.greetings;

import org.hibernate.validator.constraints.Range;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Component
@ConfigurationProperties("hello")
public class GreetingsProperties {
	/**
	 * Greeting message returned by the Hello Rest service.
	 */
	@NotEmpty
	private String greeting;
	/**
	 * La casse utilisée pour le nom.
	 */
	@NotNull
	private CaseEnum styleCase;
	@Range(min = 0, max = 1)
	/**
	 * La position du nom.
	 */
	private int position;

	public String getGreeting() {
		return greeting;
	}

	public void setGreeting(String greeting) {
		this.greeting = greeting;
	}

	public CaseEnum getStyleCase() {
		return styleCase;
	}

	public void setStyleCase(CaseEnum styleCase) {
		this.styleCase = styleCase;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
	
}
