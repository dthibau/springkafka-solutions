package org.formation.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.formation.domain.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.DelegatingSerializer;
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
    KafkaTemplate<Long,Object> kafkaTemplate;
	
	@PatchMapping("{id}")
	Mono<Void> position(@PathVariable("id") long id, @RequestParam("lat") double lat, @RequestParam("lon") double lon) {
		
		List <Header> headers = new ArrayList<>();
		headers.add(new RecordHeader(DelegatingSerializer.VALUE_SERIALIZATION_SELECTOR, "position".getBytes()));
		
		ProducerRecord<Long, Object> record = new ProducerRecord<Long, Object>(coursierChannel, null, id, new Position(lat,lon),headers);
		
		kafkaTemplate.send(record);
		
		return Mono.empty();
	}
	
	@PatchMapping("{id}/{commande}")
	Mono<Void> commande(@PathVariable("id") long id, @PathVariable("commande") String commande) {
		
		List <Header> headers = new ArrayList<>();
		headers.add(new RecordHeader(DelegatingSerializer.VALUE_SERIALIZATION_SELECTOR, "commande".getBytes()));
		
		ProducerRecord<Long, Object> record = new ProducerRecord<Long, Object>(coursierChannel, null, id, commande,headers);
		
		kafkaTemplate.send(record);
		
		return Mono.empty();
	}
	

	
	
}
