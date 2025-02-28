package org.formation;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.avro.Schema;
import org.formation.model.SendMode;

import io.confluent.kafka.schemaregistry.avro.AvroSchema;
import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;

public class KafkaProducerApplication {
	public static String TOPIC ="avro-position";
	public static String REGISTRY_URL = "http://localhost:9091";
	
	public static void main(String[] args) throws URISyntaxException {

		int nbThreads = 0;
		long nbMessages = 0;
		int sleep = 1000;
		SendMode sendMode = SendMode.FIRE_AND_FORGET;

		try {
			nbThreads = Integer.parseInt(args[0]);
			nbMessages = Long.parseLong(args[1]);
			sleep = Integer.parseInt(args[2]);
			int mode = Integer.parseInt(args[3]);
			if (mode == 0) {
				sendMode = SendMode.FIRE_AND_FORGET;
			} else if (mode == 1) {
				sendMode = SendMode.SYNCHRONOUS;
			} else {
				sendMode = SendMode.ASYNCHRONOUS;
			}
		} catch (Exception e) {
			System.err.println("Usage is <run> <nbThreads> <nbMessages> <sleep> <0|1|2>");
			System.exit(1);
		}

		ExecutorService executorService = Executors.newFixedThreadPool(nbThreads);
		
		long top = System.currentTimeMillis();

		for (int i = 0; i < nbThreads; i++) {
			Runnable r = new KafkaProducerThread("" + i, nbMessages, sleep, sendMode);
			executorService.execute(r);
		}

		executorService.shutdown();

		try {
			System.out.println(executorService.awaitTermination(10, TimeUnit.MINUTES));
		} catch (InterruptedException e) {
			System.err.println("INTERRUPTED");
		}
		System.out.println("Execution in "+ (System.currentTimeMillis()-top) + "ms");
		System.exit(0);
	}

	private static void _initRegistry() throws IOException, RestClientException {

		// avro schema avsc file path.
		String schemaPath = "/Coursier.avsc";
		// subject convention is "<topic-name>-value"
		String subject = TOPIC + "-value";

		InputStream inputStream = KafkaProducerApplication.class.getResourceAsStream(schemaPath);

		Schema avroSchema = new Schema.Parser().parse(inputStream);

		CachedSchemaRegistryClient client = new CachedSchemaRegistryClient(REGISTRY_URL, 20);

		client.register(subject, new AvroSchema(avroSchema));

	}
}
