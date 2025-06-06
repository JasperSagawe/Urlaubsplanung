package com.npj.urlaubsplanung.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Mitarbeiter {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String vorname;
	private String nachname;
	private String email;
	private String passwortHash;

	@OneToOne(mappedBy = "mitarbeiter", cascade = CascadeType.ALL, orphanRemoval = true)
	private Mitarbeiterdaten mitarbeiterdaten;

	@OneToMany(mappedBy = "mitarbeiter", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<Urlaubsantrag> urlaubsantrag = new ArrayList<>();

	@OneToOne(mappedBy = "abteilungsleiter")
	private Abteilung abteilung;

	@ManyToOne
	@JoinColumn(name = "user_role_id")
	private UserRole userRole;

	public Mitarbeiter() {
	}

	public Mitarbeiter(String vorname, String nachname, String email, UserRole userRole) {
		this.vorname = vorname;
		this.nachname = nachname;
		this.email = email;
		this.userRole = userRole;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getNachname() {
		return nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswortHash() {
		return passwortHash;
	}

	public void setPasswortHash(String passwortHash) {
		this.passwortHash = passwortHash;
	}

	public Mitarbeiterdaten getMitarbeiterdaten() {
		return mitarbeiterdaten;
	}

	public void setMitarbeiterdaten(Mitarbeiterdaten mitarbeiterdaten) {
		this.mitarbeiterdaten = mitarbeiterdaten;
	}

	public List<Urlaubsantrag> getUrlaubsantraege() {
		return urlaubsantrag;
	}

	public void setUrlaubsantraege(List<Urlaubsantrag> urlaubsantrag) {
		this.urlaubsantrag = urlaubsantrag;
	}

	public Abteilung getAbteilung() {
		return abteilung;
	}

	public void setAbteilung(Abteilung abteilung) {
		this.abteilung = abteilung;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

}
