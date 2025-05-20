package com.npj.urlaubsplanung.model;

import jakarta.persistence.*;
import java.time.*;

@Entity
public class Urlaubsantrag {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "mitarbeiter_id")
	private Mitarbeiter mitarbeiter;
	private LocalDate startDatum;
	private LocalDate endDatum;
	private Integer status;

	@ManyToOne
	@JoinColumn(name = "genehmigtVon_id")
	private Mitarbeiter genehmigtVon;
	private LocalDateTime antragsdatum;

	public Urlaubsantrag() {
	}

	public Urlaubsantrag(Mitarbeiter mitarbeiter, LocalDate startDatum, LocalDate endDatum, Integer status,
			Mitarbeiter genehmigtVon, LocalDateTime antragsdatum) {
		this.mitarbeiter = mitarbeiter;
		this.startDatum = startDatum;
		this.endDatum = endDatum;
		this.status = status;
		this.genehmigtVon = genehmigtVon;
		this.antragsdatum = antragsdatum;
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

	public LocalDate getStartDatum() {
		return startDatum;
	}

	public void setStartDatum(LocalDate startDatum) {
		this.startDatum = startDatum;
	}

	public LocalDate getEndDatum() {
		return endDatum;
	}

	public void setEndDatum(LocalDate endDatum) {
		this.endDatum = endDatum;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Mitarbeiter getGenehmigtVon() {
		return genehmigtVon;
	}

	public void setGenehmigtVon(Mitarbeiter genehmigtVon) {
		this.genehmigtVon = genehmigtVon;
	}

	public LocalDateTime getAntragsdatum() {
		return antragsdatum;
	}

	public void setAntragsdatum(LocalDateTime antragsdatum) {
		this.antragsdatum = antragsdatum;
	}

}