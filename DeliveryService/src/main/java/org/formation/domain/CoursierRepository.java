package org.formation.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CoursierRepository extends JpaRepository<Coursier, Long> {

	@Query("from Coursier c where c.livraison is not null")
	List<Coursier> findAffected();
	
	@Query("from Coursier c where c.livraison is null")
	List<Coursier> findUnAffected();
}
