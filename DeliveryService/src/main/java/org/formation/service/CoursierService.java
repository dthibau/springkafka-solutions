package org.formation.service;

import org.formation.domain.Coursier;
import org.formation.domain.CoursierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.RoutingKafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.java.Log;

@Service
@Transactional
@Log
public class CoursierService {

    @Autowired
    CoursierRepository coursierRepository;

    @Value("${app.coursier-channel}")
    String coursierChannel;
    @Autowired
    RoutingKafkaTemplate routingTemplate;

    public Coursier moveCoursier(long id, double lat, double lon) {
        Coursier coursier = coursierRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        coursier.move(lat, lon);
        coursierRepository.save(coursier);
        routingTemplate.send(coursierChannel, coursier.getId(), coursier.getPosition());
        return coursier;
    }

}
