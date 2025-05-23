
package com.npj.urlaubsplanung.model;

import jakarta.persistence.*;

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
}
