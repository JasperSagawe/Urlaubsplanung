package com.npj.urlaubsplanung.service;

import com.npj.urlaubsplanung.models.Urlaubsantrag;
import com.npj.urlaubsplanung.repository.UrlaubsantragRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UrlaubsantragService {

  @Autowired
  private UrlaubsantragRepository urlaubsantragRepository;

  public List<Urlaubsantrag> getAlle() {
    return urlaubsantragRepository.findAll();
  }

  public Optional<Urlaubsantrag> getById(Integer id) {
    return urlaubsantragRepository.findById(id);
  }

  public Urlaubsantrag speichern(Urlaubsantrag eintrag) {
    return urlaubsantragRepository.save(eintrag);
  }

  public void löschen(Integer id) {
    urlaubsantragRepository.deleteById(id);
  }
}
