package com.npj.urlaubsplanung.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Mitarbeiterdaten {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@OneToOne
	@JoinColumn(name = "mitarbeiter_id")
	private Mitarbeiter mitarbeiter;

	private Integer verfuegbareUrlaubstage;
	private Integer urlaubstageProJahr;
	private Integer aktuellesJahr;
	private Integer resturlaubVorjahr;

	@ManyToOne
	@JoinColumn(name = "abteilung_id")
	private Abteilung abteilung;

	public Mitarbeiterdaten() {
	}

	public Mitarbeiterdaten(Mitarbeiter mitarbeiter, Integer verfuegbareUrlaubstage, Integer urlaubstageProJahr,
			Integer aktuellesJahr, Integer resturlaubVorjahr) {
		this.mitarbeiter = mitarbeiter;
		this.verfuegbareUrlaubstage = verfuegbareUrlaubstage;
		this.urlaubstageProJahr = urlaubstageProJahr;
		this.aktuellesJahr = aktuellesJahr;
		this.resturlaubVorjahr = resturlaubVorjahr;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Mitarbeiter getMitarbeiter() {
		return mitarbeiter;
	}

	public void setMitarbeiter(Mitarbeiter mitarbeiter) {
		this.mitarbeiter = mitarbeiter;
	}

	public Integer getVerfuegbareUrlaubstage() {
		return verfuegbareUrlaubstage;
	}

	public void setVerfuegbareUrlaubstage(Integer verfuegbareUrlaubstage) {
		this.verfuegbareUrlaubstage = verfuegbareUrlaubstage;
	}

	public Integer getUrlaubstageProJahr() {
		return urlaubstageProJahr;
	}

	public void setUrlaubstageProJahr(Integer urlaubstageProJahr) {
		this.urlaubstageProJahr = urlaubstageProJahr;
	}

	public Integer getAktuellesJahr() {
		return aktuellesJahr;
	}

	public void setAktuellesJahr(Integer aktuellesJahr) {
		this.aktuellesJahr = aktuellesJahr;
	}

	public Integer getResturlaubVorjahr() {
		return resturlaubVorjahr;
	}

	public void setResturlaubVorjahr(Integer resturlaubVorjahr) {
		this.resturlaubVorjahr = resturlaubVorjahr;
	}

	public Abteilung getAbteilung() {
		return abteilung;
	}

	public void setAbteilung(Abteilung abteilung) {
		this.abteilung = abteilung;
	}

}
