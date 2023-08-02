package org.formation.service;

import org.formation.domain.PaymentException;
import org.formation.domain.PaymentInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

@Service
public class EventHandler {

	
	@Autowired
	PaymentService paymentService;
	
	@KafkaListener(id="payment-service", topics = "payments-in")
	@SendTo
	String processPayment(PaymentInformation paymentInformation ) throws PaymentException {
		return paymentService.processPayment(paymentInformation.getFromAccount(), paymentInformation.getToAccount(), paymentInformation.getAmount());
	}
}
