package com.npj.urlaubsplanung.service;

import com.npj.urlaubsplanung.models.Urlaubsstatus;
import com.npj.urlaubsplanung.repository.UrlaubsstatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UrlaubsstatusService {

  @Autowired
  private UrlaubsstatusRepository urlaubsstatusRepository;

  public List<Urlaubsstatus> getAlle() {
    return urlaubsstatusRepository.findAll();
  }

  public Optional<Urlaubsstatus> getById(Integer id) {
    return urlaubsstatusRepository.findById(id);
  }

  public Urlaubsstatus speichern(Urlaubsstatus eintrag) {
    return urlaubsstatusRepository.save(eintrag);
  }

  public void löschen(Integer id) {
    urlaubsstatusRepository.deleteById(id);
  }
}
