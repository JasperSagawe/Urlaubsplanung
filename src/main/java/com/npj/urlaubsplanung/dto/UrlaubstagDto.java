package com.npj.urlaubsplanung.dto;

import java.time.LocalDate;

public class UrlaubstagDto {

	private Integer id;
	private String eventName;
	private LocalDate startDate;
	private LocalDate endDate;
	private StatusDto status;

	public UrlaubstagDto(Integer id, String eventName, LocalDate startDate, LocalDate endDate, StatusDto status) {
		this.id = id;
		this.eventName = eventName;
		this.startDate = startDate;
		this.endDate = endDate;
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
