package com.npj.urlaubsplanung.service;

import com.npj.urlaubsplanung.models.Team;
import com.npj.urlaubsplanung.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamService {

  @Autowired
  private TeamRepository teamRepository;

  public List<Team> getAlle() {
    return teamRepository.findAll();
  }

  public Optional<Team> getById(Integer id) {
    return teamRepository.findById(id);
  }

  public Team speichern(Team eintrag) {
    return teamRepository.save(eintrag);
  }

  public void löschen(Integer id) {
    teamRepository.deleteById(id);
  }
}
