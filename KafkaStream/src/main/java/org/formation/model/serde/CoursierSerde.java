package org.formation.model.serde;

import org.apache.kafka.common.serialization.Serdes;
import org.formation.model.Coursier;

public class CoursierSerde extends Serdes.WrapperSerde<Coursier> {

	public CoursierSerde() {
		super(new JsonPojoSerializer<>(), new JsonPojoDeserializer<>(Coursier.class));
	}
}