package com.npj.urlaubsplanung.dto;

public class MitarbeiterDto {

	private int id;
	private String vorname;
	private String nachname;
	private String email;
	private String team;
	private Boolean aktiv;
	private Boolean passwortZuruecksetzen;
	private UrlaubsdatenDto urlaubsdaten;

	public MitarbeiterDto(int id, String vorname, String nachname, String email, String team, Boolean aktiv,
			Boolean passwortZuruecksetzen, UrlaubsdatenDto urlaubsdaten) {
		this.id = id;
		this.vorname = vorname;
		this.nachname = nachname;
		this.email = email;
		this.team = team;
		this.aktiv = aktiv;
		this.passwortZuruecksetzen = passwortZuruecksetzen;
		this.urlaubsdaten = urlaubsdaten;
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

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
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

	public UrlaubsdatenDto getUrlaubsdaten() {
		return urlaubsdaten;
	}

	public void setUrlaubsdaten(UrlaubsdatenDto urlaubsdaten) {
		this.urlaubsdaten = urlaubsdaten;
	}

}