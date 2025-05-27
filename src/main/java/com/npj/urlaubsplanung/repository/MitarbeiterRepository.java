package com.npj.urlaubsplanung.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.npj.urlaubsplanung.model.Mitarbeiter;
import com.npj.urlaubsplanung.model.Team;

@Repository
public interface MitarbeiterRepository extends JpaRepository<Mitarbeiter, Integer> {
	Optional<Mitarbeiter> findByEmail(String email);
	List<Mitarbeiter> findByTeam(Team team);
	List<Mitarbeiter> findByTeamIsNull();
}
