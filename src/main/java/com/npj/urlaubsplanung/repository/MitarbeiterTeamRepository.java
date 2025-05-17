package com.npj.urlaubsplanung.repository;

import com.npj.urlaubsplanung.models.MitarbeiterTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MitarbeiterTeamRepository extends JpaRepository<MitarbeiterTeam, Integer> {

}
