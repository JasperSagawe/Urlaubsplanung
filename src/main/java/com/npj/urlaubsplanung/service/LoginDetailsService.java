package com.npj.urlaubsplanung.service;

import com.npj.urlaubsplanung.repository.MitarbeiterRepository;
import com.npj.urlaubsplanung.repository.MitarbeiterdatenRepository;
import com.npj.urlaubsplanung.security.LoginDetails;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginDetailsService implements UserDetailsService {

    private final MitarbeiterRepository mitarbeiterRepository;
    private final MitarbeiterdatenRepository mitarbeiterdatenRepository;
    private final UrlaubsjahrService urlaubsjahrService;

    public LoginDetailsService(MitarbeiterRepository mitarbeiterRepository,
                               MitarbeiterdatenRepository mitarbeiterdatenRepository,
                               UrlaubsjahrService urlaubsjahrService) {
        this.mitarbeiterRepository = mitarbeiterRepository;
        this.mitarbeiterdatenRepository = mitarbeiterdatenRepository;
        this.urlaubsjahrService = urlaubsjahrService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return mitarbeiterRepository.findByEmail(email)
                .map(mitarbeiter -> {
                    // Jahresdaten prüfen und ggf. aktualisieren
                    var daten = mitarbeiterdatenRepository.findByMitarbeiter(mitarbeiter);
                    urlaubsjahrService.aktualisiereEinzelnenDatensatz(daten, java.time.LocalDate.now().getYear());
                    mitarbeiterdatenRepository.save(daten);
                    
                    return new LoginDetails(mitarbeiter);
                })
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }
}
