package org.formation.service;

import jakarta.annotation.Resource;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.support.serializer.DelegatingSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = "spring.kafka.consumer.auto-offset-reset=earliest")
@EmbeddedKafka(partitions = 10, topics = {"${app.coursier-channel}"}, count = 3, kraft = true)
@DirtiesContext
public class EventHandlerTest {


    @Value("${app.coursier-channel}")
    String coursierChannel;

    @Resource
    DefaultKafkaProducerFactory kafkaProducerFactory;

    @Autowired
    EventHandler eventHandler;


    @Test
    public void test() throws ExecutionException, InterruptedException {

        KafkaTemplate kafkaTemplate = new KafkaTemplate((ProducerFactory) kafkaProducerFactory);
        List<Header> headers = new ArrayList<>();
        headers.add(new RecordHeader(DelegatingSerializer.VALUE_SERIALIZATION_SELECTOR, "commande".getBytes()));

        ProducerRecord<Long, String> record = new ProducerRecord<Long, String>(coursierChannel, null, 1l, "TEST", headers);

        // Envoi synchrone
        kafkaTemplate.send(record).get();

        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            assertEquals(1, eventHandler.process );
        });

    }
}
