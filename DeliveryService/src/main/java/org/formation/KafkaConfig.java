package org.formation;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.TopicConfig;
import org.formation.domain.Position;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConfig {

    @Value("${app.coursier-channel}")
    String coursierChannel;

    @Value("${app.livraison-channel}")
    String livraisonChannel;

    @Bean
    public RoutingKafkaTemplate routingTemplate(GenericApplicationContext context, ProducerFactory<Object, Object> defaultProducerFactory) {
        // Cloner le ProducerFactory par défaut avec un sérialiseur différent
        Map<String, Object> configs = new HashMap<>(defaultProducerFactory.getConfigurationProperties());
        configs.put(ProducerConfig.ACKS_CONFIG, "0");
        configs.put(ProducerConfig.RETRIES_CONFIG, "0");
        configs.put(ProducerConfig.LINGER_MS_CONFIG, "0");
        configs.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5");
        DefaultKafkaProducerFactory<Object, Object> coursierProducerFactory = new DefaultKafkaProducerFactory<>(configs);
        context.registerBean("coursierPF", DefaultKafkaProducerFactory.class, () -> coursierProducerFactory);

        Map<Pattern, ProducerFactory<Object, Object>> map = new LinkedHashMap<>();
        map.put(Pattern.compile(coursierChannel), coursierProducerFactory);
        map.put(Pattern.compile(".+"), defaultProducerFactory);
        return new RoutingKafkaTemplate(map);
    }

    @Bean
    public CommonErrorHandler errorHandler(@Qualifier("kafkaProducerFactory") ProducerFactory<Long, Object> pf) {
        return new DefaultErrorHandler(
                new DeadLetterPublishingRecoverer(new KafkaTemplate<>(pf)), new FixedBackOff(1000L, 4));
    }

    @Bean
    @Profile("!testcontainers")
    NewTopic coursierTopic() {
        return TopicBuilder.name(coursierChannel).partitions(10).replicas(2).build();
    }

    @Bean
    @Profile("!testcontainers")
    NewTopic livraisonTopic() {
        return TopicBuilder.name(livraisonChannel).partitions(1).replicas(3)
                .config(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG, String.valueOf(2)).build();
    }
}
