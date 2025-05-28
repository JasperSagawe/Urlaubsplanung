package com.npj.urlaubsplanung.dto;

import java.util.List;

public class UrlaubsdatenDto {

	private int urlaubVorjahr;
	private int offeneUrlaubstage;
	private int beantragteUrlaubstage;
	private int genommeneUrlaubstage;
	private List<UrlaubstagDto> urlaubstage;

	public UrlaubsdatenDto(int urlaubVorjahr, int offeneUrlaubstage, int beantragteUrlaubstage,
			int genommeneUrlaubstage, List<UrlaubstagDto> urlaubstage) {
		this.urlaubVorjahr = urlaubVorjahr;
		this.offeneUrlaubstage = offeneUrlaubstage;
		this.beantragteUrlaubstage = beantragteUrlaubstage;
		this.genommeneUrlaubstage = genommeneUrlaubstage;
		this.urlaubstage = urlaubstage;
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
