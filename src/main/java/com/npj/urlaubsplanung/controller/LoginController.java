
package com.npj.urlaubsplanung.controller;

import com.npj.urlaubsplanung.models.Mitarbeiter;
import com.npj.urlaubsplanung.repository.MitarbeiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private MitarbeiterRepository mitarbeiterRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping
    public String loginForm() {
        return "login"; // login.html
    }

    @PostMapping
    public String handleLogin(@RequestParam String email,
                              @RequestParam String password,
                              Model model) {
        Optional<Mitarbeiter> opt = mitarbeiterRepository.findByEmail(email);

        if (opt.isEmpty()) {
            model.addAttribute("fehler", "E-Mail nicht gefunden.");
            return "login";
        }

        Mitarbeiter m = opt.get();

        if (m.getLoginVersuche() >= 5) {
            model.addAttribute("fehler", "Account gesperrt nach zu vielen Fehlversuchen.");
            return "login";
        }

        if (!passwordEncoder.matches(password, m.getPasswortHash())) {
            m.setLoginVersuche(m.getLoginVersuche() + 1);
            mitarbeiterRepository.save(m);
            model.addAttribute("fehler", "Falsches Passwort.");
            return "login";
        }

        // Erfolgreich
        m.setLoginVersuche(0);
        m.setLastLogin(Timestamp.from(Instant.now()));
        m.setFirstLogin(false);
        mitarbeiterRepository.save(m);

        model.addAttribute("nutzer", m);
        return "redirect:/kalender"; // oder dashboard
    }
}
