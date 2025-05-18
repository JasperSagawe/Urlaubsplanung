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

	public void deleteUrlaubstagById(Integer id) {
		urlaubsantragRepository.deleteById(id);
	}

	public List<UrlaubstagDto> getUrlaubstage() {
		List<Urlaubsantrag> urlaubsantraege = this.urlaubsantragRepository.findAll();

		return urlaubsantraege.stream().map(antrag -> {
			StatusDto statusText;
			switch (antrag.getStatus()) {
			case 0:
				statusText = StatusDto.BEANTRAGT;
				break;
			case 1:
				statusText = StatusDto.GENEHMIGT;
				break;
			case 2:
				statusText = StatusDto.ABGELEHNT;
				break;
			case 3:
				statusText = StatusDto.STORNIERT;
				break;
			default:
				statusText = StatusDto.BEANTRAGT;
			}
			Mitarbeiter mitarbeiter = antrag.getMitarbeiter();
			String mitarbeiterName = "Urlaub - " + mitarbeiter.getVorname() + " " + mitarbeiter.getNachname();
			return new UrlaubstagDto(antrag.getId(), mitarbeiterName, antrag.getStartDatum(), antrag.getEndDatum(),
					statusText);
		}).toList();
	}

	public UrlaubstagDto saveUrlaubsantrag(String email, UrlaubstagDto urlaubstagDto) {
		Optional<Mitarbeiter> mitarbeiterOpt = this.mitarbeiterRepository.findByEmail(email);

		if (mitarbeiterOpt.isPresent()) {
			Mitarbeiter mitarbeiter = mitarbeiterOpt.get();
			Urlaubsantrag urlaubsantrag = new Urlaubsantrag(mitarbeiter, urlaubstagDto.getStartDate(),
					urlaubstagDto.getEndDate(), 0, null, null);
			this.urlaubsantragRepository.save(urlaubsantrag);

			String mitarbeiterName = "Urlaub - " + mitarbeiter.getVorname() + " " + mitarbeiter.getNachname();

			return new UrlaubstagDto(urlaubstagDto.getId(), mitarbeiterName, urlaubstagDto.getStartDate(),
					urlaubstagDto.getEndDate(), null);
		}

		return null;
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
			StatusDto statusText;
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
			default:
				statusText = StatusDto.BEANTRAGT;
			}
			return new UrlaubstagDto(antrag.getId(), "Urlaub", antrag.getStartDatum(), antrag.getEndDatum(),
					statusText);
		}).toList();

		return new UrlaubsdatenDto(mitarbeiterdaten.getResturlaubVorjahr(), mitarbeiterdaten.getVerfuegbareUrlaubstage(), beantragt.get(),
				genommen.get(), urlaubstage);
	}
}
