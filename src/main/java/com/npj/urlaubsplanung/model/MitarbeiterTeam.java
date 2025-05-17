package com.npj.urlaubsplanung.model;

import jakarta.persistence.*;

@Entity
@IdClass(MitarbeiterTeamId.class)
public class MitarbeiterTeam {

	@Id
	@ManyToOne
	@JoinColumn(name = "mitarbeiter_id", referencedColumnName = "id")
	private Mitarbeiter mitarbeiter;

	@Id
	@ManyToOne
	@JoinColumn(name = "team_id", referencedColumnName = "id")
	private Team team;

	public MitarbeiterTeam() {
	}

	public MitarbeiterTeam(Mitarbeiter mitarbeiter, Team team) {
		this.mitarbeiter = mitarbeiter;
		this.team = team;
	}

	public Mitarbeiter getMitarbeiter() {
		return mitarbeiter;
	}

	public void setMitarbeiter(Mitarbeiter mitarbeiter) {
		this.mitarbeiter = mitarbeiter;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}
}
