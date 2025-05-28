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
import com.npj.urlaubsplanung.model.Abteilung;
import com.npj.urlaubsplanung.model.Mitarbeiter;
import com.npj.urlaubsplanung.model.Mitarbeiterdaten;
import com.npj.urlaubsplanung.model.UserRole;
import com.npj.urlaubsplanung.repository.AbteilungRepository;
import com.npj.urlaubsplanung.repository.MitarbeiterRepository;
import com.npj.urlaubsplanung.repository.MitarbeiterdatenRepository;
import com.npj.urlaubsplanung.repository.UserRoleRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class VerwaltungService {

	private final MitarbeiterRepository mitarbeiterRepository;
	private final MitarbeiterdatenRepository mitarbeiterdatenRepository;
	private final AbteilungRepository abteilungRepository;
	private final UserRoleRepository userRoleRepository;

	public VerwaltungService(MitarbeiterRepository mitarbeiterRepository,
			MitarbeiterdatenRepository mitarbeiterdatenRepository, AbteilungRepository abteilungRepository,
			UserRoleRepository userRoleRepository) {
		this.mitarbeiterRepository = mitarbeiterRepository;
		this.mitarbeiterdatenRepository = mitarbeiterdatenRepository;
		this.abteilungRepository = abteilungRepository;
		this.userRoleRepository = userRoleRepository;
	}

	public Iterable<MitarbeiterDto> getMitarbeiter() {
		List<Mitarbeiter> mitarbeiterList = this.mitarbeiterRepository.findAll();

		return mitarbeiterList.stream().map(m -> new MitarbeiterDto(m.getId(), m.getVorname(), m.getNachname(), m
				.getEmail(),
				m.getMitarbeiterdaten().getAbteilung() != null
						? new SelectDto(m.getMitarbeiterdaten().getAbteilung().getId(),
								m.getMitarbeiterdaten().getAbteilung().getName())
						: null,
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

		Abteilung abteilung = abteilungRepository.findById(mitarbeiterDto.getAbteilung().getId()).orElse(null);
		daten.setAbteilung(abteilung);

		mitarbeiter.setMitarbeiterdaten(daten);

		this.mitarbeiterRepository.save(mitarbeiter);
	}

	@Transactional
	public void deleteMitarbeiterById(int id) {
		mitarbeiterRepository.findById(id).ifPresent(mitarbeiter -> {
			Abteilung abteilung = mitarbeiter.getAbteilung();
			if (abteilung != null && abteilung.getAbteilungsleiter() != null
					&& abteilung.getAbteilungsleiter().equals(mitarbeiter)) {
				abteilung.setAbteilungsleiter(null);
				abteilungRepository.save(abteilung);
			}
			mitarbeiterRepository.delete(mitarbeiter);
		});
	}

	public Iterable<SelectDto> getAbteilungSelect() {
		List<Abteilung> abteilungSelect = this.abteilungRepository.findAll();
		return abteilungSelect.stream().map(a -> new SelectDto(a.getId(), a.getName()))
				.sorted(Comparator.comparing(SelectDto::getId)).toList();
	}

	public Iterable<SelectDto> getRolleSelect() {
		List<UserRole> rolleSelect = this.userRoleRepository.findAll();
		return rolleSelect.stream().map(r -> new SelectDto(r.getId(), r.getRolleName()))
				.sorted(Comparator.comparing(SelectDto::getId)).toList();
	}

	public Iterable<AbteilungDto> getAbteilungen() {
		List<Abteilung> abteilungen = this.abteilungRepository.findAll();

		return abteilungen.stream()
				.map(a -> new AbteilungDto(a.getId(), a.getName(), a.getMaxUrlaubProzent(),
						mitarbeiterdatenRepository.countByAbteilungId(a.getId()),
						a.getAbteilungsleiter() != null
								? new SelectDto(a.getAbteilungsleiter().getId(), a.getAbteilungsleiter().getVorname())
								: null))
				.toList();
	}

	public void saveAbteilung(AbteilungDto abteilungDto) {

		Abteilung abteilung = new Abteilung(abteilungDto.getName(), abteilungDto.getMaxUrlaubProzent());
		Optional<Mitarbeiter> mitarbeiterOpt = mitarbeiterRepository
				.findById(abteilungDto.getAbteilungsleiter().getId());
		if (mitarbeiterOpt.isPresent()) {
			Mitarbeiter mitarbeiter = mitarbeiterOpt.get();
			abteilung.setAbteilungsleiter(mitarbeiter);
			// Rolle ID 1 = User, Rolle ID 2 = Abteilungsleiter
			Integer rolleId = mitarbeiter.getUserRole() != null ? mitarbeiter.getUserRole().getId() : 1;
			if (rolleId == 1) {
				UserRole userRole = new UserRole("Abteilungsleiter");
				userRole.setId(2);
				mitarbeiter.setUserRole(userRole);
			}
			mitarbeiter.getMitarbeiterdaten().setAbteilung(abteilung);
			abteilungRepository.save(abteilung);
			mitarbeiterRepository.save(mitarbeiter);
		}

	}

	@Transactional
	public void removeAbteilungMitarbeiterById(int id) {
		Abteilung abteilung = abteilungRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Abteilung nicht gefunden"));

		Mitarbeiter mitarbeiter = abteilung.getAbteilungsleiter();

		if (mitarbeiter != null) {

			// Rolle ID 1 = User, Rolle ID 2 = Abteilungsleiter
			if (mitarbeiter.getUserRole().getId() == 2) {
				UserRole userRole = new UserRole("User");
				userRole.setId(1);
				mitarbeiter.setUserRole(userRole);
			}

			abteilung.setAbteilungsleiter(null);
			mitarbeiterRepository.save(mitarbeiter);
		}

		for (Mitarbeiterdaten mitarbeiterdaten : abteilung.getMitarbeiterDatenListe()) {
			mitarbeiterdaten.setAbteilung(null);
			mitarbeiterdatenRepository.save(mitarbeiterdaten);
		}

		abteilungRepository.save(abteilung);
	};

	@Transactional
	public void deleteAbteilungById(int id) {
		Abteilung abteilung = abteilungRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Abteilung nicht gefunden"));

		abteilungRepository.delete(abteilung);
	};

	public Iterable<SelectDto> getMitarbeiterSelect() {
		List<Mitarbeiter> mitarbeiterList = this.mitarbeiterRepository.findByAbteilungIsNull();
		return mitarbeiterList.stream().map(m -> new SelectDto(m.getId(), m.getVorname()))
				.sorted(Comparator.comparing(SelectDto::getId)).toList();
	}
}
