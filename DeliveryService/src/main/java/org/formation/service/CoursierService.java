package org.formation.service;

import org.formation.domain.Coursier;
import org.formation.domain.CoursierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class CoursierService {

    @Autowired
    CoursierRepository coursierRepository;

    public Coursier moveCoursier(long id, double lat, double lon) {
        Coursier coursier = coursierRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        coursier.move(lat, lon);
        coursierRepository.save(coursier);
        return coursier;
    }

}
