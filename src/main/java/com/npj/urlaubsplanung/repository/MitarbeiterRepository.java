package com.npj.urlaubsplanung.repository;

import com.npj.urlaubsplanung.model.Mitarbeiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MitarbeiterRepository extends JpaRepository<Mitarbeiter, Integer> {
	Optional<Mitarbeiter> findByEmail(String email);
}
