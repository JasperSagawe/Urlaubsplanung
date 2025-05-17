package com.npj.urlaubsplanung.repository;

import com.npj.urlaubsplanung.model.AdminRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRoleRepository extends JpaRepository<AdminRole, Integer> {
}
