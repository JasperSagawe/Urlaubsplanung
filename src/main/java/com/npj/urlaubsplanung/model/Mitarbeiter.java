
package com.npj.urlaubsplanung.model;

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
	private String passwortHash;

	@ManyToOne
	@JoinColumn(name = "admin_role_id")
	private AdminRole adminRole;

	private Boolean aktiv = true;
	private Integer loginVersuche = 0;
	private Boolean firstLogin = true;
	private Timestamp lastLogin;

	public Mitarbeiter() {
	}

	public Mitarbeiter(String vorname, String nachname, String email, AdminRole adminRole) {
		this.vorname = vorname;
		this.nachname = nachname;
		this.email = email;
		this.adminRole = adminRole;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getNachname() {
		return nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswortHash() {
		return passwortHash;
	}

	public void setPasswortHash(String passwortHash) {
		this.passwortHash = passwortHash;
	}

	public AdminRole getAdminRole() {
		return adminRole;
	}

	public void setAdminRole(AdminRole adminRole) {
		this.adminRole = adminRole;
	}

	public Boolean getAktiv() {
		return aktiv;
	}

	public void setAktiv(Boolean aktiv) {
		this.aktiv = aktiv;
	}

	public Integer getLoginVersuche() {
		return loginVersuche;
	}

	public void setLoginVersuche(Integer loginVersuche) {
		this.loginVersuche = loginVersuche;
	}

	public Boolean getFirstLogin() {
		return firstLogin;
	}

	public void setFirstLogin(Boolean firstLogin) {
		this.firstLogin = firstLogin;
	}

	public Timestamp getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Timestamp lastLogin) {
		this.lastLogin = lastLogin;
	}
}
