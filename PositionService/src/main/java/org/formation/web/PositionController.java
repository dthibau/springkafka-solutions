package org.formation.web;

import org.formation.domain.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/positions")
public class PositionController {


    @Value("${app.coursier-channel}")
    String coursierChannel;
    @Autowired
    KafkaTemplate<Long,Position> kafkaTemplate;
	
	@PatchMapping("{id}")
	Mono<Void> getPosition(@PathVariable("id") long id, @RequestParam("lat") double lat, @RequestParam("lon") double lon) {
		
		kafkaTemplate.send(coursierChannel, id, new Position(lat,lon));
		
		return Mono.empty();
	}
}
