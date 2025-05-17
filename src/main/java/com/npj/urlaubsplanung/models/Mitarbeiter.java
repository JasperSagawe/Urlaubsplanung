
package com.npj.urlaubsplanung.models;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
public class Mitarbeiter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String vorname;
    private String nachname;
    private String email;
    private Boolean aktiv = true;
    private String passwortHash;
    private Integer loginVersuche = 0;
    private Boolean firstLogin = true;

    private Timestamp lastLogin;

    public Mitarbeiter() {}

    public Mitarbeiter(Integer id, String vorname, String nachname, String email) {
        this.id = id;
        this.vorname = vorname;
        this.nachname = nachname;
        this.email = email;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getVorname() { return vorname; }
    public void setVorname(String vorname) { this.vorname = vorname; }

    public String getNachname() { return nachname; }
    public void setNachname(String nachname) { this.nachname = nachname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public Boolean getAktiv() {
        return aktiv;
    }

    public void setAktiv(Boolean aktiv) {
        this.aktiv = aktiv;
    }

    public String getPasswortHash() { return passwortHash; }
    public void setPasswortHash(String passwortHash) { this.passwortHash = passwortHash; }

    public Integer getLoginVersuche() { return loginVersuche; }
    public void setLoginVersuche(Integer loginVersuche) { this.loginVersuche = loginVersuche; }

    public Boolean getFirstLogin() { return firstLogin; }
    public void setFirstLogin(Boolean firstLogin) { this.firstLogin = firstLogin; }

    public Timestamp getLastLogin() { return lastLogin; }
    public void setLastLogin(Timestamp lastLogin) { this.lastLogin = lastLogin; }
}
