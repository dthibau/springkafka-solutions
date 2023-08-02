package org.formation.service;

import org.formation.domain.Livraison;
import org.formation.domain.LivraisonRepository;
import org.formation.domain.LivraisonStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LivraisonService {

    @Autowired
    LivraisonRepository livraisonRepository;

    public Livraison createLivraison(Livraison livraison) {
        livraison.setStatus(LivraisonStatus.CREATED);
        return livraisonRepository.save(livraison);
    }
}
