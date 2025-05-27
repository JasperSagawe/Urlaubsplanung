package com.npj.urlaubsplanung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.npj.urlaubsplanung.model.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {

}
