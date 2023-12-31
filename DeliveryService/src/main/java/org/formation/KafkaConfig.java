package org.formation;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.core.RoutingKafkaTemplate;
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
    public RoutingKafkaTemplate routingTemplate(GenericApplicationContext context, ProducerFactory<Object, Object> pf) {
        // Cloner le ProducerFactory par défaut avec un sérialiseur différent
        Map<String, Object> configs = new HashMap<>(pf.getConfigurationProperties());
        configs.put(ProducerConfig.ACKS_CONFIG, "0");
        DefaultKafkaProducerFactory<Object, Object> coursierPF = new DefaultKafkaProducerFactory<>(configs);
        context.registerBean(DefaultKafkaProducerFactory.class, "coursierPF", coursierPF);

        Map<Pattern, ProducerFactory<Object, Object>> map = new LinkedHashMap<>();
        map.put(Pattern.compile(coursierChannel), coursierPF);
        map.put(Pattern.compile(".+"), pf);
        return new RoutingKafkaTemplate(map);
    }

    
    @Bean
    public CommonErrorHandler errorHandler(@Qualifier("kafkaProducerFactory") ProducerFactory<Long, Object> pf) {
      return new DefaultErrorHandler(
        new DeadLetterPublishingRecoverer(new KafkaTemplate<>(pf)), new FixedBackOff(1000L, 4));
    }
    
    @Bean
    NewTopic coursierTopic() {
        return TopicBuilder.name(coursierChannel).partitions(10).replicas(2).build();
    }

    @Bean
    NewTopic livraisonTopic() {
        return TopicBuilder.name(livraisonChannel).partitions(1).replicas(3)
                .config(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG, String.valueOf(2)).build();
    }
}
