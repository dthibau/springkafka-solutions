package org.formation.web;

import org.formation.domain.PaymentException;
import org.formation.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

	@Autowired
	PaymentService paymentService;
	
	@PostMapping
	public String processPayment(String fromAccount, String toAccount, Float amount) throws PaymentException {
		return paymentService.processPayment(fromAccount, toAccount, amount);
	}
}
