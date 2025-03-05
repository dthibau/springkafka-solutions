package org.formation.service;

import jakarta.annotation.Resource;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.support.serializer.DelegatingSerializer;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = "spring.kafka.consumer.auto-offset-reset=earliest")
@DirtiesContext
@ActiveProfiles("testcontainers")
public class EventHandlerTestContainerTest {

    @Container
    @ServiceConnection
    static KafkaContainer kafkaContainer = new KafkaContainer("7.4.0") ;;

    @Value("${app.coursier-channel}")
    String coursierChannel;

    @Resource
    DefaultKafkaProducerFactory kafkaProducerFactory;

    @Autowired
    EventHandler eventHandler;

    @Autowired
    ConsumerFactory<Long, String> consumerFactory;

    @Test
    public void test() throws ExecutionException, InterruptedException {

        KafkaTemplate kafkaTemplate = new KafkaTemplate((ProducerFactory) kafkaProducerFactory);
        List<Header> headers = new ArrayList<>();
        headers.add(new RecordHeader(DelegatingSerializer.VALUE_SERIALIZATION_SELECTOR, "commande".getBytes()));

        ProducerRecord<Long, String> record = new ProducerRecord<Long, String>(coursierChannel, null, 1l, "TEST", headers);

        // Envoi synchrone
        Object result = kafkaTemplate.send(record).get();

        await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
            assertEquals(1, eventHandler.process );
        });
    }
}
