package com.npj.urlaubsplanung.repository;

import com.npj.urlaubsplanung.model.Mitarbeiterdaten;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MitarbeiterdatenRepository extends JpaRepository<Mitarbeiterdaten, Integer> {

}
