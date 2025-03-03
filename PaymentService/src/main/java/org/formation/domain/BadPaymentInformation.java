package org.formation.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.kafka.support.serializer.FailedDeserializationInfo;

@EqualsAndHashCode(callSuper = true)
@Data
public class BadPaymentInformation extends PaymentInformation {

    private final FailedDeserializationInfo failedDeserializationInfo;

    public BadPaymentInformation(FailedDeserializationInfo failedDeserializationInfo) {
        this.failedDeserializationInfo = failedDeserializationInfo;
    }


}
