package org.formation;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Branched;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Named;
import org.apache.kafka.streams.kstream.Produced;
import org.formation.model.Coursier;
import org.formation.model.serde.CoursierSerde;
import org.formation.model.Position;
import org.formation.model.serde.PositionSerde;

public class PositionStream {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Propriétés : ID, BOOTSTRAP, Serialiseur/Désérialiseur        
		Properties props = new Properties();
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, "position-streams");
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:19092");
		props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, CoursierSerde.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        Serde<Position> positionSerde = new PositionSerde();
        Serde<String> stringSerde = Serdes.String();

		// Création d’une topolgie de processeurs 
		final StreamsBuilder builder = new StreamsBuilder();

		
		KStream<String, Coursier> coursierStream = builder.<String, Coursier>stream("position");
        coursierStream.mapValues(c -> {
            Position position = c.getCurrentPosition();
            position.setLatitude(Math.round(position.getLatitude()));
            position.setLongitude(Math.round(position.getLongitude()));
            return c;
        }).selectKey((k, v) -> v.getCurrentPosition())
                .mapValues(c -> c.getId())
                .split(Named.as("branche-"))
                .branch((k, v) -> k.getLatitude() > 45,  Branched.withConsumer(ks -> ks.to("position-north", Produced.with(positionSerde,stringSerde))))
                .branch((k, v) -> k.getLatitude() <= 45, Branched.withConsumer(ks -> ks.to("position-south", Produced.with(positionSerde,stringSerde))));




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
