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
	
	@KafkaHandler
	public void handleCoursierPosition(Position position,
			@Header(KafkaHeaders.RECEIVED_KEY) List<Long> coursierIds) {
		log.info("Receiving position");
	}
	
	@KafkaHandler
	public void handleCommande(String commande,
			@Header(KafkaHeaders.RECEIVED_KEY) Long coursierId) {
		log.info("Receiving commande");
	}
}
