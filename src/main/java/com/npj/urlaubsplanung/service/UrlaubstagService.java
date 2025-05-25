package com.npj.urlaubsplanung.service;

import com.npj.urlaubsplanung.model.Mitarbeiter;
import com.npj.urlaubsplanung.model.Urlaubsantrag;
import com.npj.urlaubsplanung.dto.UrlaubstagDto;
import com.npj.urlaubsplanung.dto.StatusDto;
import com.npj.urlaubsplanung.repository.UrlaubsantragRepository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UrlaubstagService {

	UrlaubsantragRepository urlaubsantragRepository;

	public UrlaubstagService(UrlaubsantragRepository urlaubsantragRepository) {
		this.urlaubsantragRepository = urlaubsantragRepository;
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
}
