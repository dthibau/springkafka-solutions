package org.formation;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;

public class KafkaConsumerThread implements Runnable, ConsumerRebalanceListener {

	public static String TOPIC = "avro-position";
	KafkaConsumer<String, GenericRecord> consumer;

	private int id;

	

	public KafkaConsumerThread(int id) {
		this.id = id;

		_initConsumer();

	}

	@Override
	public void run() {
		int nbMessages = 0;
		try {
			while (true) {
				ConsumerRecords<String, GenericRecord> records = consumer.poll(Duration.ofMillis(100));
				System.out.println("Consumer " + id + " vient de recevoir " + records.count() + " records");
				for (var record : records) {
					nbMessages++;
					PostgresConfig.insererOffset(record.value().get("id").toString(), record.offset());
				}
				System.out.println("Consumer " + id + " a reçu " + nbMessages + " messages au total");
			}
		} catch (WakeupException e) {
			System.out.println("CLOSING : Consumer " + id + " a reçu " + nbMessages + " messages au total");
		}  finally {
			consumer.close();
		}
	}

	private void _initConsumer() {
		Properties kafkaProps = new Properties();
		kafkaProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:19092,localhost:19093,localhost:19094");
		kafkaProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
		kafkaProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.KafkaAvroDeserializer");
		kafkaProps.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
		kafkaProps.put(ConsumerConfig.GROUP_ID_CONFIG, "position-consumer");
		kafkaProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		consumer = new KafkaConsumer<String, GenericRecord>(kafkaProps);
		consumer.subscribe(Collections.singletonList(TOPIC),this);
	}

	@Override
	public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
		System.out.println("Process " +  ProcessHandle.current().pid() + " Thread " +id + " Revocation of " + partitions);
		
	}

	@Override
	public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
		System.out.println("Process " +  ProcessHandle.current().pid() + " Thread " +id + " Assignement of " + partitions);		
	}
}
