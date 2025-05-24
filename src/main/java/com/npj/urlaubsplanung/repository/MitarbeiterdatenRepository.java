package com.npj.urlaubsplanung.repository;

import com.npj.urlaubsplanung.model.Mitarbeiterdaten;
import com.npj.urlaubsplanung.model.Mitarbeiter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MitarbeiterdatenRepository extends JpaRepository<Mitarbeiterdaten, Integer> {
    Mitarbeiterdaten findByMitarbeiter(Mitarbeiter mitarbeiter);
}
