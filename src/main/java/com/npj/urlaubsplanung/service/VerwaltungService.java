package com.npj.urlaubsplanung.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.npj.urlaubsplanung.dto.MitarbeiterDto;
import com.npj.urlaubsplanung.dto.TeamDto;
import com.npj.urlaubsplanung.model.Mitarbeiter;
import com.npj.urlaubsplanung.model.Mitarbeiterdaten;
import com.npj.urlaubsplanung.model.Team;
import com.npj.urlaubsplanung.model.UserRole;
import com.npj.urlaubsplanung.repository.MitarbeiterRepository;
import com.npj.urlaubsplanung.repository.TeamRepository;
import com.npj.urlaubsplanung.repository.UserRoleRepository;

@Service
public class VerwaltungService {

	private final MitarbeiterRepository mitarbeiterRepository;
	private final TeamRepository teamRepository;
	private final UserRoleRepository userRoleRepository;

	public VerwaltungService(MitarbeiterRepository mitarbeiterRepository, TeamRepository teamRepository,
			UserRoleRepository userRoleRepository) {
		this.mitarbeiterRepository = mitarbeiterRepository;
		this.teamRepository = teamRepository;
		this.userRoleRepository = userRoleRepository;
	}

	public Iterable<MitarbeiterDto> getMitarbeiter() {
		List<Mitarbeiter> mitarbeiterList = this.mitarbeiterRepository.findAll();

		return mitarbeiterList.stream().map(m -> new MitarbeiterDto(m.getId(), m.getVorname(), m.getNachname(),
				m.getEmail(),
				m.getMitarbeiterdaten().getTeam() != null ? new TeamDto(m.getMitarbeiterdaten().getTeam().getId(),
						m.getMitarbeiterdaten().getTeam().getName()) : null,
				m.getAktiv(), m.getFirstLogin(), m.getMitarbeiterdaten().getUrlaubstageProJahr(),
				m.getMitarbeiterdaten().getVerfuegbareUrlaubstage())).sorted(Comparator.comparing(MitarbeiterDto::getId)).toList();
	}

	public void saveMitarbeiter(MitarbeiterDto mitarbeiterDto) {

		UserRole userRole = userRoleRepository.findById(1).orElse(null);

		Mitarbeiter mitarbeiter = new Mitarbeiter(mitarbeiterDto.getVorname(), mitarbeiterDto.getNachname(),
				mitarbeiterDto.getEmail(), userRole);
		mitarbeiter.setEmail(mitarbeiterDto.getVorname() + "@firma.de");
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		mitarbeiter.setPasswortHash(passwordEncoder.encode(mitarbeiterDto.getPasswort()));
		mitarbeiter.setAktiv(mitarbeiterDto.getAktiv());
		mitarbeiter.setFirstLogin(true);

		Mitarbeiterdaten daten = new Mitarbeiterdaten(mitarbeiter, mitarbeiterDto.getUrlaubstageProJahr(),
				mitarbeiterDto.getVerfuegbareUrlaubstage(), LocalDate.now().getYear(), 0);


		Team team = teamRepository.findById(mitarbeiterDto.getTeam().getId()).orElse(null); 
		daten.setTeam(team);

		mitarbeiter.setMitarbeiterdaten(daten);

		this.mitarbeiterRepository.save(mitarbeiter);
	}

	@Transactional
	public void deleteMitarbeiterById(int id) {
		mitarbeiterRepository.findById(id).ifPresent(mitarbeiter -> {
			Team team = mitarbeiter.getTeam();
			if (team != null && team.getTeamleiter() != null && team.getTeamleiter().equals(mitarbeiter)) {
				team.setTeamleiter(null);
				teamRepository.save(team);
			}
			mitarbeiterRepository.delete(mitarbeiter);
		});
	}

	public Iterable<TeamDto> getTeams() {
		List<Team> teamList = this.teamRepository.findAll();
		return teamList.stream().map(t -> new TeamDto(t.getId(), t.getName())).sorted(Comparator.comparing(TeamDto::getId)).toList();
	}
}
