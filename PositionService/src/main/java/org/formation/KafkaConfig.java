package org.formation;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.DelegatingSerializer;


@Configuration
public class KafkaConfig {
	
	@Bean
	public ProducerFactory<Long, Object> producerFactory() {
	    return new DefaultKafkaProducerFactory<>(producerConfigs());
	}

	@Bean
	public Map<String, Object> producerConfigs() {
	    Map<String, Object> props = new HashMap<>();
	    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:19092, localhost:19093,localhost:19094");
	    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
	    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, DelegatingSerializer.class);
	    props.put(DelegatingSerializer.VALUE_SERIALIZATION_SELECTOR_CONFIG,
	    	    "commande:org.apache.kafka.common.serialization.StringSerializer, position:org.springframework.kafka.support.serializer.JsonSerializer");
	    
	    props.put(ProducerConfig.RETRIES_CONFIG, 5);
	    props.put(ProducerConfig.ACKS_CONFIG, "all");
	    return props;
	}
	
	@Bean
	public KafkaTemplate<Long, Object> kafkaTemplate() {
	    return new KafkaTemplate<Long, Object>(producerFactory());
	}
}
