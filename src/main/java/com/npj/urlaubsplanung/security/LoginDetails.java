package com.npj.urlaubsplanung.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.npj.urlaubsplanung.model.Mitarbeiter;

public class LoginDetails implements UserDetails {
	private static final long serialVersionUID = 1L;

	private Mitarbeiter mitarbeiter;

	public LoginDetails(Mitarbeiter mitarbeiter) {
		this.mitarbeiter = mitarbeiter;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		String rolle = mitarbeiter.getUserRole() != null
				? mitarbeiter.getUserRole().getRolleName().toUpperCase().replace("-", "_").replace(" ", "_")
				: "KEIN_ADMIN";

		return List.of(new SimpleGrantedAuthority("ROLE_" + rolle));
	}

	public Mitarbeiter getMitarbeiter() {
		return mitarbeiter;
	}

	@Override
	public String getPassword() {
		return mitarbeiter.getPasswortHash();
	}

	@Override
	public String getUsername() {
		return mitarbeiter.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}