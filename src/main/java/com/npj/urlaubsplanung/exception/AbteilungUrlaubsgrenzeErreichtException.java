package com.npj.urlaubsplanung.exception;

public class AbteilungUrlaubsgrenzeErreichtException extends RuntimeException {
	private static final long serialVersionUID = 2L;

	public AbteilungUrlaubsgrenzeErreichtException() {
		super("Zu viele Abteilungsmitglieder sind im Urlaub.");
	}
}