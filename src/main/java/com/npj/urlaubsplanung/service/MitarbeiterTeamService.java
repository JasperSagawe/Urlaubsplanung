package com.npj.urlaubsplanung.service;

import com.npj.urlaubsplanung.models.MitarbeiterTeam;
import com.npj.urlaubsplanung.repository.MitarbeiterTeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MitarbeiterTeamService {

  @Autowired
  private MitarbeiterTeamRepository mitarbeiterTeamRepository;

  public List<MitarbeiterTeam> getAlle() {
    return mitarbeiterTeamRepository.findAll();
  }

  public Optional<MitarbeiterTeam> getById(Integer id) {
    return mitarbeiterTeamRepository.findById(id);
  }

  public MitarbeiterTeam speichern(MitarbeiterTeam eintrag) {
    return mitarbeiterTeamRepository.save(eintrag);
  }

  public void löschen(Integer id) {
    mitarbeiterTeamRepository.deleteById(id);
  }
}
