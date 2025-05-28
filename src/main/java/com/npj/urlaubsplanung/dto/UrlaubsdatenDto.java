package com.npj.urlaubsplanung.dto;

import java.util.List;

public class UrlaubsdatenDto {

	private int urlaubVorjahr;
	private int offeneUrlaubstage;
	private int geplanteUrlaubstage;
	private int genommeneUrlaubstage;
	private List<UrlaubstagDto> urlaubstage;

	public UrlaubsdatenDto(int urlaubVorjahr, int offeneUrlaubstage, int geplanteUrlaubstage,
			int genommeneUrlaubstage, List<UrlaubstagDto> urlaubstage) {
		this.urlaubVorjahr = urlaubVorjahr;
		this.offeneUrlaubstage = offeneUrlaubstage;
		this.geplanteUrlaubstage = geplanteUrlaubstage;
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

	public int getGeplanteUrlaubstage() {
		return geplanteUrlaubstage;
	}

	public void setGeplanteUrlaubstage(int geplanteUrlaubstage) {
		this.geplanteUrlaubstage = geplanteUrlaubstage;
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
