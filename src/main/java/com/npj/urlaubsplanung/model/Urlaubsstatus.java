package com.npj.urlaubsplanung.model;

import jakarta.persistence.*;

@Entity
public class Urlaubsstatus {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String statusName;

	public Urlaubsstatus() {
	}

	public Urlaubsstatus(String statusName) {
		this.statusName = statusName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

}