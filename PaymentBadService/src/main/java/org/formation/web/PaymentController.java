package org.formation.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bad")
public class PaymentController {

	@Autowired
	KafkaTemplate<Long,byte[]> kafkaTemplate;
	
	@PostMapping
	public void processPayment() {
		kafkaTemplate.send("payments-in",Long.valueOf(1),"bad".getBytes());	}
}
