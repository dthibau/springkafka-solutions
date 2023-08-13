package org.formation.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class PaymentService {

	
	public String processPayment(String fromAccount, String toAccount, Float amount) {
		return UUID.randomUUID().toString();
	}
}
