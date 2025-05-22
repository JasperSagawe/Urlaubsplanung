package com.npj.urlaubsplanung.repository;

import com.npj.urlaubsplanung.model.Mitarbeiter;
import com.npj.urlaubsplanung.model.Urlaubsantrag;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlaubsantragRepository extends JpaRepository<Urlaubsantrag, Integer> {
    List<Urlaubsantrag> findByMitarbeiter(Mitarbeiter mitarbeiter);
    
    void deleteAllByMitarbeiter(Mitarbeiter mitarbeiter);
    
    void deleteAllByGenehmigtVon(Mitarbeiter mitarbeiter);
    
    
}
