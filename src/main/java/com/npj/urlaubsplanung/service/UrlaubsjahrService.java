package com.npj.urlaubsplanung.service;

import com.npj.urlaubsplanung.model.Mitarbeiterdaten;
import com.npj.urlaubsplanung.repository.MitarbeiterdatenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UrlaubsjahrService {

    @Autowired
    private MitarbeiterdatenRepository mitarbeiterdatenRepository;

    public void aktualisiereAlleMitarbeiterdaten() {
        List<Mitarbeiterdaten> alleDaten = mitarbeiterdatenRepository.findAll();
        int aktuellesJahr = LocalDate.now().getYear();

        for (Mitarbeiterdaten daten : alleDaten) {
            aktualisiereEinzelnenDatensatz(daten, aktuellesJahr);
        }

        mitarbeiterdatenRepository.saveAll(alleDaten);
    }

    public void aktualisiereEinzelnenDatensatz(Mitarbeiterdaten daten, int aktuellesJahr) {
        while (daten.getAktuellesJahr() < aktuellesJahr) {
            int rest = daten.getVerfuegbareUrlaubstage();
            daten.setResturlaubVorjahr(rest);
            daten.setVerfuegbareUrlaubstage(daten.getUrlaubstageProJahr() + rest);
            daten.setAktuellesJahr(daten.getAktuellesJahr() + 1);
        }
    }
}
