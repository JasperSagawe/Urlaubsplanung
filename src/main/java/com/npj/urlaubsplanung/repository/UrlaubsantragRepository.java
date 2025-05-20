package com.npj.urlaubsplanung.repository;

import com.npj.urlaubsplanung.model.Urlaubsantrag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlaubsantragRepository extends JpaRepository<Urlaubsantrag, Integer> {

}
