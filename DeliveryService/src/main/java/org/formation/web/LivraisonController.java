package org.formation.web;

import java.util.List;

import org.formation.domain.Livraison;
import org.formation.domain.LivraisonRepository;
import org.formation.service.LivraisonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/livraison")
public class LivraisonController {

	@Autowired
	LivraisonRepository livraisonRepository;
	
	@Autowired
	LivraisonService livraisonService;
	
	@GetMapping
	List<Livraison> findAll() {
		return livraisonRepository.findAll();
	}
	
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Livraison createLivraison(@Valid @RequestBody Livraison livraison) {
    	return livraisonService.createLivraison(livraison);
    }
}
