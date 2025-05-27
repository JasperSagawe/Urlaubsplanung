package com.npj.urlaubsplanung.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Team {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;

	private Integer maxUrlaubProzent;

	@OneToMany(mappedBy = "team")
	private List<Mitarbeiterdaten> mitarbeiterDatenListe = new ArrayList<>();

	@OneToOne
	@JoinColumn(name = "teamleiter_id")
	private Mitarbeiter teamleiter;

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

	public List<Mitarbeiterdaten> getMitarbeiterDatenListe() {
		return mitarbeiterDatenListe;
	}

	public void setMitarbeiterListe(List<Mitarbeiterdaten> mitarbeiterDatenListe) {
		this.mitarbeiterDatenListe = mitarbeiterDatenListe;
	}

	public Mitarbeiter getTeamleiter() {
		return teamleiter;
	}

	public void setTeamleiter(Mitarbeiter teamleiter) {
		this.teamleiter = teamleiter;
	}

}
