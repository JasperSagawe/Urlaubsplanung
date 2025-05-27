package com.npj.urlaubsplanung.exception;

public class TeamUrlaubsgrenzeErreichtException extends RuntimeException {
	private static final long serialVersionUID = 2L;

	public TeamUrlaubsgrenzeErreichtException() {
        super("Zu viele Teammitglieder sind im Urlaub.");
    }
}