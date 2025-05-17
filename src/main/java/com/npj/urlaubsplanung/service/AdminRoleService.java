package com.npj.urlaubsplanung.service;

import com.npj.urlaubsplanung.models.AdminRole;
import com.npj.urlaubsplanung.repository.AdminRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminRoleService {

  @Autowired
  private AdminRoleRepository adminRoleRepository;

  public List<AdminRole> getAlle() {
    return adminRoleRepository.findAll();
  }

  public Optional<AdminRole> getById(Integer id) {
    return adminRoleRepository.findById(id);
  }

  public AdminRole speichern(AdminRole eintrag) {
    return adminRoleRepository.save(eintrag);
  }

  public void löschen(Integer id) {
    adminRoleRepository.deleteById(id);
  }
}
