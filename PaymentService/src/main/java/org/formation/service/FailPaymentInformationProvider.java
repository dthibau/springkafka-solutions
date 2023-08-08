package org.formation.service;

import java.util.function.Function;

import org.formation.domain.BadPaymentInformation;
import org.formation.domain.PaymentInformation;
import org.springframework.kafka.support.serializer.FailedDeserializationInfo;

public class FailPaymentInformationProvider implements Function<FailedDeserializationInfo, PaymentInformation> {

	@Override
	public PaymentInformation apply(FailedDeserializationInfo t) {
		return new BadPaymentInformation(t);
	}

}
