package com.npj.urlaubsplanung.dto;

import java.time.LocalDate;

public class UrlaubstagDto {

	private String eventName;
	private LocalDate startDate;
	private LocalDate endDate;

	public UrlaubstagDto(String eventName, LocalDate startDate, LocalDate endDate) {
		this.eventName = eventName;
		this.startDate = startDate;
		this.endDate = endDate;
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
}
