package org.formation.web;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = {"spring.kafka.bootstrap-servers=localhost:9092","spring.kafka.consumer.auto-offset-reset=earliest"})
@EmbeddedKafka(partitions = 1, topics = {"${app.coursier-channel}"},
        brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PositionControllerTest {

    @Value("${app.coursier-channel}")
    String coursierChannel;

    @Autowired
    PositionController positionController;

    @Autowired
    EmbeddedKafkaBroker embeddedKafkaBroker;


    private Consumer<Long, String> consumer;


    @BeforeAll
    void setUp() {

        // On configure le consommateur pour les tests à partir des propriétés générées par le broker embarqué
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps(coursierChannel, "true", embeddedKafkaBroker);
       // consumerProps.put("auto.offset.reset", "earliest");

        consumer = new DefaultKafkaConsumerFactory<>(consumerProps, new LongDeserializer(), new StringDeserializer())
                .createConsumer();
        // On abonne le consommateur au topic à tester
        consumer.subscribe(Collections.singleton(coursierChannel));
    }

    @AfterAll
    void tearDown() {
        consumer.close();
    }

    @Test
    public void testSend() throws ExecutionException, InterruptedException {

        positionController.commande(1, "TEST");

        ConsumerRecord<Long, String> record = KafkaTestUtils.getSingleRecord(consumer, coursierChannel, Duration.ofSeconds(10));

        // Vérification du contenu du message
        assertEquals("\"TEST\"", record.value());


    }

}
