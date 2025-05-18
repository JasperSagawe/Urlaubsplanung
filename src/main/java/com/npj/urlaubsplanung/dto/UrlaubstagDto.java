package com.npj.urlaubsplanung.dto;

import java.time.LocalDate;

public class UrlaubstagDto {

	private String eventName;
	private LocalDate startDate;
	private LocalDate endDate;
	private StatusDto status;

	public UrlaubstagDto(String eventName, LocalDate startDate, LocalDate endDate, StatusDto status) {
		this.eventName = eventName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public StatusDto getStatus() {
		return status;
	}

	public void setStatus(StatusDto status) {
		this.status = status;
	}
}
