package com.npj.urlaubsplanung.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.npj.urlaubsplanung.dto.AbteilungDto;
import com.npj.urlaubsplanung.dto.MitarbeiterDto;
import com.npj.urlaubsplanung.model.Mitarbeiter;
import com.npj.urlaubsplanung.model.Team;
import com.npj.urlaubsplanung.repository.MitarbeiterRepository;
import com.npj.urlaubsplanung.repository.MitarbeiterTeamRepository;
import com.npj.urlaubsplanung.repository.MitarbeiterdatenRepository;
import com.npj.urlaubsplanung.repository.TeamRepository;
import com.npj.urlaubsplanung.repository.UrlaubsantragRepository;

@Service
public class VerwaltungService {

	private final MitarbeiterRepository mitarbeiterRepository;
	private final MitarbeiterdatenRepository mitarbeiterdatenRepository;
	private final MitarbeiterTeamRepository mitarbeiterTeamRepository;
	private final UrlaubsantragRepository urlaubsantragRepository;
	private final UrlaubstagService urlaubstagService;
	private final TeamRepository teamRepository;

	public VerwaltungService(MitarbeiterRepository mitarbeiterRepository,
			MitarbeiterdatenRepository mitarbeiterdatenRepository, MitarbeiterTeamRepository mitarbeiterTeamRepository,
			UrlaubsantragRepository urlaubsantragRepository, UrlaubstagService urlaubstagService,
			TeamRepository teamRepository) {
		this.mitarbeiterRepository = mitarbeiterRepository;
		this.mitarbeiterdatenRepository = mitarbeiterdatenRepository;
		this.mitarbeiterTeamRepository = mitarbeiterTeamRepository;
		this.urlaubsantragRepository = urlaubsantragRepository;
		this.urlaubstagService = urlaubstagService;
		this.teamRepository = teamRepository;
	}

	public Iterable<AbteilungDto> getAbteilungen() {
		List<Team> abteilungen = this.teamRepository.findAll();

		return abteilungen.stream()
				.map(a -> new AbteilungDto(a.getId(), a.getName(), a.getMaxUrlaubProzent(), 5, "Fridolin")).toList();
	}

	public Iterable<MitarbeiterDto> getMitarbeiter() {
		List<Mitarbeiter> mitarbeiterList = this.mitarbeiterRepository.findAll();

		return mitarbeiterList.stream().map(m -> new MitarbeiterDto(m.getId(), m.getVorname(), m.getNachname(),
				m.getEmail(), "test", m.getAktiv(), m.getFirstLogin(), urlaubstagService.getUrlaubsdaten(m.getEmail())))
				.toList();
	}

	@Transactional
	public void deleteMitarbeiterById(int id) {
		Mitarbeiter mitarbeiter = mitarbeiterRepository.findById(id).get();

		mitarbeiterdatenRepository.deleteByMitarbeiter(mitarbeiter);
		mitarbeiterTeamRepository.deleteAllByMitarbeiter(mitarbeiter);
		urlaubsantragRepository.deleteAllByMitarbeiter(mitarbeiter);
		urlaubsantragRepository.deleteAllByGenehmigtVon(mitarbeiter);
		mitarbeiterRepository.delete(mitarbeiter);
	}
}
