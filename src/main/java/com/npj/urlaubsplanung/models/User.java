package com.npj.urlaubsplanung.models;

public class User {

	private String username;
	private String password;
	private int urlaubstage;
	private int resturlaub;

	public User(String username, String password, int urlaubstage, int resturlaub) {
		this.username = username;
		this.password = password;
		this.setUrlaubstage(urlaubstage);
		this.setResturlaub(resturlaub);
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
