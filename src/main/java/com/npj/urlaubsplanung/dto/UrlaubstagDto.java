package com.npj.urlaubsplanung.dto;

import java.time.LocalDate;

public class UrlaubstagDto {

	private Integer id;
	private String eventName;
	private LocalDate startdatum;
	private LocalDate enddatum;
	private StatusDto status;

	public UrlaubstagDto(Integer id, String eventName, LocalDate startdatum, LocalDate enddatum, StatusDto status) {
		this.id = id;
		this.eventName = eventName;
		this.startdatum = startdatum;
		this.enddatum = enddatum;
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public LocalDate getStartdatum() {
		return startdatum;
	}

	public void setStartdatum(LocalDate startdatum) {
		this.startdatum = startdatum;
	}

	public LocalDate getEnddatum() {
		return enddatum;
	}

	public void setEnddatum(LocalDate enddatum) {
		this.enddatum = enddatum;
	}

	public StatusDto getStatus() {
		return status;
	}

	public void setStatus(StatusDto status) {
		this.status = status;
	}
}
