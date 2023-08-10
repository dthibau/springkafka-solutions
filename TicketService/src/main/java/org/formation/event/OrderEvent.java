package org.formation.event;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Data;

@Data
public class OrderEvent {
    private long orderId;
    private String type;
    private String payload;
}
