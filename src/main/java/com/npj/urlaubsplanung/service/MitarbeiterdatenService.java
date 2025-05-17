package com.npj.urlaubsplanung.service;

import com.npj.urlaubsplanung.models.Mitarbeiterdaten;
import com.npj.urlaubsplanung.repository.MitarbeiterdatenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MitarbeiterdatenService {

  @Autowired
  private MitarbeiterdatenRepository urlaubsdatenRepository;

  public List<Mitarbeiterdaten> getAlle() {
    return urlaubsdatenRepository.findAll();
  }

  public Optional<Mitarbeiterdaten> getById(Integer id) {
    return urlaubsdatenRepository.findById(id);
  }

  public Mitarbeiterdaten speichern(Mitarbeiterdaten eintrag) {
    return urlaubsdatenRepository.save(eintrag);
  }

  public void löschen(Integer id) {
    urlaubsdatenRepository.deleteById(id);
  }
}
