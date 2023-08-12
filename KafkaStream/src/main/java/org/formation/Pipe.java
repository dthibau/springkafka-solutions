package org.formation;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.formation.model.Coursier;
import org.formation.model.CoursierSerde;

public class Pipe {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Propriétés : ID, BOOTSTRAP, Serialiseur/Désérialiseur        
		Properties props = new Properties();
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-pipe");
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:19092");
		props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, CoursierSerde.class);

		// Création d’une topolgie de processeurs 
		final StreamsBuilder builder = new StreamsBuilder();

		
		KStream<String, Coursier> coursierStream = builder.<String, Coursier>stream("position");
        coursierStream.filter((key, value) -> value.getCurrentPosition().getLatitude() > 48.8)
                .to("position-nord");



		final Topology topology = builder.build();

		// Instanciation du Stream à partir d’une topologie et des propriétés
		final KafkaStreams streams = new KafkaStreams(topology, props);
		
        final CountDownLatch latch = new CountDownLatch(1);

        // attach shutdown handler to catch control-c
        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });

        // Démarrage du stream
        try {
            streams.start();
            latch.await();
        } catch (Throwable e) {
            System.exit(1);
        }
        System.exit(0);
	}

}
