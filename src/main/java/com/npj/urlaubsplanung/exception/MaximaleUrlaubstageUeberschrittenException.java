package com.npj.urlaubsplanung.exception;

public class MaximaleUrlaubstageUeberschrittenException extends RuntimeException {
	private static final long serialVersionUID = 3L;
	private final int verbleibendeTage;

    public MaximaleUrlaubstageUeberschrittenException(int verbleibendeTage) {
        super("Nicht genug Urlaubstage verfügbar.");
        this.verbleibendeTage = verbleibendeTage;
    }

    public int getVerbleibendeTage() {
        return verbleibendeTage;
    }
}