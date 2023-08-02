package org.formation.web;

import java.util.List;

import org.formation.domain.Coursier;
import org.formation.domain.CoursierRepository;
import org.formation.service.CoursierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/coursier")
public class CoursierController {

	@Autowired 
	CoursierRepository coursierRepository;
	@Autowired
	CoursierService coursierService;
	
	@GetMapping
	List<Coursier> findAll() {
		return coursierRepository.findAll(); 
	}

	@GetMapping("/affected")
	List<Coursier> findAffected() {
		return coursierRepository.findAffected();
	}
	@GetMapping("/unaffected")
	List<Coursier> findUnAffected() {
		return coursierRepository.findUnAffected();
	}

	@PatchMapping("{id}/move")
	Coursier moveCoursier(@PathVariable("id") long id, @RequestParam("lat") double lat, @RequestParam("lon") double lon) {

		return coursierService.moveCoursier(id, lat, lon);
	}
}
