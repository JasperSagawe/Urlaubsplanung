package com.npj.urlaubsplanung.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.npj.urlaubsplanung.dto.AbteilungDto;
import com.npj.urlaubsplanung.dto.MitarbeiterDto;
import com.npj.urlaubsplanung.dto.SelectDto;
import com.npj.urlaubsplanung.model.Mitarbeiter;
import com.npj.urlaubsplanung.model.Mitarbeiterdaten;
import com.npj.urlaubsplanung.model.Team;
import com.npj.urlaubsplanung.model.UserRole;
import com.npj.urlaubsplanung.repository.MitarbeiterRepository;
import com.npj.urlaubsplanung.repository.MitarbeiterdatenRepository;
import com.npj.urlaubsplanung.repository.TeamRepository;
import com.npj.urlaubsplanung.repository.UserRoleRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class VerwaltungService {

	private final MitarbeiterRepository mitarbeiterRepository;
	private final MitarbeiterdatenRepository mitarbeiterdatenRepository;
	private final TeamRepository teamRepository;
	private final UserRoleRepository userRoleRepository;

	public VerwaltungService(MitarbeiterRepository mitarbeiterRepository,
			MitarbeiterdatenRepository mitarbeiterdatenRepository, TeamRepository teamRepository,
			UserRoleRepository userRoleRepository) {
		this.mitarbeiterRepository = mitarbeiterRepository;
		this.mitarbeiterdatenRepository = mitarbeiterdatenRepository;
		this.teamRepository = teamRepository;
		this.userRoleRepository = userRoleRepository;
	}

	public Iterable<MitarbeiterDto> getMitarbeiter() {
		List<Mitarbeiter> mitarbeiterList = this.mitarbeiterRepository.findAll();

		return mitarbeiterList.stream().map(m -> new MitarbeiterDto(m.getId(), m.getVorname(), m.getNachname(),
				m.getEmail(),
				m.getMitarbeiterdaten().getTeam() != null ? new SelectDto(m.getMitarbeiterdaten().getTeam().getId(),
						m.getMitarbeiterdaten().getTeam().getName()) : null,
				m.getUserRole() != null ? new SelectDto(m.getUserRole().getId(), m.getUserRole().getRolleName()) : null,
				m.getMitarbeiterdaten().getUrlaubstageProJahr(), m.getMitarbeiterdaten().getVerfuegbareUrlaubstage()))
				.sorted(Comparator.comparing(MitarbeiterDto::getId)).toList();
	}

	public void saveMitarbeiter(MitarbeiterDto mitarbeiterDto) {

		UserRole userRole = userRoleRepository.findById(mitarbeiterDto.getRolle().getId()).orElse(null);

		Mitarbeiter mitarbeiter = new Mitarbeiter(mitarbeiterDto.getVorname(), mitarbeiterDto.getNachname(),
				mitarbeiterDto.getEmail(), userRole);
		mitarbeiter.setEmail(mitarbeiterDto.getVorname() + "@firma.de");
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		mitarbeiter.setPasswortHash(passwordEncoder.encode(mitarbeiterDto.getPasswort()));

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

	public Iterable<SelectDto> getTeamSelect() {
		List<Team> teamSelect = this.teamRepository.findAll();
		return teamSelect.stream().map(t -> new SelectDto(t.getId(), t.getName()))
				.sorted(Comparator.comparing(SelectDto::getId)).toList();
	}

	public Iterable<SelectDto> getRolleSelect() {
		List<UserRole> rolleSelect = this.userRoleRepository.findAll();
		return rolleSelect.stream().map(u -> new SelectDto(u.getId(), u.getRolleName()))
				.sorted(Comparator.comparing(SelectDto::getId)).toList();
	}

	public Iterable<AbteilungDto> getAbteilungen() {
		List<Team> abteilungen = this.teamRepository.findAll();

		return abteilungen.stream()
				.map(a -> new AbteilungDto(a.getId(), a.getName(), a.getMaxUrlaubProzent(),
						mitarbeiterdatenRepository.countByTeamId(a.getId()),
						a.getTeamleiter() != null
								? new SelectDto(a.getTeamleiter().getId(), a.getTeamleiter().getVorname())
								: null))
				.toList();
	}

	public void saveAbteilung(AbteilungDto abteilungDto) {

		Team abteilung = new Team(abteilungDto.getName(), abteilungDto.getMaxUrlaubProzent());
		Optional<Mitarbeiter> mitarbeiterOpt = mitarbeiterRepository
				.findById(abteilungDto.getAbteilungsleiter().getId());
		if (mitarbeiterOpt.isPresent()) {
			Mitarbeiter mitarbeiter = mitarbeiterOpt.get();
			abteilung.setTeamleiter(mitarbeiter);
			Integer rolleName = mitarbeiter.getUserRole() != null ? mitarbeiter.getUserRole().getId() : 1;
			if (rolleName == 1) {
				UserRole userRole = new UserRole("Team-Admin");
				userRole.setId(2);
				mitarbeiter.setUserRole(userRole);
			}
			mitarbeiter.getMitarbeiterdaten().setTeam(abteilung);
			teamRepository.save(abteilung);
			mitarbeiterRepository.save(mitarbeiter);
		}

	}

	@Transactional
	public void removeAbteilungMitarbeiterById(int id) {
		Team abteilung = teamRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Team nicht gefunden"));

		Mitarbeiter mitarbeiter = abteilung.getTeamleiter();

		if (mitarbeiter != null) {
			if (mitarbeiter.getUserRole().getId() == 2) {
				UserRole userRole = new UserRole("Kein Admin");
				userRole.setId(1);
				mitarbeiter.setUserRole(userRole);
			}

			abteilung.setTeamleiter(null);
			mitarbeiterRepository.save(mitarbeiter);
		}

		for (Mitarbeiterdaten mitarbeiterdaten : abteilung.getMitarbeiterDatenListe()) {
			mitarbeiterdaten.setTeam(null);
			mitarbeiterdatenRepository.save(mitarbeiterdaten);
		}

		teamRepository.save(abteilung);
	};

	@Transactional
	public void deleteAbteilungById(int id) {
		Team abteilung = teamRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Team nicht gefunden"));

		teamRepository.delete(abteilung);
	};

	public Iterable<SelectDto> getMitarbeiterSelect() {
		List<Mitarbeiter> mitarbeiterList = this.mitarbeiterRepository.findByTeamIsNull();
		return mitarbeiterList.stream().map(m -> new SelectDto(m.getId(), m.getVorname()))
				.sorted(Comparator.comparing(SelectDto::getId)).toList();
	}
}
