package org.formation.model;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

public class CoursierSerde implements Serde<Coursier> {
	
	Serializer<Coursier> courierSerializer = new JsonSerializer();
	Deserializer<Coursier> courierDeserializer = new JsonDeserializer();
	
	

	@Override
	public Serializer<Coursier> serializer() {
		// TODO Auto-generated method stub
		return courierSerializer;
	}

	@Override
	public Deserializer<Coursier> deserializer() {
		// TODO Auto-generated method stub
		return courierDeserializer;
	}

}
