package org.formation.service;

import org.formation.domain.BadPaymentInformation;
import org.formation.domain.PaymentInformation;
import org.springframework.kafka.support.serializer.FailedDeserializationInfo;

import java.util.function.Function;

public class FailPaymentInformationProvider implements Function<FailedDeserializationInfo, PaymentInformation> {

    @Override
    public PaymentInformation apply(FailedDeserializationInfo t) {
        return new BadPaymentInformation(t);
    }

}
