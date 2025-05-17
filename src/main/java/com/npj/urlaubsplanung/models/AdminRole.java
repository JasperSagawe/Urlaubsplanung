package com.npj.urlaubsplanung.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class AdminRole {

    @Id
    private Integer id;
    private String rolleName;

    public AdminRole() {}

    public AdminRole(Integer id, String rolleName) {
        this.id = id;
        this.rolleName = rolleName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRolleName() {
        return rolleName;
    }

    public void setRolleName(String rolleName) {
        this.rolleName = rolleName;
    }
}
