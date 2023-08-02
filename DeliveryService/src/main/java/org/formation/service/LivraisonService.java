package org.formation.service;

import org.formation.domain.Livraison;
import org.formation.domain.LivraisonRepository;
import org.formation.domain.LivraisonStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.RoutingKafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.java.Log;

@Service
@Transactional
@Log
public class LivraisonService {

    @Autowired
    LivraisonRepository livraisonRepository;

    @Value("${app.livraison-channel}")
    String livraisonChannel;
    @Autowired
    RoutingKafkaTemplate routingTemplate;
    
    public Livraison createLivraison(Livraison livraison) {
        livraison.setStatus(LivraisonStatus.CREATED);
        final Livraison ret = livraisonRepository.save(livraison);
        
        routingTemplate.send(livraisonChannel, livraison.getId(), livraison).whenComplete((r, e) -> {
            if (e != null) {
            	log.info("ERROR when sending : " + e.toString());
                ret.setStatus(LivraisonStatus.FAILED);
                livraisonRepository.save(livraison);
            } else {
                log.info("Message sent to Livraison channel offset : " + r.getRecordMetadata().offset());
            }
        });;
        return livraison;
    }
}
