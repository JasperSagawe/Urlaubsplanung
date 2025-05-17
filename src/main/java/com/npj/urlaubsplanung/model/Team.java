package com.npj.urlaubsplanung.model;

import jakarta.persistence.*;

@Entity
public class Team {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private Integer maxUrlaubProzent;

	public Team() {
	}

	public Team(String name, Integer maxUrlaubProzent) {
		this.name = name;
		this.maxUrlaubProzent = maxUrlaubProzent;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getMaxUrlaubProzent() {
		return maxUrlaubProzent;
	}

	public void setMaxUrlaubProzent(Integer maxUrlaubProzent) {
		this.maxUrlaubProzent = maxUrlaubProzent;
	}

}