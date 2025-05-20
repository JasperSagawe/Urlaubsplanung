package com.npj.urlaubsplanung.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class AdminRole {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String rolleName;

	public AdminRole() {
	}

	public AdminRole(String rolleName) {
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
