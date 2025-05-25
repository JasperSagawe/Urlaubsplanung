package com.npj.urlaubsplanung.dto;

public class MitarbeiterDto {

	private int id;
	private String vorname;
	private String nachname;
	private String email;

	// Nur zum Erstellen des Nutzers
	private String passwort;
	private TeamDto team;
	private Boolean aktiv;
	private Boolean passwortZuruecksetzen;
	private int urlaubstageProJahr;
	private int verfuegbareUrlaubstage;

	public MitarbeiterDto(int id, String vorname, String nachname, String email, TeamDto team, Boolean aktiv,
			Boolean passwortZuruecksetzen, int urlaubstageProJahr, int verfuegbareUrlaubstage) {
		this.id = id;
		this.vorname = vorname;
		this.nachname = nachname;
		this.email = email;
		this.team = team;
		this.aktiv = aktiv;
		this.passwortZuruecksetzen = passwortZuruecksetzen;
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

	public TeamDto getTeam() {
		return team;
	}

	public void setTeam(TeamDto team) {
		this.team = team;
	}

	public Boolean getAktiv() {
		return aktiv;
	}

	public void setAktiv(Boolean aktiv) {
		this.aktiv = aktiv;
	}

	public Boolean getPasswortZuruecksetzen() {
		return passwortZuruecksetzen;
	}

	public void setPasswortZuruecksetzen(Boolean passwortZuruecksetzen) {
		this.passwortZuruecksetzen = passwortZuruecksetzen;
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