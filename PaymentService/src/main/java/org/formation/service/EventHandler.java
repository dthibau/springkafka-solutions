package org.formation.service;

import org.formation.domain.PaymentException;
import org.formation.domain.PaymentInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.java.Log;

@Service
@Log
public class EventHandler {

	
	@Autowired
	PaymentService paymentService;
	
	@KafkaListener(id="payment-service", topics = "payments-in")
	@SendTo
	@Transactional("kafkaTransactionManager")
	String processPayment(PaymentInformation paymentInformation ) throws PaymentException {
		log.info("Receiving Payment Request with : " + paymentInformation);
		if ( paymentInformation.getFromAccount() == null || paymentInformation.getToAccount() == null ) {
			log.info("SKIPPING PROCESS of BadPaymentInfo ");
			return "NOK";
		}
		return paymentService.processPayment(paymentInformation.getFromAccount(), paymentInformation.getToAccount(), paymentInformation.getAmount());
	}
}
