package com.npj.urlaubsplanung.dto;

public enum StatusDto {
	BEANTRAGT("Beantragt"), GENEHMIGT("Genehmigt"), ABGELEHNT("Abgelehnt"), GENOMMEN("Genommen");

	private final String displayName;

	StatusDto(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
}