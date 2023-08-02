package org.formation.service;

import java.util.UUID;

import org.formation.domain.PaymentException;
import org.springframework.stereotype.Service;

import lombok.extern.java.Log;

@Service
@Log
public class PaymentService {

	
	public String processPayment(String fromAccount, String toAccount, Float amount) throws PaymentException {
		log.info("Receiving process payment");
		if ( fromAccount.equalsIgnoreCase("BAD_ACCOUNT") )
			throw new PaymentException("Bad account");
		return UUID.randomUUID().toString();
	}
}
