package org.formation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;

@SpringBootApplication
@EnableScheduling
public class Application {

	public static void main(String[] args) {

		ApplicationContext context = SpringApplication.run(Application.class, args);
		Arrays.stream(context.getBeanDefinitionNames())
				.filter(beanName -> beanName.toLowerCase().contains("kafka"))
				.forEach(System.out::println);

		DefaultKafkaProducerFactory kafkaProducerFactory = (DefaultKafkaProducerFactory)context.getBean("kafkaProducerFactory");
		kafkaProducerFactory.getConfigurationProperties().forEach((k,v) -> System.out.println(k + " : " + v));


	}

}
