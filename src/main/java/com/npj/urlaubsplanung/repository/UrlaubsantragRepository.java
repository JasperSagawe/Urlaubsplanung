package com.npj.urlaubsplanung.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.npj.urlaubsplanung.model.Mitarbeiter;
import com.npj.urlaubsplanung.model.Urlaubsantrag;

@Repository
public interface UrlaubsantragRepository extends JpaRepository<Urlaubsantrag, Integer> {
	List<Urlaubsantrag> findByMitarbeiter(Mitarbeiter mitarbeiter);
}
