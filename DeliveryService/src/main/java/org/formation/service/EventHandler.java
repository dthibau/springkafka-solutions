package org.formation.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.formation.domain.Coursier;
import org.formation.domain.CoursierRepository;
import org.formation.domain.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.java.Log;

@Service
@Log
public class EventHandler {

	@Autowired 
	CoursierRepository coursierRepository;
	
	@KafkaListener(topics = "${app.coursier-channel}" )
	public void handleCoursierPosition(List<Position> positions,
			@Header(KafkaHeaders.RECEIVED_KEY) List<Long> coursierIds) {
		Map<Long,Position> map = new HashMap<>();
		for (int i=0; i< positions.size(); i++) {
			map.put(coursierIds.get(i), positions.get(i));
		}
		log.info(positions.size() + " positions received  Pour " + map.size() + " coursiers");
		
		map.forEach((id,position) -> {
			Coursier coursier = coursierRepository.findById(id).orElse(Coursier.builder().id(id).build());
			coursier.setPosition(position);
			coursierRepository.save(coursier);
		});
	}
}
