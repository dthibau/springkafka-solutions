package org.formation.service;

import org.formation.domain.CoursierRepository;
import org.formation.domain.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.RoutingKafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public void moveCoursier(long id, double lat, double lon) {

        routingTemplate.send(coursierChannel, id, new Position(lat,lon));
    }

}
