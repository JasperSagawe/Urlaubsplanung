package com.npj.urlaubsplanung.dto;

import java.util.List;

public class UrlaubsdatenDto {

	private String vorname;
	private String nachname;
	private int urlaubVorjahr;
	private int offeneUrlaubstage;
	private int beantragteUrlaubstage;
	private int genommeneUrlaubstage;
	private List<UrlaubstagDto> urlaubstage;

	public UrlaubsdatenDto(String vorname, String nachname, int urlaubVorjahr, int offeneUrlaubstage) {
		this.vorname = vorname;
		this.nachname = nachname;
		this.urlaubVorjahr = urlaubVorjahr;
		this.offeneUrlaubstage = offeneUrlaubstage;
		this.beantragteUrlaubstage = 0;
		this.genommeneUrlaubstage = 0;
	}

	public UrlaubsdatenDto(String vorname, String nachname, int urlaubVorjahr, int offeneUrlaubstage,
			int beantragteUrlaubstage, int genommeneUrlaubstage, List<UrlaubstagDto> urlaubstage) {
		this.vorname = vorname;
		this.nachname = nachname;
		this.urlaubVorjahr = urlaubVorjahr;
		this.offeneUrlaubstage = offeneUrlaubstage;
		this.beantragteUrlaubstage = beantragteUrlaubstage;
		this.genommeneUrlaubstage = genommeneUrlaubstage;
		this.urlaubstage = urlaubstage;
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

	public int getUrlaubVorjahr() {
		return urlaubVorjahr;
	}

	public void setUrlaubVorjahr(int urlaubVorjahr) {
		this.urlaubVorjahr = urlaubVorjahr;
	}

	public int getOffeneUrlaubstage() {
		return offeneUrlaubstage;
	}

	public void setOffeneUrlaubstage(int offeneUrlaubstage) {
		this.offeneUrlaubstage = offeneUrlaubstage;
	}

	public int getBeantragteUrlaubstage() {
		return beantragteUrlaubstage;
	}

	public void setBeantragteUrlaubstage(int beantragteUrlaubstage) {
		this.beantragteUrlaubstage = beantragteUrlaubstage;
	}

	public int getGenommeneUrlaubstage() {
		return genommeneUrlaubstage;
	}

	public void setGenommeneUrlaubstage(int genommeneUrlaubstage) {
		this.genommeneUrlaubstage = genommeneUrlaubstage;
	}

	public List<UrlaubstagDto> getUrlaubstage() {
		return urlaubstage;
	}

	public void setUrlaubstage(List<UrlaubstagDto> urlaubstage) {
		this.urlaubstage = urlaubstage;
	}
}
