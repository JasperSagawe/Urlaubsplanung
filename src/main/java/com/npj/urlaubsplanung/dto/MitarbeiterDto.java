package com.npj.urlaubsplanung.dto;

public class MitarbeiterDto {

	private int id;
	private String vorname;
	private String nachname;
	private String email;

	// Nur zum Erstellen des Nutzers
	private String passwort;
	private SelectDto team;
	private SelectDto rolle;
	private int urlaubstageProJahr;
	private int verfuegbareUrlaubstage;

	public MitarbeiterDto(int id, String vorname, String nachname, String email, SelectDto team, SelectDto rolle,
			int urlaubstageProJahr, int verfuegbareUrlaubstage) {
		this.id = id;
		this.vorname = vorname;
		this.nachname = nachname;
		this.email = email;
		this.team = team;
		this.rolle = rolle;
		this.urlaubstageProJahr = urlaubstageProJahr;
		this.verfuegbareUrlaubstage = verfuegbareUrlaubstage;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public String getPasswort() {
		return passwort;
	}

	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}

	public SelectDto getTeam() {
		return team;
	}

	public void setTeam(SelectDto team) {
		this.team = team;
	}

	public SelectDto getRolle() {
		return rolle;
	}

	public void setRolle(SelectDto rolle) {
		this.rolle = rolle;
	}

	public int getUrlaubstageProJahr() {
		return urlaubstageProJahr;
	}

	public void setUrlaubstageProJahr(int urlaubstageProJahr) {
		this.urlaubstageProJahr = urlaubstageProJahr;
	}

	public int getVerfuegbareUrlaubstage() {
		return verfuegbareUrlaubstage;
	}

	public void setVerfuegbareUrlaubstage(int verfuegbareUrlaubstage) {
		this.verfuegbareUrlaubstage = verfuegbareUrlaubstage;
	}

}