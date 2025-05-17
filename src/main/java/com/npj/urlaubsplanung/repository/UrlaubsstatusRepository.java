package com.npj.urlaubsplanung.repository;

import com.npj.urlaubsplanung.model.Urlaubsstatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlaubsstatusRepository extends JpaRepository<Urlaubsstatus, Integer> {

}
