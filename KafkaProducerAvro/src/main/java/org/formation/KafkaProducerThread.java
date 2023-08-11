package org.formation;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.formation.model.Coursier;
import org.formation.model.Position;
import org.formation.model.SendMode;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;

public class KafkaProducerThread implements Runnable {

	KafkaProducer<String,Coursier> producer;
	private long nbMessages,sleep;
	private SendMode sendMode;
	
	private Coursier coursier;
	
	private ProducerCallback callback = new ProducerCallback();
	
	public KafkaProducerThread(String id, long nbMessages, long sleep, SendMode sendMode) {
		this.nbMessages = nbMessages;
		this.sleep = sleep;
		this.sendMode = sendMode;
		this.coursier = new Coursier(id, "David", 12, new Position(Math.random() + 45, Math.random() + 2));
		
		_initProducer();
		
	}

	@Override
	public void run() {
		
		for (int i =0; i< nbMessages; i++) {
			((Position)coursier.getPosition()).latitude += Math.random()-0.5;
			((Position)coursier.getPosition()).longitude += Math.random()-0.5;
			
			
			ProducerRecord<String, Coursier> producerRecord = new ProducerRecord<String, Coursier>(KafkaProducerApplication.TOPIC, coursier.getId().toString(), coursier);
			
			switch (sendMode) {
			case FIRE_AND_FORGET:
				fireAndForget(producerRecord);
				break;
			case SYNCHRONOUS:
				try {
					synchronous(producerRecord);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ExecutionException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			case ASYNCHRONOUS:
				asynchronous(producerRecord);
				break;
			default:
				break;
			}
			
			
			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				System.err.println("INTERRUPTED");
			}
		}
		
	}
	
	public void fireAndForget(ProducerRecord<String,Coursier> record) {
		
		producer.send(record);
		System.out.println("FireAndForget  - " + record);
		
	}
	
	public void synchronous(ProducerRecord<String,Coursier> record) throws InterruptedException, ExecutionException {
		RecordMetadata metaData = producer.send(record).get();
		System.out.println("Synchronous  - Partition :" + metaData.partition() + " Offset : "+ metaData.offset());
		
	}
	public void asynchronous(ProducerRecord<String,Coursier> record) {
		producer.send(record,callback);
	}
	
	private void _initProducer() {
		Properties kafkaProps = new Properties();
		kafkaProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:19092,localhost:19093,localhost:19094");
		kafkaProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringSerializer");
		kafkaProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
				"io.confluent.kafka.serializers.KafkaAvroSerializer");
		kafkaProps.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, KafkaProducerApplication.REGISTRY_URL);
		producer = new KafkaProducer<String, Coursier>(kafkaProps);

		// A compléter
	}
}
