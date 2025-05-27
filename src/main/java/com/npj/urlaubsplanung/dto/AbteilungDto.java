package com.npj.urlaubsplanung.dto;

public class AbteilungDto {

    private int id;
    private String name;
    private int maxUrlaubProzent;
    private int mitarbeiterAnzahl;
    private SelectDto abteilungsleiter;

    public AbteilungDto(int id, String name, int maxUrlaubProzent, int mitarbeiterAnzahl,
            SelectDto abteilungsleiter) {
        this.id = id;
        this.name = name;
        this.maxUrlaubProzent = maxUrlaubProzent;
        this.mitarbeiterAnzahl = mitarbeiterAnzahl;
        this.abteilungsleiter = abteilungsleiter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxUrlaubProzent() {
        return maxUrlaubProzent;
    }

    public void setMaxUrlaubProzent(int maxUrlaubProzent) {
        this.maxUrlaubProzent = maxUrlaubProzent;
    }

    public int getMitarbeiterAnzahl() {
        return mitarbeiterAnzahl;
    }

    public void setMitarbeiterAnzahl(int mitarbeiterAnzahl) {
        this.mitarbeiterAnzahl = mitarbeiterAnzahl;
    }

    public SelectDto getAbteilungsleiter() {
        return abteilungsleiter;
    }

    public void setAbteilungsleiter(SelectDto abteilungsleiter) {
        this.abteilungsleiter = abteilungsleiter;
    }

}
