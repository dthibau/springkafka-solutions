package org.formation.service;

import java.util.List;

import org.formation.domain.CoursierRepository;
import org.formation.domain.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import lombok.extern.java.Log;

@Service
@Log
@KafkaListener(topics = "${app.coursier-channel}" )
public class EventHandler {

	@Autowired 
	CoursierRepository coursierRepository;

	int process = 0;

	int retry = 0;
	
	@KafkaHandler
	public void handleCoursierPosition(Position position,
			@Header(KafkaHeaders.RECEIVED_KEY) List<Long> coursierIds) {
		log.info("Receiving position");
	}
	
	@KafkaHandler
	public void handleCommande(String commande,
			@Header(KafkaHeaders.RECEIVED_KEY) Long coursierId,
			@Header(KafkaHeaders.OFFSET) int offset) {
		log.info("Receiving commande offset is " + offset);
		if ( offset%10 == 0 ) {
			throw new RuntimeException("Boom");
		} else if ( offset%5 == 0 && retry < 3 ) {
			log.info("Retrying offset " + offset);
			retry++;
			throw new RuntimeException("Boom");
		} else if ( offset%5 == 0 && retry >= 3 ) {
			retry=0;
			process++;
		}else {
			process++;
		}		
	}
}
