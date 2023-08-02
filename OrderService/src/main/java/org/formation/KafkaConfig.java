package org.formation;

import org.apache.kafka.clients.admin.NewTopic;
import org.formation.event.OrderEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

import java.time.Duration;

@Configuration
public class KafkaConfig {

    @Value("${app.payment-in-channel}")
    String requestPaymentChannel;

    @Value("${app.payment-out-channel}")
    String responsePaymentChannel;
    @Bean
    public ConcurrentMessageListenerContainer<String, String> repliesContainer(
            ConcurrentKafkaListenerContainerFactory<String, String> containerFactory) {
        ConcurrentMessageListenerContainer<String, String> repliesContainer =  containerFactory.createContainer(responsePaymentChannel);
        repliesContainer.getContainerProperties().setGroupId("order-service");
        repliesContainer.setAutoStartup(true);
        return repliesContainer;
    }
    @Bean
    public ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate(
            ProducerFactory<String, String> pf,
            ConcurrentMessageListenerContainer<String, String> repliesContainer) throws InterruptedException {
        return new ReplyingKafkaTemplate<>(pf, repliesContainer);
    }
    @Bean
    public KafkaTemplate<Long, OrderEvent> kafkaTemplate(
    		DefaultKafkaProducerFactory<Long, OrderEvent> factory) {
        return new KafkaTemplate<Long, OrderEvent>(factory);
    }
    
    @Bean
    NewTopic requestTopic() {
        return TopicBuilder.name(requestPaymentChannel).partitions(3).replicas(3).build();
    }

    @Bean
    NewTopic responseTopic() {
        return TopicBuilder.name(responsePaymentChannel).partitions(3).replicas(3).build();
    }
}
