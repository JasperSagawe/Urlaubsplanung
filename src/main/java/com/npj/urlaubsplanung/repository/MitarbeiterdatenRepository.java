package com.npj.urlaubsplanung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.npj.urlaubsplanung.model.Mitarbeiter;
import com.npj.urlaubsplanung.model.Mitarbeiterdaten;

@Repository
public interface MitarbeiterdatenRepository extends JpaRepository<Mitarbeiterdaten, Integer> {
	Mitarbeiterdaten findByMitarbeiter(Mitarbeiter mitarbeiter);

	int countByAbteilungId(Integer id);
}
