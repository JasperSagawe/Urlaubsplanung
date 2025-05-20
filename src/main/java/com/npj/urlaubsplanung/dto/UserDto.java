package com.npj.urlaubsplanung.dto;

public class UserDto {

	private String username;
	private String password;
	private int urlaubstage;
	private int resturlaub;

	public UserDto(String username, String password, int urlaubstage, int resturlaub) {
		this.username = username;
		this.password = password;
		this.urlaubstage = urlaubstage;
		this.resturlaub = resturlaub;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getUrlaubstage() {
		return urlaubstage;
	}

	public void setUrlaubstage(int urlaubstage) {
		this.urlaubstage = urlaubstage;
	}

	public int getResturlaub() {
		return resturlaub;
	}

	public void setResturlaub(int resturlaub) {
		this.resturlaub = resturlaub;
	}
}
