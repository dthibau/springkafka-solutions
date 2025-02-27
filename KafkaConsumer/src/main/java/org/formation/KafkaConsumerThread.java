package org.formation;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.formation.model.Coursier;

public class KafkaConsumerThread implements Runnable {

	public static String TOPIC = "position";
	KafkaConsumer<String, Coursier> consumer;
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

				// A compléter, insérer dans la base, l'id du coursier et l'offset Kafka

				
			}
		} finally {
			consumer.close();
		}

	}

	private void _initConsumer() {

	}
}
