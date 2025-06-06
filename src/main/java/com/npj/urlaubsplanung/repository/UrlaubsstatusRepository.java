package com.npj.urlaubsplanung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.npj.urlaubsplanung.model.Urlaubsstatus;

@Repository
public interface UrlaubsstatusRepository extends JpaRepository<Urlaubsstatus, Integer> {

}
