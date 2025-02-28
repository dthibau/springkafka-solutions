package org.formation;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.*;
import org.formation.model.Coursier;
import org.formation.model.Position;
import org.formation.model.SendMode;

public class KafkaProducerThread implements Runnable {

	public static String TOPIC ="position";
	KafkaProducer<String,Coursier> producer;
	private long nbMessages,sleep;
	private SendMode sendMode;
	
	private Coursier coursier;

	public KafkaProducerThread(String id, long nbMessages, long sleep, SendMode sendMode) {
		this.nbMessages = nbMessages;
		this.sleep = sleep;
		this.sendMode = sendMode;
		this.coursier = new Coursier(id, new Position(Math.random() + 45, Math.random() + 2));
		
		_initProducer();
		
	}

	@Override
	public void run() {
		
		for (int i =0; i< nbMessages; i++) {
			coursier.move();
			
			ProducerRecord<String, Coursier> producerRecord = new ProducerRecord<String, Coursier>(TOPIC, coursier.getId(), coursier);
			
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

		producer.send(record, new Callback() {
			@Override
			public void onCompletion(RecordMetadata recordMetadata, Exception e) {
				if (e != null) {
					e.printStackTrace();
				} else {
					System.out.println("Asynchronous  - Partition :" + recordMetadata.partition() + " Offset : "+ recordMetadata.offset());
				}
			}
		});
	}
	
	private void _initProducer() {
		Properties kafkaProps = new Properties();
		kafkaProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:19092,localhost:19093,localhost:19094");
		kafkaProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringSerializer");
		kafkaProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
				"org.formation.JsonSerializer");
		producer = new KafkaProducer<String, Coursier>(kafkaProps);

		// A compléter
	}
}
