package com.npj.urlaubsplanung.service;

import com.npj.urlaubsplanung.dto.StatusDto;
import com.npj.urlaubsplanung.dto.UrlaubsdatenDto;
import com.npj.urlaubsplanung.dto.UrlaubstagDto;
import com.npj.urlaubsplanung.model.Mitarbeiter;
import com.npj.urlaubsplanung.model.Mitarbeiterdaten;
import com.npj.urlaubsplanung.model.Urlaubsantrag;
import com.npj.urlaubsplanung.repository.MitarbeiterRepository;
import com.npj.urlaubsplanung.repository.MitarbeiterdatenRepository;
import com.npj.urlaubsplanung.repository.UrlaubsantragRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

@Service
public class UrlaubstagService {

	private MitarbeiterRepository mitarbeiterRepository;
	private MitarbeiterdatenRepository mitarbeiterdatenRepository;
	private UrlaubsantragRepository urlaubsantragRepository;

	public UrlaubstagService(MitarbeiterRepository mitarbeiterRepository,
			MitarbeiterdatenRepository mitarbeiterdatenRepository, UrlaubsantragRepository urlaubsantragRepository) {
		this.mitarbeiterRepository = mitarbeiterRepository;
		this.mitarbeiterdatenRepository = mitarbeiterdatenRepository;
		this.urlaubsantragRepository = urlaubsantragRepository;
	}

	public Iterable<UrlaubstagDto> getUrlaubstag() {
		LocalDate aliceDate = LocalDate.now();
		LocalDate bobDate = LocalDate.now().plusDays(7);
		LocalDate charlieDate = LocalDate.now().minusDays(4);

		return List.of(new UrlaubstagDto("Urlaub - Alice", aliceDate, aliceDate.plusDays(2), StatusDto.BEANTRAGT),
				new UrlaubstagDto("Urlaub - Bob", bobDate, bobDate.plusDays(3), StatusDto.BEANTRAGT),
				new UrlaubstagDto("Urlaub - Charlie", charlieDate, charlieDate, StatusDto.BEANTRAGT));
	}

	public UrlaubsdatenDto getUrlaubsdaten(String email) {
		Optional<Mitarbeiter> mitarbeiterOpt = this.mitarbeiterRepository.findByEmail(email);

		if (mitarbeiterOpt.isEmpty()) {
			return null;
		}

		AtomicInteger beantragt = new AtomicInteger(0);
		AtomicInteger genommen = new AtomicInteger(0);

		Mitarbeiter mitarbeiter = mitarbeiterOpt.get();

		Mitarbeiterdaten mitarbeiterdaten = this.mitarbeiterdatenRepository.findByMitarbeiter(mitarbeiter);

		List<Urlaubsantrag> urlaubsantraege = this.urlaubsantragRepository.findByMitarbeiter(mitarbeiter);

		List<UrlaubstagDto> urlaubstage = urlaubsantraege.stream().map(antrag -> {
			StatusDto statusText = StatusDto.BEANTRAGT;
			switch (antrag.getStatus()) {
			case 0:
				statusText = StatusDto.BEANTRAGT;
				beantragt.getAndIncrement();
				break;
			case 1:
				statusText = StatusDto.GENEHMIGT;
				genommen.getAndIncrement();
				break;
			case 2:
				statusText = StatusDto.ABGELEHNT;
				break;
			case 3:
				statusText = StatusDto.STORNIERT;
				break;
			}
			return new UrlaubstagDto("Urlaub", antrag.getStartDatum(), antrag.getEndDatum(), statusText);
		}).toList();

		return new UrlaubsdatenDto(mitarbeiter.getVorname(), mitarbeiter.getNachname(),
				mitarbeiterdaten.getResturlaubVorjahr(), mitarbeiterdaten.getVerfuegbareUrlaubstage(), beantragt.get(),
				genommen.get(), urlaubstage);
	}
}
