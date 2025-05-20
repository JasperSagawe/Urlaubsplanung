package com.npj.urlaubsplanung.model;

import java.io.Serializable;
import java.util.Objects;

public class MitarbeiterTeamId implements Serializable {
	private static final long serialVersionUID = 2L;

	private Integer mitarbeiter;
	private Integer team;

	public MitarbeiterTeamId() {
	}

	public MitarbeiterTeamId(Integer mitarbeiter, Integer team) {
		this.mitarbeiter = mitarbeiter;
		this.team = team;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof MitarbeiterTeamId that))
			return false;
		return Objects.equals(mitarbeiter, that.mitarbeiter) && Objects.equals(team, that.team);
	}

	@Override
	public int hashCode() {
		return Objects.hash(mitarbeiter, team);
	}
}
