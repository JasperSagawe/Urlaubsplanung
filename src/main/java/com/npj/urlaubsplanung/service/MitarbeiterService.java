package com.npj.urlaubsplanung.service;

import com.npj.urlaubsplanung.models.Mitarbeiter;
import com.npj.urlaubsplanung.repository.MitarbeiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MitarbeiterService {

  @Autowired
  private MitarbeiterRepository mitarbeiterRepository;

  public List<Mitarbeiter> getAlle() {
    return mitarbeiterRepository.findAll();
  }

  public Optional<Mitarbeiter> getById(Integer id) {
    return mitarbeiterRepository.findById(id);
  }

  public Mitarbeiter speichern(Mitarbeiter eintrag) {
    return mitarbeiterRepository.save(eintrag);
  }

  public void löschen(Integer id) {
    mitarbeiterRepository.deleteById(id);
  }
}
