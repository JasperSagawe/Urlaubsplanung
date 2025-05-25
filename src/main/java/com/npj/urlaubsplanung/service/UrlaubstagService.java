package com.npj.urlaubsplanung.service;

import static de.focus_shift.jollyday.core.HolidayCalendar.GERMANY;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Year;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import com.npj.urlaubsplanung.dto.StatusDto;
import com.npj.urlaubsplanung.dto.UrlaubsdatenDto;
import com.npj.urlaubsplanung.dto.UrlaubstagDto;
import com.npj.urlaubsplanung.exception.MaximaleUrlaubstageUeberschrittenException;
import com.npj.urlaubsplanung.exception.TeamUrlaubsgrenzeErreichtException;
import com.npj.urlaubsplanung.model.Mitarbeiter;
import com.npj.urlaubsplanung.model.Mitarbeiterdaten;
import com.npj.urlaubsplanung.model.Team;
import com.npj.urlaubsplanung.model.Urlaubsantrag;
import com.npj.urlaubsplanung.repository.MitarbeiterRepository;
import com.npj.urlaubsplanung.repository.MitarbeiterdatenRepository;
import com.npj.urlaubsplanung.repository.UrlaubsantragRepository;

import de.focus_shift.jollyday.core.Holiday;
import de.focus_shift.jollyday.core.HolidayManager;
import de.focus_shift.jollyday.core.ManagerParameters;

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
		Optional<Urlaubsantrag> urlaubsantragOpt = urlaubsantragRepository.findById(id);
		if (urlaubsantragOpt.isPresent()) {
			Urlaubsantrag urlaubsantrag = urlaubsantragOpt.get();
			int status = urlaubsantrag.getStatus();
			// Nur wenn Status BEANTRAGT (0) oder GENEHMIGT (1)
			if (status == 0 || status == 1) {
				Mitarbeiter mitarbeiter = urlaubsantrag.getMitarbeiter();
				Mitarbeiterdaten mitarbeiterdaten = mitarbeiterdatenRepository.findByMitarbeiter(mitarbeiter);
				if (mitarbeiterdaten != null) {
					long tage = berechneTage(urlaubsantrag.getStartDatum(), urlaubsantrag.getEndDatum());
					mitarbeiterdaten
							.setVerfuegbareUrlaubstage(mitarbeiterdaten.getVerfuegbareUrlaubstage() + (int) tage);
					mitarbeiterdatenRepository.save(mitarbeiterdaten);
				}
			}
			urlaubsantragRepository.deleteById(id);
		}
	}

	public List<UrlaubstagDto> getUrlaubstage() {
		return mapUrlaubsantraegeToDto(this.urlaubsantragRepository.findAll());
	}

	public List<UrlaubstagDto> getUrlaubstageByTeam(int id) {
		return mapUrlaubsantraegeToDto(this.urlaubsantragRepository.findAll().stream().filter(antrag -> {
			Mitarbeiter mitarbeiter = antrag.getMitarbeiter();
			Team team = mitarbeiter.getMitarbeiterdaten().getTeam();
			return team != null && team.getId() == id;
		}).toList());
	}

	private List<UrlaubstagDto> mapUrlaubsantraegeToDto(List<Urlaubsantrag> urlaubsantraege) {
		LocalDate heute = LocalDate.now();
		return urlaubsantraege.stream().map(antrag -> {
			StatusDto status;
			if (antrag.getStatus() == 1 && antrag.getStartDatum().isBefore(heute)) {
				status = StatusDto.GENOMMEN;
			} else {
				status = switch (antrag.getStatus()) {
				case 0 -> StatusDto.BEANTRAGT;
				case 1 -> StatusDto.GENEHMIGT;
				case 2 -> StatusDto.ABGELEHNT;
				default -> StatusDto.BEANTRAGT;
				};
			}
			Mitarbeiter m = antrag.getMitarbeiter();
			String mitarbeiterName = "Urlaub - " + m.getVorname() + " " + m.getNachname();
			return new UrlaubstagDto(antrag.getId(), mitarbeiterName, antrag.getStartDatum(), antrag.getEndDatum(),
					status);
		}).sorted(Comparator.comparing(UrlaubstagDto::getStartDate)).toList();
	}

	public UrlaubstagDto saveUrlaubsantrag(String email, UrlaubstagDto urlaubstagDto) {
		Optional<Mitarbeiter> mitarbeiterOpt = this.mitarbeiterRepository.findByEmail(email);

		if (mitarbeiterOpt.isPresent()) {
			Mitarbeiter mitarbeiter = mitarbeiterOpt.get();

			long tage = berechneTage(urlaubstagDto.getStartDate(), urlaubstagDto.getEndDate());

			Mitarbeiterdaten mitarbeiterdaten = this.mitarbeiterdatenRepository.findByMitarbeiter(mitarbeiter);
			if (mitarbeiterdaten == null) {
				return null;
			}

			int verfuegbareUrlaubstage = mitarbeiterdaten.getVerfuegbareUrlaubstage();
			if (tage > verfuegbareUrlaubstage) {
				throw new MaximaleUrlaubstageUeberschrittenException(verfuegbareUrlaubstage);
			}

			Urlaubsantrag urlaubsantrag = new Urlaubsantrag(mitarbeiter, urlaubstagDto.getStartDate(),
					urlaubstagDto.getEndDate(), 0, null);

			Team team = mitarbeiter.getMitarbeiterdaten().getTeam();
			int maxTeamUrlauber = team.getMaxUrlaubProzent();

			List<LocalDate> urlaubstage = urlaubstagDto.getStartDate()
					.datesUntil(urlaubstagDto.getEndDate().plusDays(1)).toList();

			int teamMitglieder = mitarbeiterdatenRepository.countByTeamId(team.getId());
			int maxUrlauber = (int) Math.ceil(teamMitglieder * (maxTeamUrlauber / 100.0));

			for (LocalDate tag : urlaubstage) {
				long bereitsImUrlaub = urlaubsantragRepository.findAll().stream().filter(a -> {
					Mitarbeiter m = a.getMitarbeiter();
					Team t = m.getMitarbeiterdaten().getTeam();
					return t != null && t.getId() == team.getId() && !a.getStartDatum().isAfter(tag)
							&& !a.getEndDatum().isBefore(tag) && (a.getStatus() == 0 || a.getStatus() == 1);
				}).count();
				if (bereitsImUrlaub >= maxUrlauber) {
					throw new TeamUrlaubsgrenzeErreichtException();
				}
			}			
			
			int resturlaubVorjahr = mitarbeiterdaten.getResturlaubVorjahr();
			if (resturlaubVorjahr > 0) {
				if (resturlaubVorjahr >= (int) tage) {
					mitarbeiterdaten.setResturlaubVorjahr(resturlaubVorjahr - (int) tage);
				} else {
					mitarbeiterdaten.setResturlaubVorjahr(0);
				}
			}

			mitarbeiterdaten.setVerfuegbareUrlaubstage(verfuegbareUrlaubstage - (int) tage);
			this.mitarbeiterdatenRepository.save(mitarbeiterdaten);
			this.urlaubsantragRepository.save(urlaubsantrag);

			String mitarbeiterName = "Urlaub - " + mitarbeiter.getVorname() + " " + mitarbeiter.getNachname();

			return new UrlaubstagDto(urlaubstagDto.getId(), mitarbeiterName, urlaubstagDto.getStartDate(),
					urlaubstagDto.getEndDate(), StatusDto.BEANTRAGT);
		}

		return null;
	}

	public UrlaubsdatenDto getUrlaubsdaten(String email) {
		Optional<Mitarbeiter> mitarbeiterOpt = mitarbeiterRepository.findByEmail(email);
		if (mitarbeiterOpt.isEmpty())
			return null;

		Mitarbeiter mitarbeiter = mitarbeiterOpt.get();
		Mitarbeiterdaten mitarbeiterdaten = aktualisiereMitarbeiterdaten(
				mitarbeiterdatenRepository.findByMitarbeiter(mitarbeiter));
		List<Urlaubsantrag> urlaubsantraege = urlaubsantragRepository.findByMitarbeiter(mitarbeiter);

		AtomicInteger beantragt = new AtomicInteger();
		AtomicInteger genommen = new AtomicInteger();
		LocalDate heute = LocalDate.now();

		List<UrlaubstagDto> urlaubstage = urlaubsantraege.stream().map(antrag -> {
			long tage = berechneTage(antrag.getStartDatum(), antrag.getEndDatum());
			StatusDto status;
			if (antrag.getStatus() == 1 && antrag.getStartDatum().isBefore(heute)) {
				status = StatusDto.GENOMMEN;
				genommen.addAndGet((int) tage);
			} else {
				status = switch (antrag.getStatus()) {
				case 0 -> StatusDto.BEANTRAGT;
				case 1 -> StatusDto.GENEHMIGT;
				case 2 -> StatusDto.ABGELEHNT;
				default -> StatusDto.BEANTRAGT;
				};
				if (antrag.getStatus() == 0 || antrag.getStatus() == 1)
					beantragt.addAndGet((int) tage);
			}
			return new UrlaubstagDto(antrag.getId(), "Urlaub", antrag.getStartDatum(), antrag.getEndDatum(), status);
		}).sorted(Comparator.comparing(UrlaubstagDto::getStartDate)).toList();

		return new UrlaubsdatenDto(mitarbeiterdaten.getResturlaubVorjahr(),
				mitarbeiterdaten.getVerfuegbareUrlaubstage(), beantragt.get(), genommen.get(), urlaubstage);
	}

	public Mitarbeiterdaten aktualisiereMitarbeiterdaten(Mitarbeiterdaten daten) {
		int aktuellesJahr = LocalDate.now().getYear();
		boolean aktualisiert = false;
		while (daten.getAktuellesJahr() < aktuellesJahr) {
			int rest = daten.getVerfuegbareUrlaubstage();
			daten.setResturlaubVorjahr(rest);
			daten.setVerfuegbareUrlaubstage(daten.getUrlaubstageProJahr() + rest);
			daten.setAktuellesJahr(daten.getAktuellesJahr() + 1);
			aktualisiert = true;
		}

		if (aktualisiert) {
			mitarbeiterdatenRepository.save(daten);
		}

		return daten;
	}

	public void genehmigeUrlaubstag(Long id) {
		Optional<Urlaubsantrag> urlaubsantragOpt = urlaubsantragRepository.findById(id.intValue());
		if (urlaubsantragOpt.isPresent()) {
			Urlaubsantrag urlaubsantrag = urlaubsantragOpt.get();
			urlaubsantrag.setStatus(1);
			urlaubsantragRepository.save(urlaubsantrag);
		}
	}

	public void lehneUrlaubstagAb(Long id) {
		Optional<Urlaubsantrag> urlaubsantragOpt = urlaubsantragRepository.findById(id.intValue());
		if (urlaubsantragOpt.isPresent()) {
			Urlaubsantrag urlaubsantrag = urlaubsantragOpt.get();
			urlaubsantrag.setStatus(2);
			urlaubsantragRepository.save(urlaubsantrag);

			Mitarbeiter mitarbeiter = urlaubsantrag.getMitarbeiter();
			Mitarbeiterdaten mitarbeiterdaten = mitarbeiterdatenRepository.findByMitarbeiter(mitarbeiter);
			if (mitarbeiterdaten != null) {
				long tage = berechneTage(urlaubsantrag.getStartDatum(), urlaubsantrag.getEndDatum());
				mitarbeiterdaten.setVerfuegbareUrlaubstage(mitarbeiterdaten.getVerfuegbareUrlaubstage() + (int) tage);
				mitarbeiterdatenRepository.save(mitarbeiterdaten);
			}
		}
	}

	public long berechneTage(LocalDate start, LocalDate end) {
		Set<Holiday> feiertage = getFeiertage(start, end);
		long tage = 0;
		for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
			LocalDate day = date;
			DayOfWeek dayOfWeek = date.getDayOfWeek();
			boolean isWeekend = (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY);
			boolean isFeiertag = feiertage.stream().anyMatch(f -> f.getDate().equals(day));
			if (!isWeekend && !isFeiertag) {
				tage++;
			}
		}
		return tage;
	}

	public Set<Holiday> getFeiertage(int year) {
		HolidayManager manager = HolidayManager.getInstance(ManagerParameters.create(GERMANY));
		return manager.getHolidays(Year.of(year));
	}

	public Set<Holiday> getFeiertage(LocalDate start, LocalDate end) {
		HolidayManager manager = HolidayManager.getInstance(ManagerParameters.create(GERMANY));
		return manager.getHolidays(start, end);
	}
}
