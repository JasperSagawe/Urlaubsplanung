package com.npj.urlaubsplanung.model;

import java.sql.Timestamp;
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

	@OneToOne(mappedBy = "teamleiter")
	private Team team;

	@ManyToOne
	@JoinColumn(name = "user_role_id")
	private UserRole userRole;

	private Boolean aktiv = true;
	private Integer loginVersuche = 0;
	private Boolean firstLogin = true;
	private Timestamp lastLogin;

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

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	public Boolean getAktiv() {
		return aktiv;
	}

	public void setAktiv(Boolean aktiv) {
		this.aktiv = aktiv;
	}

	public Integer getLoginVersuche() {
		return loginVersuche;
	}

	public void setLoginVersuche(Integer loginVersuche) {
		this.loginVersuche = loginVersuche;
	}

	public Boolean getFirstLogin() {
		return firstLogin;
	}

	public void setFirstLogin(Boolean firstLogin) {
		this.firstLogin = firstLogin;
	}

	public Timestamp getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Timestamp lastLogin) {
		this.lastLogin = lastLogin;
	}

}
