package com.npj.urlaubsplanung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.npj.urlaubsplanung.model.Abteilung;

@Repository
public interface AbteilungRepository extends JpaRepository<Abteilung, Integer> {

}
