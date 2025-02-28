package org.formation.model.serde;


import org.apache.kafka.common.serialization.Serdes;
import org.formation.model.Position;

public class PositionSerde extends Serdes.WrapperSerde<Position> {

    public PositionSerde() {
        super(new JsonPojoSerializer<>(), new JsonPojoDeserializer<>(Position.class));
    }
}